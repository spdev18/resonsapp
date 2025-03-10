package com.resons.app.repositories;

import com.google.gson.JsonArray;
import com.resons.app.model.request.SetPassRequest;
import com.resons.app.model.request.response.NotificationsModel;
import com.resons.app.model.request.response.SetPassResponse;
import com.resons.app.network.ApiClient;
import com.resons.app.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsRepositories
{
    private final ApiService apiService;


    public NotificationsRepositories() {
        apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
    }

    public void postData(String token, final NotificationsRepositories.RepositoryCallback<JsonArray> callback) {
        apiService.getNotifications(token).enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public interface RepositoryCallback<T> {
        void onSuccess(T result);

        void onError(String message);
    }
}
