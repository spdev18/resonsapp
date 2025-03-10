package com.resons.app.repositories;

import com.resons.app.model.request.ForgotPasswordRequest;
import com.resons.app.model.request.SetPassRequest;
import com.resons.app.model.request.response.ForgotPasswordResponse;
import com.resons.app.model.request.response.SetPassResponse;
import com.resons.app.network.ApiClient;
import com.resons.app.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordRepositories
{
    private final ApiService apiService;


    public ForgotPasswordRepositories() {
        apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
    }

    public void postData( ForgotPasswordRequest requestModel, final ForgotPasswordRepositories.RepositoryCallback<ForgotPasswordResponse> callback) {
        apiService.doForgotPassword(requestModel).enqueue(new Callback<ForgotPasswordResponse>() {
            @Override
            public void onResponse(Call<ForgotPasswordResponse> call, Response<ForgotPasswordResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    if (response.code() == 401 || response.code()==400) {
                        callback.onError("Email not exits");
                    } else {
                        callback.onError("Error: " + response.message());

                    }
                }
            }

            @Override
            public void onFailure(Call<ForgotPasswordResponse> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public interface RepositoryCallback<T> {
        void onSuccess(T result);

        void onError(String message);
    }
}
