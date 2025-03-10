package com.resons.app.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.resons.app.model.request.LoginRequest;
import com.resons.app.model.request.response.LoginResponse;
import com.resons.app.model.request.response.UserProfile;
import com.resons.app.repositories.LoginRepositories;
import com.resons.app.repositories.UserProfileRepositories;

public class UserProfileViewModel extends ViewModel
{
    private final UserProfileRepositories repository;
    private final MutableLiveData<UserProfile> responseLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    private final MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>();


    public UserProfileViewModel() {
        repository = new UserProfileRepositories();
    }

    public void postData(String token) {
        loadingLiveData.setValue(true); // Show loading indicator
        repository.postData(token, new UserProfileRepositories.RepositoryCallback<UserProfile>() {
            @Override
            public void onSuccess(UserProfile result) {
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

    public LiveData<UserProfile> getResponseLiveData() {
        return responseLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public LiveData<Boolean> getLoadingLiveData() {
        return loadingLiveData;
    }
}
