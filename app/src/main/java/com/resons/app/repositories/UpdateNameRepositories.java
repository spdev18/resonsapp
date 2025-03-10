package com.resons.app.repositories;

import com.resons.app.UpdateRequest;
import com.resons.app.UpdateResponse;
import com.resons.app.model.request.LoginRequest;
import com.resons.app.model.request.response.LoginResponse;
import com.resons.app.network.ApiClient;
import com.resons.app.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateNameRepositories {
    private final ApiService apiService;


    public UpdateNameRepositories() {
        apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
    }

    public void postData(String token, UpdateRequest requestModel, final UpdateNameRepositories.RepositoryCallback<UpdateResponse> callback) {
        apiService.updateName(token, requestModel).enqueue(new Callback<UpdateResponse>() {
            @Override
            public void onResponse(Call<UpdateResponse> call, Response<UpdateResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<UpdateResponse> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public interface RepositoryCallback<T> {
        void onSuccess(T result);

        void onError(String message);
    }
}
