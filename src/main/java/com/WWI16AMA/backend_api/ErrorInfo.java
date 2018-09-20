package com.WWI16AMA.backend_api;

public class ErrorInfo {

    public final String url;
    public final String exception;

    public ErrorInfo(String url, Exception ex) {
        this.url = url;
        this.exception = ex.getLocalizedMessage();
    }
}