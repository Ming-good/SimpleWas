package org.http.response;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import org.resource.ResourceWrite;

public class HttpResponse {

    private String indexFileNm = "";
    private ResourceWrite resourceWrite;
    private OutputStream outputStream;
    private Map<String, String> attribute;

    public HttpResponse(String indexFileNm, ResourceWrite resourceWrite, OutputStream outputStream) {
        this.indexFileNm = indexFileNm;
        this.resourceWrite = resourceWrite;
        this.outputStream = outputStream;
        this.attribute = new HashMap<>();
    }

    public ResourceWrite getResourceWrite(String fileNm) {
        resourceWrite.setTarget(fileNm);
        return resourceWrite;
    }

    public Map<String, String> getAttributeMap() {
        return attribute;
    }

    public String getAttribute(String key) {
        return attribute.get(key);
    }

    public void setAttribute(String key, String value) {
        attribute.put(key, value);
    }

    public ResourceWrite getResourceWrite() {
        return resourceWrite;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public String getIndexFileNm() {
        return indexFileNm;
    }
}
