package com.example.controller;

import java.time.LocalDateTime;

public class ErrorResponse {

    public String errorText;

    public LocalDateTime dateTime = LocalDateTime.now();

    public ErrorResponse(String errorText) {
        this.errorText = errorText;
    }

    public String getErrorText() {
        return errorText;
    }

    public void setErrorText(String errorText) {
        this.errorText = errorText;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
