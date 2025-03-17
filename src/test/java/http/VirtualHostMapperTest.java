package http;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.http.response.VirtualHostMapper;
import org.junit.Test;
import org.resource.ResourceLoader;
import org.resource.ResourceWrite;

public class VirtualHostMapperTest {

    @Test
    public void 가상호스트_파싱() throws IOException, NoSuchFieldException {
        Map<String, Object> vhost = getVhost();
        List<HashMap<String, Object>> virtualHostList = (List<HashMap<String, Object>>) vhost.get("virtualHost");

        ResourceWrite parse = VirtualHostMapper.parse("www.a.co.kr", virtualHostList);
        assertEquals("/hellow.html", parse.getTarget());
    }

    @Test
    public void 존재하지_않는_호스트_요청시_오류반환() throws IOException, NoSuchFieldException {
        Map<String, Object> vhost = getVhost();
        List<HashMap<String, Object>> virtualHostList = (List<HashMap<String, Object>>) vhost.get("virtualHost");

        assertThrows(NoSuchFieldException.class, () -> VirtualHostMapper.parse("www.b.co.kr", virtualHostList));
    }
    public Map<String, Object> getVhost() throws IOException {
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
