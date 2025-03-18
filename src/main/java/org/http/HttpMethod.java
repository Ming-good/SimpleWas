package org.http;

public enum HttpMethod {
    GET, POST;

    public static HttpMethod getMethod(String method) {
        for (HttpMethod httpMethod : values()) {
            if (httpMethod.name().equalsIgnoreCase(method)) {
                return httpMethod;
            }
        }
        return null;
    }

    public static boolean isValidMethod(String method) {
        for (HttpMethod httpMethod : values()) {
            if (httpMethod.name().equalsIgnoreCase(method)) {
                return true;
            }
        }
        return false;
    }
}
