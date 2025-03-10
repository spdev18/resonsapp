package com.resons.app.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.resons.app.model.request.ForgotPasswordRequest;
import com.resons.app.model.request.SetPassRequest;
import com.resons.app.model.request.response.ForgotPasswordResponse;
import com.resons.app.model.request.response.SetPassResponse;
import com.resons.app.repositories.ForgotPasswordRepositories;
import com.resons.app.repositories.SetPasswordRepositories;

public class ForgotPasswordViewModel extends ViewModel {
    private final ForgotPasswordRepositories repository;
    private final MutableLiveData<ForgotPasswordResponse> responseLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    private final MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>();


    public ForgotPasswordViewModel() {
        repository = new ForgotPasswordRepositories();
    }

    public void postData(ForgotPasswordRequest requestModel) {
        loadingLiveData.setValue(true); // Show loading indicator
        repository.postData(requestModel, new ForgotPasswordRepositories.RepositoryCallback<ForgotPasswordResponse>() {
            @Override
            public void onSuccess(ForgotPasswordResponse result) {
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

    public LiveData<ForgotPasswordResponse> getResponseLiveData() {
        return responseLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public LiveData<Boolean> getLoadingLiveData() {
        return loadingLiveData;
    }
}
