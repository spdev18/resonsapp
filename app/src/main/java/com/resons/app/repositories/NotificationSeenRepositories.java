package com.resons.app.repositories;

import com.google.gson.JsonArray;
import com.resons.app.model.request.NotificationSeenRequest;
import com.resons.app.model.request.response.NotificationsSeenRespone;
import com.resons.app.network.ApiClient;
import com.resons.app.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationSeenRepositories
{
    private final ApiService apiService;


    public NotificationSeenRepositories() {
        apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
    }

    public void postData(String token, NotificationSeenRequest request, final NotificationSeenRepositories.RepositoryCallback<NotificationsSeenRespone> callback) {
        apiService.addSeen(token,request).enqueue(new Callback<NotificationsSeenRespone>() {
            @Override
            public void onResponse(Call<NotificationsSeenRespone> call, Response<NotificationsSeenRespone> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<NotificationsSeenRespone> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public interface RepositoryCallback<T> {
        void onSuccess(T result);

        void onError(String message);
    }

}
