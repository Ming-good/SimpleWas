package org.http;

public enum HttpStatus {
    OK("200", "HTTP/1.1 200 OK"),
    FORBIDDEN("403", "HTTP/1.1 403 Forbidden"),
    NOT_FOUND("404", "HTTP/1.1 404 Not Found"),
    INTERNAL_SERVER_ERROR("500", "HTTP/1.1 500 Internal Server Error");

    private final String code;
    private final String message;

    HttpStatus(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
