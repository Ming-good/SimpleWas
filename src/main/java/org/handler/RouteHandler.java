package org.handler;

import org.http.HttpMethod;
import org.http.request.HttpRequest;
import org.http.response.HttpResponse;
import org.servlet.SimpleServlet;

public class RouteHandler implements SimpleServlet{
    private SimpleServlet simpleServlet;
    private HttpMethod method;

    public RouteHandler(SimpleServlet simpleServlet, HttpMethod method) {
        this.simpleServlet = simpleServlet;
        this.method = method;
    }

    public HttpMethod getMethod() {
        return method;
    }

    @Override
    public void service(HttpRequest req, HttpResponse res) throws Exception {
        this.simpleServlet.service(req,res);
    }
}
