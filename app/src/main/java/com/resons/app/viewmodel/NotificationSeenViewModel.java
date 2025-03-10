package com.resons.app.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonArray;
import com.resons.app.model.request.NotificationSeenRequest;
import com.resons.app.model.request.response.NotificationsSeenRespone;
import com.resons.app.repositories.NotificationSeenRepositories;
import com.resons.app.repositories.NotificationsRepositories;

public class NotificationSeenViewModel extends ViewModel {

    private final NotificationSeenRepositories repository;
    private final MutableLiveData<NotificationsSeenRespone> responseLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    private final MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>();


    public NotificationSeenViewModel() {
        repository = new NotificationSeenRepositories();
    }

    public void postData(String token, NotificationSeenRequest request) {
        loadingLiveData.setValue(true); // Show loading indicator
        repository.postData(token, request, new NotificationSeenRepositories.RepositoryCallback<NotificationsSeenRespone>() {
            @Override
            public void onSuccess(NotificationsSeenRespone result) {
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

    public LiveData<NotificationsSeenRespone> getResponseLiveData() {
        return responseLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public LiveData<Boolean> getLoadingLiveData() {
        return loadingLiveData;
    }
}
