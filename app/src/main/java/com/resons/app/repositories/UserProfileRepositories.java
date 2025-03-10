package com.resons.app.repositories;

import com.resons.app.UpdateRequest;
import com.resons.app.UpdateResponse;
import com.resons.app.model.request.response.UserProfile;
import com.resons.app.network.ApiClient;
import com.resons.app.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileRepositories {

    private final ApiService apiService;


    public UserProfileRepositories() {
        apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
    }

    public void postData(String token, final UserProfileRepositories.RepositoryCallback<UserProfile> callback) {
        apiService.getUserProfile(token).enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public interface RepositoryCallback<T> {
        void onSuccess(T result);

        void onError(String message);
    }
}
