package com.resons.app.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.resons.app.UpdateResponse;
import com.resons.app.model.request.SetPassRequest;
import com.resons.app.model.request.response.SetPassResponse;
import com.resons.app.repositories.ProfilePictureRepositories;
import com.resons.app.repositories.SetPasswordRepositories;

import okhttp3.MultipartBody;

public class ProfilePictureViewModel extends ViewModel
{

    private final ProfilePictureRepositories repository;
    private final MutableLiveData<UpdateResponse> responseLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    private final MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>();


    public ProfilePictureViewModel() {
        repository = new ProfilePictureRepositories();
    }

    public void postData(String token, MultipartBody.Part image) {
        loadingLiveData.setValue(true); // Show loading indicator
        repository.postData(token,image, new ProfilePictureRepositories.RepositoryCallback<UpdateResponse>() {
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
