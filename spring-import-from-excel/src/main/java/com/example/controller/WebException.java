package com.example.controller;

import org.springframework.http.HttpStatus;

public class WebException extends RuntimeException {

    private final HttpStatus status;

    public WebException(HttpStatus status) {
        this.status = status;
    }

    public WebException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
