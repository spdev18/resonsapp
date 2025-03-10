package com.resons.app.viewmodel;

import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.resons.app.model.request.GmailLoginRequest;
import com.resons.app.model.request.LoginRequest;
import com.resons.app.model.request.response.LoginResponse;
import com.resons.app.repositories.GmailLoginRepositories;
import com.resons.app.repositories.LoginRepositories;

public class GmailLoginViewModel extends ViewModel
{

    private final GmailLoginRepositories repository;
    private final MutableLiveData<LoginResponse> responseLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    private final MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>();


    public GmailLoginViewModel() {
        repository = new GmailLoginRepositories();
    }

    public void postData(GmailLoginRequest requestModel) {
        loadingLiveData.setValue(true); // Show loading indicator
        repository.postData(requestModel, new GmailLoginRepositories.RepositoryCallback<LoginResponse>() {
            @Override
            public void onSuccess(LoginResponse result) {
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

    public LiveData<LoginResponse> getResponseLiveData() {
        return responseLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public LiveData<Boolean> getLoadingLiveData() {
        return loadingLiveData;
    }
}
