package com.resons.app.model.request.response;

import com.resons.app.model.request.OtpVerificationRequest;

public class OtpVerificationResponse {
    private int status;
    private String message;
    private String token;

    public OtpVerificationResponse(int status, String message, String token) {
        this.status = status;
        this.message = message;
        this.token = token;
    }

    public int status() {
        return status;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
