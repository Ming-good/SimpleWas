package org.handler;

import static org.http.HttpStatus.FORBIDDEN;
import static org.http.HttpStatus.INTERNAL_SERVER_ERROR;

import org.http.HttpStatus;
import org.http.request.HttpRequest;
import org.http.response.HttpResponse;
import org.mapping.strategy.ServletMappingStrategy;
import org.resource.ResourceWrite;
import org.servlet.SimpleServlet;

public class ServletHandler {

    private final ServletMappingStrategy strategy;

    public ServletHandler(ServletMappingStrategy strategy) {
        this.strategy = strategy;
    }

    public void handler(HttpRequest request, HttpResponse response) {

        try {
            if (isForbiddenPath(request)) {
                sendError(request,response,FORBIDDEN);
                return;
            }

            if (isIndexRequest(request,response)) {
                forwardResource(request,response);
                return;
            }

            SimpleServlet simpleServlet = strategy.getServlet(request.getPath());
            if (simpleServlet == null) {
                forwardResource(request,response,request.getPath());
                return;
            }

            simpleServlet.service(request, response);
        } catch (Exception e) {
            sendError(request, response, INTERNAL_SERVER_ERROR);
        }
    }

    private boolean isForbiddenPath(HttpRequest request) {
        String path = request.getPath();

        if (path.contains("..") || path.endsWith(".exe") || path.endsWith(".sh") || path.endsWith(".git") || path.endsWith(".bat")) {
            return true;
        }

        return false;
    }

    private boolean isIndexRequest(HttpRequest request, HttpResponse response) {
        String path = request.getPath();
        return path.length() == 1 || path.equals(response.getIndexFileNm());
    }

    private void forwardResource(HttpRequest request, HttpResponse response) {
        ResourceWrite resourceWrite = response.getResourceWrite();
        resourceWrite.forward(request, response);
    }

    private void forwardResource(HttpRequest request, HttpResponse response, String resourcePath) {
        ResourceWrite resourceWrite = response.getResourceWrite(resourcePath);
        resourceWrite.forward(request, response);
    }

    private void sendError(HttpRequest request, HttpResponse response, HttpStatus status) {
        ResourceWrite resourceWrite = response.getResourceWrite();
        resourceWrite.error(request, response, status);
    }
}
