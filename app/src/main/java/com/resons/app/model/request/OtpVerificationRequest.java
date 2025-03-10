package com.resons.app.model.request;

public class OtpVerificationRequest {
    private String email;
    private String otp;
    public OtpVerificationRequest(String email, String otp) {
        this.email = email;
        this.otp = otp;
    }




}
