package org.mapping;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import org.annotation.Servlet;
import org.handler.RouteHandler;
import org.http.HttpMethod;
import org.http.request.HttpRequest;
import org.http.response.HttpResponse;
import org.servlet.SimpleServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServletFactory {
    private static final Logger logger = LoggerFactory.getLogger(ServletFactory.class);
    private static final Map<String, SimpleServlet> servletMap = new HashMap<>();
    private static String servletRootPackage = "";
    public static void init(Class clazz) throws NoSuchMethodException {
        servletRootPackage = clazz.getPackage().getName();
        ServiceLoader<SimpleServlet> loader = ServiceLoader.load(SimpleServlet.class);
        for (SimpleServlet servlet : loader) {
            servletMap.put(servlet.getClass().getName(), createServlet(servlet));
        }
    }

    public static SimpleServlet search(String url) {
        String clazz = servletRootPackage + "." + url;
        logger.debug("[WAS] mapping package : " + clazz);
        if (!servletMap.containsKey(clazz)) {
            return null;
        }

        return servletMap.get(clazz);
    }

    private static RouteHandler createServlet(SimpleServlet servlet) throws NoSuchMethodException {
        Method service = servlet.getClass().getMethod("service", HttpRequest.class, HttpResponse.class);
        if (!service.isAnnotationPresent(Servlet.class)) {
            return new RouteHandler(servlet, HttpMethod.GET);
        }

        Servlet servletAnnotation = service.getAnnotation(Servlet.class);
        if(!HttpMethod.isValidMethod(servletAnnotation.value())) {
            throw new IllegalArgumentException("지원되지 않는 메소드 입니다");
        }

        return new RouteHandler(servlet, HttpMethod.getMethod(servletAnnotation.value()));
    }
}
