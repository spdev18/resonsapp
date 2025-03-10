package com.resons.app.repositories;

import com.resons.app.model.request.ForgotPasswordOtpRequest;
import com.resons.app.model.request.ForgotPasswordRequest;
import com.resons.app.model.request.response.ForgotOtpResponse;
import com.resons.app.model.request.response.ForgotPasswordResponse;
import com.resons.app.network.ApiClient;
import com.resons.app.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotOtpRepositories {

    private final ApiService apiService;


    public ForgotOtpRepositories() {
        apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
    }

    public void postData(ForgotPasswordOtpRequest requestModel, final ForgotOtpRepositories.RepositoryCallback<ForgotOtpResponse> callback) {
        apiService.forgotOtp(requestModel).enqueue(new Callback<ForgotOtpResponse>() {
            @Override
            public void onResponse(Call<ForgotOtpResponse> call, Response<ForgotOtpResponse> response) {
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
            public void onFailure(Call<ForgotOtpResponse> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public interface RepositoryCallback<T> {
        void onSuccess(T result);

        void onError(String message);
    }
}
