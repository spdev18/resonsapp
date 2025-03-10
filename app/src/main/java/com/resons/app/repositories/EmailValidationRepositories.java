package com.resons.app.repositories;

import com.resons.app.model.request.EmailValidationRequest;
import com.resons.app.model.request.LoginRequest;
import com.resons.app.model.request.response.EmailValidationResponse;
import com.resons.app.model.request.response.LoginResponse;
import com.resons.app.network.ApiClient;
import com.resons.app.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmailValidationRepositories {

    private final ApiService apiService;


    public EmailValidationRepositories() {
        apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
    }

    public void postData(EmailValidationRequest requestModel, final EmailValidationRepositories.RepositoryCallback<EmailValidationResponse> callback) {
        apiService.validateEmail(requestModel).enqueue(new Callback<EmailValidationResponse>() {
            @Override
            public void onResponse(Call<EmailValidationResponse> call, Response<EmailValidationResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    if (response.code() == 401 || response.code()==400) {
                        callback.onError("Email already exits");
                    } else {
                        callback.onError("Error: " + response.message());

                    }

                }
            }

            @Override
            public void onFailure(Call<EmailValidationResponse> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public interface RepositoryCallback<T> {
        void onSuccess(T result);

        void onError(String message);
    }
}
