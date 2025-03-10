package com.resons.app.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.resons.app.model.request.LoginRequest;
import com.resons.app.model.request.response.LoginResponse;
import com.resons.app.repositories.LoginRepositories;

public class LoginViewModel extends ViewModel {

    private final LoginRepositories repository;
    private final MutableLiveData<LoginResponse> responseLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    private final MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>();


    public LoginViewModel() {
        repository = new LoginRepositories();
    }

    public void postData(LoginRequest requestModel) {
        loadingLiveData.setValue(true); // Show loading indicator
        repository.postData(requestModel, new LoginRepositories.RepositoryCallback<LoginResponse>() {
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
