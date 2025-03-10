package com.resons.app.model.request.response;

public class LoginResponse {
    private String message;
    private int status;
    private String token;

    public LoginResponse(String message, int status, String token) {
        this.message = message;
        this.status = status;
        this.token = token;
    }

    // Getters and setters
    public String getMessage() {
        return message;
    }

    public int status() {
        return status;
    }

    public String getToken() {
        return token;
    }
}
