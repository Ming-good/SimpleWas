package org.http.request;

import java.util.Map;

public class HttpRequest {
    private String method;
    private String path;
    private String version;
    private Map<String, String> headerMap;
    private Map<String, String> paramMap;

    public HttpRequest(String method, String path, String version,
            Map<String, String> headerMap, Map<String, String> paramMap) {
        this.method = method;
        this.path = path;
        this.version = version;
        this.headerMap = headerMap;
        this.paramMap = paramMap;
    }

    // Getter 메서드들
    public String getMethod() { return method; }
    public String getPath() { return path; }
    public String getVersion() { return version; }
    public Map<String, String> getHeaderMap() { return headerMap; }
    public Map<String, String> getParamMap() { return paramMap; }
}
