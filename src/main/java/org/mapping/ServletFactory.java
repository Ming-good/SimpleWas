package org.mapping;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import org.servlet.SimpleServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServletFactory {
    private static final Logger logger = LoggerFactory.getLogger(ServletFactory.class);
    private static final Map<String, SimpleServlet> servletMap = new HashMap<>();
    private static String servletRootPackage = "";
    public static void init(Class clazz) {
        servletRootPackage = clazz.getPackage().getName();
        ServiceLoader<SimpleServlet> loader = ServiceLoader.load(SimpleServlet.class);
        for (SimpleServlet servlet : loader) {
            servletMap.put(servlet.getClass().getName(), servlet);
        }
    }

    public static SimpleServlet createServlet(String url) {
        String clazz = servletRootPackage + "." + url;
        logger.debug("[WAS] mapping package : " + clazz);
        if (!servletMap.containsKey(clazz)) {
            return null;
        }

        return servletMap.get(clazz);
    }
}
