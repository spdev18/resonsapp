package com.resons.app.model.request.response;

public class NotificationsSeenRespone {
    public String message;
    public int status;

    public NotificationsSeenRespone(String message, int status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
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
