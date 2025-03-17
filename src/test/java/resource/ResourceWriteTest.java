package resource;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import org.http.request.HttpRequest;
import org.http.response.HttpResponse;
import org.junit.Before;
import org.junit.Test;
import org.resource.ResourceLoader;
import org.resource.ResourceWrite;

public class ResourceWriteTest {

    private HttpRequest request;
    private HttpResponse response;
    private ResourceWrite resourceWrite;
    private ResourceLoader resourceLoader;
    @Before
    public void init() {
        request = mock(HttpRequest.class);
        response = mock(HttpResponse.class);
        resourceLoader = mock(ResourceLoader.class);
        resourceWrite = new ResourceWrite(resourceLoader, "index.html",  new HashMap<>());
    }

    @Test
    public void 오류파일을찾을수없습니다() {
        when(response.getOutputStream()).thenReturn(mock());
        when(resourceLoader.getResource(anyString())).thenReturn(null);

        resourceWrite.forward(request, response);

        willThrow(FileNotFoundException.class);
    }

    @Test
    public void 성공() {
        String html = "<html>Hello {{name}}!</html>";
        InputStream stream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        HashMap<String, String> attributeMap = new HashMap<>();
        attributeMap.put("name", "서블릿테스트");

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        when(resourceLoader.getResource(any())).thenReturn(stream);
        when(response.getOutputStream()).thenReturn(out);
        when(response.getAttributeMap()).thenReturn(attributeMap);
        when(request.getVersion()).thenReturn("HTTP/1.1");

        resourceWrite.forward(request, response);
        String convertHtml = out.toString(StandardCharsets.UTF_8);
        assertTrue(convertHtml.contains("서블릿테스트"));
    }
}
