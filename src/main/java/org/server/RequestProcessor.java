package org.server;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import org.handler.ServletHandler;
import org.http.request.HttpRequest;
import org.http.request.HttpRequestParser;
import org.http.response.Dispacher;
import org.http.response.HttpResponse;
import org.mapping.strategy.PackageMappingStrategy;
import org.resource.ResourceWrite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestProcessor implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestProcessor.class);
    private List<HashMap<String, Object>> virtualHostList;
    private Socket connection;

    public RequestProcessor(List<HashMap<String, Object>> virtualHostList, Socket connection){
        this.virtualHostList = virtualHostList;
        this.connection = connection;
    }

    @Override
    public void run() {
        try {
            HttpRequest httpRequest = HttpRequestParser.parse(connection.getInputStream());
            ResourceWrite resourceWrite = Dispacher.parse(httpRequest.getHeaderMap().get("Host"), virtualHostList);
            HttpResponse httpResponse = new HttpResponse(resourceWrite.getTarget(), resourceWrite, connection.getOutputStream());

            ServletHandler requestHandler = new ServletHandler(new PackageMappingStrategy());
            requestHandler.handler(httpRequest, httpResponse);

        } catch (IOException e) {
            logger.error("Error talking to " + connection.getRemoteSocketAddress(), e);
        } catch (NoSuchFieldException e) {
            logger.error("Error not found host " + connection.getRemoteSocketAddress(), e);
        } finally {
            try {
                connection.close();
            } catch (IOException ex) {
            }
        }
    }


}