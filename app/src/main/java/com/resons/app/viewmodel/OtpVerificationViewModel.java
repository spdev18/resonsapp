package com.resons.app.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.resons.app.model.request.LoginRequest;
import com.resons.app.model.request.OtpVerificationRequest;
import com.resons.app.model.request.response.LoginResponse;
import com.resons.app.model.request.response.OtpVerificationResponse;
import com.resons.app.repositories.LoginRepositories;
import com.resons.app.repositories.OtpVerificationRepositories;

public class OtpVerificationViewModel extends ViewModel
{
    private final OtpVerificationRepositories repository;
    private final MutableLiveData<OtpVerificationResponse> responseLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    private final MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>();


    public OtpVerificationViewModel() {
        repository = new OtpVerificationRepositories();
    }

    public void postData(OtpVerificationRequest requestModel) {
        loadingLiveData.setValue(true); // Show loading indicator
        repository.postData(requestModel, new OtpVerificationRepositories.RepositoryCallback<OtpVerificationResponse>() {
            @Override
            public void onSuccess(OtpVerificationResponse result) {
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

    public LiveData<OtpVerificationResponse> getResponseLiveData() {
        return responseLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public LiveData<Boolean> getLoadingLiveData() {
        return loadingLiveData;
    }
}
