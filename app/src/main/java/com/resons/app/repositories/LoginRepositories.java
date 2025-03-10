package com.resons.app.repositories;

import com.resons.app.model.request.LoginRequest;
import com.resons.app.model.request.response.LoginResponse;
import com.resons.app.network.ApiClient;
import com.resons.app.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRepositories
{
    private final ApiService apiService;


    public LoginRepositories() {
        apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
    }

    public void postData(LoginRequest requestModel, final RepositoryCallback<LoginResponse> callback) {
        apiService.loginUser(requestModel).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    if (response.code() == 401) {
                        callback.onError("Invalid login credentials. Please try again.");
                    } else {
                        callback.onError("Error: " + response.message());

                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public interface RepositoryCallback<T> {
        void onSuccess(T result);

        void onError(String message);
    }
}
