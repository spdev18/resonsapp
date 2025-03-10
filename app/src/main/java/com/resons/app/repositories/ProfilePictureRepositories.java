package com.resons.app.repositories;

import com.resons.app.UpdateResponse;
import com.resons.app.model.request.SetPassRequest;
import com.resons.app.model.request.response.SetPassResponse;
import com.resons.app.network.ApiClient;
import com.resons.app.network.ApiService;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;

public class ProfilePictureRepositories
{
    private final ApiService apiService;


    public ProfilePictureRepositories() {
        apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
    }

    public void postData(String token, MultipartBody.Part profile, final ProfilePictureRepositories.RepositoryCallback<UpdateResponse> callback) {
        apiService.updateProfilePicture(token,profile).enqueue(new Callback<UpdateResponse>() {
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
