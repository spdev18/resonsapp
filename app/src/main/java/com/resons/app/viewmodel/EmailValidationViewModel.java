package com.resons.app.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.resons.app.model.request.EmailValidationRequest;
import com.resons.app.model.request.response.EmailValidationResponse;
import com.resons.app.repositories.EmailValidationRepositories;

public class EmailValidationViewModel extends ViewModel
{
    private final EmailValidationRepositories repository;
    private final MutableLiveData<EmailValidationResponse> responseLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    private final MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>();


    public EmailValidationViewModel() {
        repository = new EmailValidationRepositories();
    }

    public void postData(EmailValidationRequest requestModel) {
        loadingLiveData.setValue(true); // Show loading indicator
        repository.postData(requestModel, new EmailValidationRepositories.RepositoryCallback<EmailValidationResponse>() {
            @Override
            public void onSuccess(EmailValidationResponse result) {
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

    public LiveData<EmailValidationResponse> getResponseLiveData() {
        return responseLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public LiveData<Boolean> getLoadingLiveData() {
        return loadingLiveData;
    }
}
