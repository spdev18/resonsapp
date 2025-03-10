package com.resons.app.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.resons.app.model.request.ForgotPasswordOtpRequest;
import com.resons.app.model.request.ForgotPasswordRequest;
import com.resons.app.model.request.response.ForgotOtpResponse;
import com.resons.app.model.request.response.ForgotPasswordResponse;
import com.resons.app.repositories.ForgotOtpRepositories;
import com.resons.app.repositories.ForgotPasswordRepositories;

public class ForgotOtpViewModel extends ViewModel {

    private final ForgotOtpRepositories repository;
    private final MutableLiveData<ForgotOtpResponse> responseLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    private final MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>();


    public ForgotOtpViewModel() {
        repository = new ForgotOtpRepositories();
    }

    public void postData(ForgotPasswordOtpRequest requestModel) {
        loadingLiveData.setValue(true); // Show loading indicator
        repository.postData(requestModel, new ForgotOtpRepositories.RepositoryCallback<ForgotOtpResponse>() {
            @Override
            public void onSuccess(ForgotOtpResponse result) {
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

    public LiveData<ForgotOtpResponse> getResponseLiveData() {
        return responseLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public LiveData<Boolean> getLoadingLiveData() {
        return loadingLiveData;
    }
}
