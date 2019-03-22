package com.WWI16AMA.backend_api;

public class ErrorInfo {

    private String url;
    private String exception;

    public ErrorInfo(Object req, Throwable ex) {
//        this.url = req.toString();
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
