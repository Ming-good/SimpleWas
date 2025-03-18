package org.resource;

import static org.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.http.HttpStatus.NOT_FOUND;
import static org.http.HttpStatus.OK;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.http.HttpStatus;
import org.http.request.HttpRequest;
import org.http.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceWrite {
    private static final Logger logger = LoggerFactory.getLogger(ResourceWrite.class);
    private ResourceLoader resourceLoader;
    private String target = "";
    private HashMap<String, String> errorPage = null;

    public ResourceWrite(ResourceLoader resourceLoader, String indexFileNm, HashMap<String, String> errorPage) {
        this.resourceLoader = resourceLoader;
        this.target = indexFileNm;
        this.errorPage = errorPage;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getTarget() {
        return target;
    }

    public void forward(HttpRequest request, HttpResponse response) {
        try(OutputStream raw = new BufferedOutputStream(response.getOutputStream());
                Writer out = new OutputStreamWriter(raw)) {

            String fileName = this.target;
            InputStream resource = resourceLoader.getResource(fileName);

            if (resource == null) {
                error(request, response, NOT_FOUND);
                return;
            }

            String contentType = URLConnection.getFileNameMap().getContentTypeFor(fileName);
            byte[] content = loadHtmlTemplate(response,resource);

            if (isHttpRequest(request)) { // send a MIME header
                sendHeader(out, OK.getMessage(), contentType, content.length);
            }
            raw.write(content);
            raw.flush();
        } catch (IOException e) {
            error(request, response, INTERNAL_SERVER_ERROR);
            logger.error("[ERROR] FORWARD FAIL ",e);
        }
    }


    public void error(HttpRequest request, HttpResponse response, HttpStatus httpResCode) {
        try(OutputStream raw = new BufferedOutputStream(response.getOutputStream());
                Writer out = new OutputStreamWriter(raw)) {

            InputStream resource = resourceLoader.getResource(errorPage.get(httpResCode.getCode()));

            if (resource == null) {
                byte[] selfError = getSelfError(request, response).getBytes();
                sendHeader(out, INTERNAL_SERVER_ERROR.getMessage(),"text/html; charset=utf-8", selfError.length);
                raw.write(selfError);
                raw.flush();
                throw new FileNotFoundException("NOT FOUND ERRROR PAGES");
            }
            
            byte[] content = resource.readAllBytes();
            if (isHttpRequest(request)) { // send a MIME header
                sendHeader(out, httpResCode.getMessage(),"text/html; charset=utf-8", content.length);
            }
            raw.write(content);
            raw.flush();
        } catch (FileNotFoundException e){
            logger.error("[WAS] NOT FOUND ERRROR PAGES", e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[]  loadHtmlTemplate(HttpResponse response, InputStream resource) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                contentBuilder.append(line).append(System.lineSeparator());
            }
        }

        String content = contentBuilder.toString();
        Map<String, String> attributeMap = response.getAttributeMap();

        for (Map.Entry<String, String> entry : attributeMap.entrySet()) {
            String key = "\\{\\{" + Pattern.quote(entry.getKey()) + "\\}\\}";
            content = content.replaceAll(key, Matcher.quoteReplacement(entry.getValue()));
        }

        return content.getBytes();
    }

    private boolean isHttpRequest(HttpRequest request) {
        return request.getVersion().startsWith("HTTP/");
    }
    private void sendHeader(Writer out, String responseCode, String contentType, int length) throws IOException {
        out.write(responseCode + "\r\n");
        Date now = new Date();
        out.write("Date: " + now + "\r\n");
        out.write("Server: JHTTP 2.0\r\n");
        out.write("Content-length: " + length + "\r\n");
        out.write("Content-type: " + contentType + "\r\n\r\n");
        out.flush();
    }
    private String getSelfError(HttpRequest request, HttpResponse response) {
        String body = new StringBuilder("<HTML>\r\n").append("<HEAD><TITLE>Server ERROR</TITLE>\r\n").append("</HEAD>\r\n")
                                                     .append("<BODY>")
                                                     .append("<H1>"+ INTERNAL_SERVER_ERROR.getMessage()+"</H1>\r\n")
                                                     .append("</BODY></HTML>\r\n").toString();

        return body;
    }
}
