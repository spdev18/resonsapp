package com.resons.app.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.resons.app.UpdateRequest;
import com.resons.app.UpdateResponse;
import com.resons.app.repositories.UpdateNameRepositories;

public class UpdateNameViewModel extends ViewModel
{
    private final UpdateNameRepositories repository;
    private final MutableLiveData<UpdateResponse> responseLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    private final MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>();


    public UpdateNameViewModel() {
        repository = new UpdateNameRepositories();
    }

    public void postData(String token,UpdateRequest requestModel) {
        loadingLiveData.setValue(true); // Show loading indicator
        repository.postData(token,requestModel, new UpdateNameRepositories.RepositoryCallback<UpdateResponse>() {
            @Override
            public void onSuccess(UpdateResponse result) {
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

    public LiveData<UpdateResponse> getResponseLiveData() {
        return responseLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public LiveData<Boolean> getLoadingLiveData() {
        return loadingLiveData;
    }
}
