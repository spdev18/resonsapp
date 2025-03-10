package com.resons.app.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.resons.app.model.request.LoginRequest;
import com.resons.app.model.request.SetPassRequest;
import com.resons.app.model.request.response.LoginResponse;
import com.resons.app.model.request.response.SetPassResponse;
import com.resons.app.repositories.LoginRepositories;
import com.resons.app.repositories.SetPasswordRepositories;

public class SetPasswordViewModel extends ViewModel
{
    private final SetPasswordRepositories repository;
    private final MutableLiveData<SetPassResponse> responseLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    private final MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>();


    public SetPasswordViewModel() {
        repository = new SetPasswordRepositories();
    }

    public void postData(String token,SetPassRequest requestModel) {
        loadingLiveData.setValue(true); // Show loading indicator
        repository.postData(token,requestModel, new SetPasswordRepositories.RepositoryCallback<SetPassResponse>() {
            @Override
            public void onSuccess(SetPassResponse result) {
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

    public LiveData<SetPassResponse> getResponseLiveData() {
        return responseLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public LiveData<Boolean> getLoadingLiveData() {
        return loadingLiveData;
    }
}
