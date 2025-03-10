package com.resons.app.model.request.response;


public class ForgotOtpResponse {
    public String token;
    public String message;
    public int status;

    public ForgotOtpResponse(String token,String message, int status) {
        this.token=token;
        this.message = message;
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

