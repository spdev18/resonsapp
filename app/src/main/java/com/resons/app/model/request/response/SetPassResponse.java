package com.resons.app.model.request.response;

public class SetPassResponse {
    private String message;
    private int status;

    public SetPassResponse(String message, int status) {
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


