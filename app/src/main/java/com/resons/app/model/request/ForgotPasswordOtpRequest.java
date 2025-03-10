package com.resons.app.model.request;

public class ForgotPasswordOtpRequest {
    public String otp;
    public String email;
    public String is_resetpassword;

    public ForgotPasswordOtpRequest(String otp, String email,
                                    String is_resetpassword) {
        this.otp = otp;
        this.email = email;
        this.is_resetpassword = is_resetpassword;
    }

}
