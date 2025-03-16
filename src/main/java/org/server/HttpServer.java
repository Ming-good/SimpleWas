package org.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.mapping.ServletFactory;
import org.resource.ResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpServer {

    private static final Logger logger = LoggerFactory.getLogger(HttpServer.class);
    private static final int NUM_THREADS = 50;
    private final Integer port;
    private final List<HashMap<String, Object>> virtualHostList;
    public HttpServer() throws IOException {
        Map<String, Object> host = getVhost();
        this.port = (Integer) host.get("port");
        this.virtualHostList = (List<HashMap<String, Object>>) host.get("virtualHost");
    }

    public void start(Class clazz) throws Exception {
        ServletFactory.init(clazz);
        ExecutorService pool = Executors.newFixedThreadPool(NUM_THREADS);
        try (ServerSocket server = new ServerSocket(port)) {
            logger.info("Accepting connections on port " + server.getLocalPort());
            while (true) {
                try {
                    Socket request = server.accept();
                    Runnable r = new RequestProcessor(virtualHostList, request);
                    pool.submit(r);
                } catch (IOException ex) {
                    logger.info("Error accepting connection", ex);
                }
            }
        }
    }
    private Map<String, Object> getVhost() throws IOException {
        Map<String, Object> host;
        ResourceLoader resourceLoader = new ResourceLoader("");
        InputStream resourceAsStream = resourceLoader.getResource("vhost.json");
        if (resourceAsStream == null) {
            new FileNotFoundException();
        }

        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resourceAsStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            ObjectMapper mapper = new ObjectMapper();
            host = mapper.readValue(builder.toString(), Map.class);
        }
        return host;
    }
}