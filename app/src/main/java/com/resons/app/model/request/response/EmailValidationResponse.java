package com.resons.app.model.request.response;

public class EmailValidationResponse {
    private boolean valid;

    private String message;
    private int status;

    public EmailValidationResponse(boolean isvalid, String message, int status) {
        this.valid = isvalid;
        this.message = message;
        this.status = status;
    }


    // Getters and setters
    public String getMessage() {
        return message;
    }

    public int status() {
        return status;
    }

    public boolean isValid() {
        return valid;
    }


    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
