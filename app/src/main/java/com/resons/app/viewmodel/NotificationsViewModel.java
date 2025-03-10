package com.resons.app.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonArray;
import com.resons.app.model.request.SetPassRequest;
import com.resons.app.model.request.response.NotificationsModel;
import com.resons.app.model.request.response.SetPassResponse;
import com.resons.app.repositories.NotificationsRepositories;
import com.resons.app.repositories.SetPasswordRepositories;

public class NotificationsViewModel extends ViewModel {

    private final NotificationsRepositories repository;
    private final MutableLiveData<JsonArray> responseLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    private final MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>();


    public NotificationsViewModel() {
        repository = new NotificationsRepositories();
    }

    public void postData(String token) {
        loadingLiveData.setValue(true); // Show loading indicator
        repository.postData(token, new NotificationsRepositories.RepositoryCallback<JsonArray>() {
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
