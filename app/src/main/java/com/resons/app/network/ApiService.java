package com.resons.app.network;

import com.google.gson.JsonArray;
import com.resons.app.model.request.EmailValidationRequest;
import com.resons.app.model.request.ForgotPasswordOtpRequest;
import com.resons.app.model.request.ForgotPasswordRequest;
import com.resons.app.model.request.GmailLoginRequest;
import com.resons.app.model.request.NotificationSeenRequest;
import com.resons.app.model.request.response.EmailValidationResponse;
import com.resons.app.model.request.LoginRequest;
import com.resons.app.model.request.response.ForgotOtpResponse;
import com.resons.app.model.request.response.ForgotPasswordResponse;
import com.resons.app.model.request.response.LoginResponse;
import com.resons.app.model.request.OtpVerificationRequest;
import com.resons.app.model.request.response.NotificationsModel;
import com.resons.app.model.request.response.NotificationsSeenRespone;
import com.resons.app.model.request.response.OtpVerificationResponse;
import com.resons.app.model.request.SetPassRequest;
import com.resons.app.model.request.response.SetPassResponse;
import com.resons.app.model.request.TranscriptionRequest;
import com.resons.app.UpdateRequest;
import com.resons.app.UpdateResponse;
import com.resons.app.model.request.response.UserProfile;
import com.resons.app.model.request.response.TranslatorResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;

public interface ApiService {
    @POST("registration")
        // Replace with your API endpoint
    Call<EmailValidationResponse> validateEmail(@Body EmailValidationRequest request);


    @POST("forgotpassword")
    Call<ForgotPasswordResponse> doForgotPassword(@Body ForgotPasswordRequest request);

    @POST("otp_verify")
        // Replace with your API endpoint
    Call<OtpVerificationResponse> verifyOtp(@Body OtpVerificationRequest request);


    @POST("otp_verify")
        // Replace with your API endpoint
    Call<ForgotOtpResponse> forgotOtp(@Body ForgotPasswordOtpRequest request);


    @POST("log-in")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    @POST("sign-in")
    Call<LoginResponse> gmailLogin(@Body GmailLoginRequest request);

    @POST("text/create")
        // Change "transcription" to the actual endpoint path.
    Call<TranslatorResponse> addTranslator(@Header("Authorization") String token, @Body TranscriptionRequest transcriptionRequest);

    @PUT("profile/update")
        // Replace "update-name" with your API endpoint
    Call<UpdateResponse> updateName(@Header("Authorization") String token, @Body UpdateRequest request);

    @Multipart
    @PUT("profile/update")
        // Replace "update-name" with your API endpoint
    Call<UpdateResponse> updateProfilePicture(@Header("Authorization") String token,
                                              @Part MultipartBody.Part image);


    @FormUrlEncoded
    @PUT("profile/update")
        // Replace "update-name" with your API endpoint
    Call<UpdateResponse> updateProfile(@Header("Authorization") String token,
                                       @Field("name") String name,
                                       @Field("dob") String dob,
                                       @Field("gender") String gender
    );

    @PUT("profile/update")
        // Replace "update-name" with your API endpoint
    Call<SetPassResponse> updatepass(@Header("Authorization") String token, @Body SetPassRequest request);

    @GET("profile")
        // Replace with your actual API endpoint
    Call<UserProfile> getUserProfile(@Header("Authorization") String token);


    @GET("notifications")
    Call<JsonArray> getNotifications(@Header("Authorization") String token);

    @GET("text/list")
    Call<JsonArray> getHistory(@Header("Authorization") String token);

    @PUT("mark-as-read")
    Call<NotificationsSeenRespone> addSeen(@Header("Authorization") String token,
                                           @Body NotificationSeenRequest request);
}
