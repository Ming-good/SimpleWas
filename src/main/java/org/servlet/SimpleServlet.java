package org.servlet;

import org.http.request.HttpRequest;
import org.http.response.HttpResponse;

public interface SimpleServlet {
    public void service(HttpRequest req, HttpResponse res);
}
