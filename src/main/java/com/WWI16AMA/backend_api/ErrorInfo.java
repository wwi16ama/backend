package com.WWI16AMA.backend_api;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;

public class ErrorInfo {

    private String url;
    private String exception;

    public ErrorInfo(HttpServletRequest req, Throwable ex) {
        this.url = req.getRequestURL().toString();
        this.exception = ex.getLocalizedMessage();
    }

    public ErrorInfo() {
    }

    public String getUrl() {
        return url;
    }

    public String getException() {
        return exception;
    }

}
