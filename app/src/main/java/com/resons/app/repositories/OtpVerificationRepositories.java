package com.resons.app.repositories;

import com.resons.app.model.request.LoginRequest;
import com.resons.app.model.request.OtpVerificationRequest;
import com.resons.app.model.request.response.LoginResponse;
import com.resons.app.model.request.response.OtpVerificationResponse;
import com.resons.app.network.ApiClient;
import com.resons.app.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpVerificationRepositories {

    private final ApiService apiService;


    public OtpVerificationRepositories() {
        apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
    }

    public void postData(OtpVerificationRequest requestModel, final OtpVerificationRepositories.RepositoryCallback<OtpVerificationResponse> callback) {
        apiService.verifyOtp(requestModel).enqueue(new Callback<OtpVerificationResponse>() {
            @Override
            public void onResponse(Call<OtpVerificationResponse> call, Response<OtpVerificationResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    if (response.code() == 401 || response.code()==400) {
                        callback.onError("Invalid Otp");
                    } else {
                        callback.onError("Error: " + response.message());

                    }
                }
            }

            @Override
            public void onFailure(Call<OtpVerificationResponse> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public interface RepositoryCallback<T> {
        void onSuccess(T result);

        void onError(String message);
    }
}
