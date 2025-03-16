package org.http.request;

import static org.http.HttpMethod.GET;
import static org.http.HttpMethod.POST;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestParser {
    public static HttpRequest parse(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new BufferedInputStream(in), StandardCharsets.UTF_8));
        String reqLine = reader.readLine();
        String[] tokens = reqLine.split("\\s+");

        String method = tokens[0];
        String path = tokens[1];
        String version = "";
        if (tokens.length > 2) {
            version = tokens[2];
        }

        String line;
        Map<String, String> headerMap = new HashMap<>();
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            String[] headerStr = line.split(":");
            headerMap.put(headerStr[0].trim(), headerStr[1].trim());
        }

        Map<String, String> paramMap = new HashMap<>();
        if (method.equals(POST)) {
            StringBuilder bodyBuffer = new StringBuilder();
            while (reader.ready()) {
                bodyBuffer.append((char)reader.read());
            }

            if ("application/x-www-form-urlencoded".equalsIgnoreCase(headerMap.get("Content-Type"))) {
                paramMap = parseQueryStr(bodyBuffer.toString());
            }
        } else if (method.equals(GET) && path.contains("?")) {
            String[] split = path.split("\\?");
            path = split[0];
            paramMap = parseQueryStr(split[1]);
        }

        return new HttpRequest(method, path, version, headerMap, paramMap);
    }

    public static Map<String, String> parseQueryStr(String body) {
        Map<String, String> paramMap = new HashMap<>();
        for (String param : body.toString().split("&")) {
            String[] keyValue = param.split("=");
            if (keyValue.length == 2) {
                paramMap.put(keyValue[0], keyValue[1]);
            }
        }

        return paramMap;
    }
}
