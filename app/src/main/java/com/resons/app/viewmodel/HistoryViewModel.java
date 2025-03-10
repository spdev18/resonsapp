package com.resons.app.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonArray;
import com.resons.app.repositories.HistoryRepositories;
import com.resons.app.repositories.NotificationsRepositories;

public class HistoryViewModel extends ViewModel
{
    private final HistoryRepositories repository;
    private final MutableLiveData<JsonArray> responseLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    private final MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>();


    public HistoryViewModel() {
        repository = new HistoryRepositories();
    }

    public void postData(String token) {
        loadingLiveData.setValue(true); // Show loading indicator
        repository.postData(token, new HistoryRepositories.RepositoryCallback<JsonArray>() {
            @Override
            public void onSuccess(JsonArray result) {
                loadingLiveData.postValue(false); // Hide loading indicator
                responseLiveData.postValue(result);
            }

            @Override
            public void onError(String message) {
                loadingLiveData.postValue(false); // Hide loading indicator
                errorLiveData.postValue(message);
            }
        });
    }

    public LiveData<JsonArray> getResponseLiveData() {
        return responseLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public LiveData<Boolean> getLoadingLiveData() {
        return loadingLiveData;
    }

}
