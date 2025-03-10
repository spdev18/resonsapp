package com.resons.app.model.request;

public class GmailLoginRequest {
    public String email;

    public GmailLoginRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
