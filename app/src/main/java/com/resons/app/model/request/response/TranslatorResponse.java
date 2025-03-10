package com.resons.app.model.request.response;

public class TranslatorResponse
{
    public String message;
    public int status;

    public TranslatorResponse(String message,int status){
        this.message=message;
        this.status=status;
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
