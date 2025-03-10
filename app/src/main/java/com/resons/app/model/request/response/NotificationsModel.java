package com.resons.app.model.request.response;

public class NotificationsModel {
    private String id;
    private String message;
    private String timestamp;
    private boolean is_read;

    public NotificationsModel(String id, String message, String timestamp,
                              boolean is_read) {
        this.id = id;
        this.message = message;
        this.timestamp = timestamp;
        this.is_read = is_read;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isIs_read() {
        return is_read;
    }

    public void setIs_read(boolean is_read) {
        this.is_read = is_read;
    }
}
