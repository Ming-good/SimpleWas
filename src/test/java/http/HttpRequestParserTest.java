package http;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
        assertEquals("/abc?test=123123", httpRequest.getPath());
        assertEquals("HTTP/1.1", httpRequest.getVersion());
        assertEquals("www.a.co.kr", httpRequest.getHeaderMap().get("Host"));
        assertEquals("application/x-www-form-urlencoded", httpRequest.getHeaderMap().get("Content-Type"));
        assertTrue(httpRequest.getParamMap().isEmpty());
    }
}
