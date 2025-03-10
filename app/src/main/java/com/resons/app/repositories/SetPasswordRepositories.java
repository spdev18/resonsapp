package com.resons.app.repositories;

import com.resons.app.model.request.LoginRequest;
import com.resons.app.model.request.SetPassRequest;
import com.resons.app.model.request.response.LoginResponse;
import com.resons.app.model.request.response.SetPassResponse;
import com.resons.app.network.ApiClient;
import com.resons.app.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetPasswordRepositories
{
    private final ApiService apiService;


    public SetPasswordRepositories() {
        apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
    }

    public void postData(String token,SetPassRequest requestModel, final SetPasswordRepositories.RepositoryCallback<SetPassResponse> callback) {
        apiService.updatepass(token,requestModel).enqueue(new Callback<SetPassResponse>() {
            @Override
            public void onResponse(Call<SetPassResponse> call, Response<SetPassResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<SetPassResponse> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public interface RepositoryCallback<T> {
        void onSuccess(T result);

        void onError(String message);
    }
}
