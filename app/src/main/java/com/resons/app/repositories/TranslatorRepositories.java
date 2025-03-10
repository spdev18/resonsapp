package com.resons.app.repositories;

import com.resons.app.model.request.SetPassRequest;
import com.resons.app.model.request.TranscriptionRequest;
import com.resons.app.model.request.response.SetPassResponse;
import com.resons.app.model.request.response.TranslatorResponse;
import com.resons.app.network.ApiClient;
import com.resons.app.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TranslatorRepositories
{
    private final ApiService apiService;


    public TranslatorRepositories() {
        apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
    }

    public void postData(String token, TranscriptionRequest requestModel, final TranslatorRepositories.RepositoryCallback<TranslatorResponse> callback) {
        apiService.addTranslator(token,requestModel).enqueue(new Callback<TranslatorResponse>() {
            @Override
            public void onResponse(Call<TranslatorResponse> call, Response<TranslatorResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<TranslatorResponse> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public interface RepositoryCallback<T> {
        void onSuccess(T result);

        void onError(String message);
    }
}
