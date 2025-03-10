package com.resons.app.viewmodel;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.resons.app.PrefManager;
import com.resons.app.model.request.SetPassRequest;
import com.resons.app.model.request.TranscriptionRequest;
import com.resons.app.model.request.response.SetPassResponse;
import com.resons.app.model.request.response.TranslatorResponse;
import com.resons.app.repositories.SetPasswordRepositories;
import com.resons.app.repositories.TranslatorRepositories;

import java.util.Arrays;

/*public class TranslatorViewModel extends ViewModel {
    private final TranslatorRepositories repository;
    private final MutableLiveData<TranslatorResponse> responseLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    private final MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>();


    public TranslatorViewModel() {
        repository = new TranslatorRepositories();
    }

    public void postData(String token, TranscriptionRequest requestModel) {
        loadingLiveData.setValue(true); // Show loading indicator
        repository.postData(token,requestModel, new TranslatorRepositories.RepositoryCallback<TranslatorResponse>() {
            @Override
            public void onSuccess(TranslatorResponse result) {
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

    public LiveData<TranslatorResponse> getResponseLiveData() {
        return responseLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public LiveData<Boolean> getLoadingLiveData() {
        return loadingLiveData;
    }
}*/

public class TranslatorViewModel extends ViewModel {
    private final TranslatorRepositories repository;
    private final MutableLiveData<TranslatorResponse> responseLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> chunkProcessingLiveData = new MutableLiveData<>();

    private int wordLimitPerUpload = 10;  // Set your chunk size (word limit)

    public TranslatorViewModel() {
        repository = new TranslatorRepositories();
    }

    public void postData(String token, TranscriptionRequest requestModel) {
        loadingLiveData.setValue(true); // Show loading indicator
        repository.postData(token, requestModel, new TranslatorRepositories.RepositoryCallback<TranslatorResponse>() {
            @Override
            public void onSuccess(TranslatorResponse result) {
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

    public LiveData<TranslatorResponse> getResponseLiveData() {
        return responseLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public LiveData<Boolean> getLoadingLiveData() {
        return loadingLiveData;
    }

    public LiveData<Boolean> getChunkProcessingLiveData() {
        return chunkProcessingLiveData;
    }

    public void checkChunkCountAndUpload(Context context, String token, String transcript) {
        chunkProcessingLiveData.setValue(true);  // Show loading for chunk processing
        processChunks(token, transcript, 0, Integer.parseInt(new PrefManager(context).getvalue("uploadLimit")), transcript.split(" ").length);
    }

    private void processChunks(String token, String transcript, int startIndex, int wordLimitPerUpload, int totalWords) {
        if (startIndex >= totalWords) {
            chunkProcessingLiveData.setValue(false);  // Hide chunk processing when done
            return;
        }

        int endIndex = Math.min(startIndex + wordLimitPerUpload, totalWords);  // Calculate endIndex based on word limit

        // Create a chunk by extracting the words from startIndex to endIndex
        String[] words = transcript.split(" ");
        String chunk = String.join(" ", Arrays.copyOfRange(words, startIndex, endIndex));

        // Log the chunk being uploaded
        Log.e("ChunkToUpload", "Uploading chunk: " + chunk);

        final int finalStartIndex = startIndex;  // Make this variable effectively final

        repository.postData(token, new TranscriptionRequest(chunk), new TranslatorRepositories.RepositoryCallback<TranslatorResponse>() {
            @Override
            public void onSuccess(TranslatorResponse result) {
                Log.e("ChunkUploadSuccess", "Successfully uploaded chunk: " + chunk);

                int nextStartIndex = finalStartIndex + wordLimitPerUpload;  // Move to the next chunk
                processChunks(token, transcript, nextStartIndex, wordLimitPerUpload, totalWords);  // Proceed to the next chunk
                responseLiveData.postValue(result);
            }

            @Override
            public void onError(String message) {
                Log.e("ChunkUploadError", "Error uploading chunk: " + chunk + ", Error: " + message);
                chunkProcessingLiveData.setValue(false);
                errorLiveData.postValue(message);
            }
        });
    }


}
