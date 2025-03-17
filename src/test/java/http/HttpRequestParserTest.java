package http;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.http.request.HttpRequest;
import org.http.request.HttpRequestParser;
import org.junit.Test;

public class HttpRequestParserTest {

    @Test
    public void GET요청_파라미터검증() throws IOException {
        String request = "GET /abc?test=123123 HTTP/1.1\r\n" +
                "Host: www.a.co.kr\r\n" +
                "Content-Type: application/x-www-form-urlencoded\r\n" +
                "\r\n";
        InputStream inputStream = new ByteArrayInputStream(request.getBytes());
        HttpRequest httpRequest = HttpRequestParser.parse(inputStream);

        assertEquals("GET", httpRequest.getMethod());
        assertEquals("/abc", httpRequest.getPath());
        assertEquals("HTTP/1.1", httpRequest.getVersion());
        assertEquals("www.a.co.kr", httpRequest.getHeaderMap().get("Host"));
        assertEquals("123123", httpRequest.getParamMap().get("test"));
        assertEquals("application/x-www-form-urlencoded", httpRequest.getHeaderMap().get("Content-Type"));
    }

    @Test
    public void POST요청_파라미터검증() throws IOException {
        String request = POST요청_헤더();

        InputStream inputStream = new ByteArrayInputStream(request.getBytes());
        HttpRequest httpRequest = HttpRequestParser.parse(inputStream);

        assertEquals("POST", httpRequest.getMethod());
        assertEquals("/service.TimeView", httpRequest.getPath());
        assertEquals("HTTP/1.1", httpRequest.getVersion());
        assertEquals("www.a.co.kr", httpRequest.getHeaderMap().get("Host"));
        assertEquals("123123", httpRequest.getParamMap().get("test"));
        assertEquals("11", httpRequest.getParamMap().get("asd"));
        assertEquals("233", httpRequest.getParamMap().get("zzzx"));
        assertEquals("application/x-www-form-urlencoded", httpRequest.getHeaderMap().get("Content-Type"));
    }

    public String  POST요청_헤더() {
        StringBuilder sb = new StringBuilder();

        sb.append("POST /service.TimeView HTTP/1.1\r\n")
          .append("User-Agent: PostmanRuntime/7.43.2\r\n")
          .append("Accept: */*\r\n")
          .append("Postman-Token: b1ed3c6f-ad2d-4a24-a312-16a49a1cf3da\r\n")
          .append("Host: www.a.co.kr\r\n")
          .append("Accept-Encoding: gzip, deflate, br\r\n")
          .append("Connection: keep-alive\r\n")
          .append("Content-Type: application/x-www-form-urlencoded\r\n")
          .append("Content-Length: 27\r\n")
          .append("\r\n")
          .append("test=123123&asd=11&zzzx=233");

        return sb.toString();
    }
}
