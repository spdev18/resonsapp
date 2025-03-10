package com.resons.app;

public class UpdateResponse {
    private String message;
    private int status;

    public UpdateResponse(String message, int status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }
}
