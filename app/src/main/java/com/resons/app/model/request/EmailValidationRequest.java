package com.resons.app.model.request;

public class EmailValidationRequest {
private  String email;
    private String password;
    private String name;
    private String account_type;
    public EmailValidationRequest(String email,String password,String name,String account_type) {
        this.email = email;
        this.password = password;
        this.account_type=account_type;
        this.name=name;
    }


}
