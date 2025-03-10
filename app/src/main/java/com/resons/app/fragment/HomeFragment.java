package com.resons.app.fragment;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.resons.app.PermissionUtils;
import com.resons.app.PrefManager;
import com.resons.app.R;
import com.resons.app.databinding.FragmentHomeBinding;
import com.resons.app.viewmodel.TranslatorViewModel;

import java.util.ArrayList;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private static final int REQUEST_CODE_SPEECH_INPUT = 1;
    private TranslatorViewModel viewModel;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private String fullSpeech = "";  // To store and accumulate previous speech

    private boolean isRecording = false;
    private String transcript = "";

    private SpeechRecognizer speechRecognizer;
    private Intent speechRecognizerIntent;

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private boolean permissionToRecordAccepted = false;
    private String[] permissions = {android.Manifest.permission.RECORD_AUDIO};

    private AudioManager audioManager;
    private int originalVolume;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        binding.speechText.setMovementMethod(new ScrollingMovementMethod());


        viewModel = new ViewModelProvider(this).get(TranslatorViewModel.class);

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        ActivityCompat.requestPermissions(requireActivity(), permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        audioManager = (AudioManager) requireContext().getSystemService(Context.AUDIO_SERVICE);

        // Check and request permissions
        if (!PermissionUtils.checkPermission(requireContext(), android.Manifest.permission.RECORD_AUDIO)) {
            PermissionUtils.requestPermissionAlert(requireContext(), android.Manifest.permission.RECORD_AUDIO, PermissionUtils.REQUEST_RECORD_AUDIO_PERMISSION);
        }

        NotificationManager notificationManager = (NotificationManager) requireContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !notificationManager.isNotificationPolicyAccessGranted()) {
            PermissionUtils.requestDNDPermissionAlert(requireContext(), PermissionUtils.REQUEST_DND_PERMISSION);
        }


        binding.btnMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRecording) {
                    stopRecording();
                    binding.btnMic.setImageResource(R.drawable.baseline_mic_24);
                } else {
                    if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                        // Permission is granted; start recording
                        startRecording();
                        binding.btnMic.setImageResource(R.drawable.baseline_pause_24);
                    } else {
                        // Permission is not granted; request the permission
                        ActivityCompat.requestPermissions(requireActivity(), permissions, REQUEST_RECORD_AUDIO_PERMISSION);
                    }
                }
            }
        });

        binding.btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transcript = "";
                isRecording = false;
                binding.speechText.setText("");
                binding.btnMic.setImageResource(R.drawable.baseline_mic_24);
            }
        });

        binding.btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.speechText.getText().toString().trim().isEmpty()) {
                    Toast.makeText(requireContext(), "Please start speech", Toast.LENGTH_SHORT).show();
                    return;
                }

                String token = "Bearer " + new PrefManager(requireContext()).getvalue("token");
                String transcript = binding.speechText.getText().toString().trim();
                if (!transcript.isEmpty()) {
                    viewModel.checkChunkCountAndUpload(requireActivity(), token, binding.speechText.getText().toString().trim());
                } else {
                    Toast.makeText(requireContext(), "Please start speech", Toast.LENGTH_SHORT).show();
                }
            }
        });

        observeLiveData();

        mFirebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        String uploadWordLimit = mFirebaseRemoteConfig.getString("upload_word_limit");
                        Log.e("uploadLimitValue", "=" + uploadWordLimit);
                        new PrefManager(requireContext()).setvalue("uploadLimit", uploadWordLimit);
                    }
                });

        return binding.getRoot();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            permissionToRecordAccepted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
        }

        if (!permissionToRecordAccepted) {
            Toast.makeText(requireContext(), "Permission to record audio is required!", Toast.LENGTH_LONG).show();
            requireActivity().finish();
        }
    }

    public void startRecording() {
        isRecording = true;

        audioManager.setStreamMute(AudioManager.STREAM_NOTIFICATION, true);
        audioManager.setStreamMute(AudioManager.STREAM_ALARM, true);
        audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
        audioManager.setStreamMute(AudioManager.STREAM_RING, true);
        audioManager.setStreamMute(AudioManager.STREAM_SYSTEM, true);

        // Initialize SpeechRecognizer
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(requireContext());
        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE, true);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 0);

        // Set up the RecognitionListener
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {
                Log.d("SpeechRecognition", "onReadyForSpeech: ");
            }

            @Override
            public void onBeginningOfSpeech() {
                Log.d("SpeechRecognition", "onBeginningOfSpeech: ");
            }

            @Override
            public void onRmsChanged(float rmsdB) {
            }

            @Override
            public void onBufferReceived(byte[] buffer) {
                Log.d("SpeechRecognition", "onBufferReceived: ");
            }

            @Override
            public void onEndOfSpeech() {
                Log.d("SpeechRecognition", "onEndOfSpeech: ");
                if (fullSpeech.length() > 0) {
                    binding.speechText.setMovementMethod(new ScrollingMovementMethod());
                    binding.speechText.setText(fullSpeech);

                    Log.d("SpeechRecognition", "Final Speech: " + fullSpeech);
                }
            }

            @Override
            public void onError(int error) {
                switch (error) {
                    case SpeechRecognizer.ERROR_NO_MATCH:
                    case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                    case SpeechRecognizer.ERROR_NETWORK:
                        if (isRecording) {
                            speechRecognizer.startListening(speechRecognizerIntent);
                        }
                        break;
                    case SpeechRecognizer.ERROR_CLIENT:
                    case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                        Log.e("SpeechRecognizer", "Recognizer busy or client-side error");
                        break;
                    default:
                        Log.e("SpeechRecognizer", "Error code: " + error);
                        break;
                }
            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null && !matches.isEmpty()) {
                    String recognizedText = matches.get(0);
                    recognizedText = formatSentence(recognizedText);

                    transcript = transcript + "\n" + recognizedText;

                    requireActivity().runOnUiThread(() -> {
                        binding.speechText.setMovementMethod(new ScrollingMovementMethod());
                        binding.speechText.setText(transcript);
                        autoScrollToBottom(); // Auto-scroll to the latest speech
                        Log.d("SpeechRecognition", "Final Result: " + transcript);
                    });
                }
                if (isRecording) {
                    speechRecognizer.startListening(speechRecognizerIntent);
                }
            }

            @Override
            public void onPartialResults(Bundle results) {
                ArrayList<String> partialResults = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (partialResults != null && !partialResults.isEmpty()) {
                    String recognizedText = partialResults.get(0);
                    recognizedText = formatSentence(recognizedText);

                    StringBuilder fullSpeechBuilder = new StringBuilder(transcript + ".");
                    fullSpeechBuilder.append("\n").append(recognizedText);
                    String fullSpeech = fullSpeechBuilder.toString();

                    requireActivity().runOnUiThread(() -> {
                        binding.speechText.setMovementMethod(new ScrollingMovementMethod());
                        binding.speechText.setText(fullSpeech);
                        autoScrollToBottom(); // Auto-scroll on partial results
                    });
                }
            }

            @Override
            public void onEvent(int eventType, Bundle params) {
                Log.d("SpeechRecognition", "onEvent eventType: " + eventType);
            }
        });

        // Start listening
        speechRecognizer.startListening(speechRecognizerIntent);
    }


    public void stopRecording() {
        isRecording = false;

        if (speechRecognizer != null) {
            speechRecognizer.stopListening();
            speechRecognizer.destroy();
            speechRecognizer = null;
        }

        audioManager.setStreamMute(AudioManager.STREAM_NOTIFICATION, false);
        audioManager.setStreamMute(AudioManager.STREAM_ALARM, false);
        audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
        audioManager.setStreamMute(AudioManager.STREAM_RING, false);
        audioManager.setStreamMute(AudioManager.STREAM_SYSTEM, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRecording();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopRecording();
    }

    public void observeLiveData() {
        viewModel.getResponseLiveData().observe(requireActivity(), response -> {
            if (response.getStatus() == 200) {
                Toast.makeText(requireContext(), response.getMessage(), Toast.LENGTH_SHORT).show();
                binding.speechText.setText("");
                transcript = "";
            } else {
                Toast.makeText(requireContext(), response.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getErrorLiveData().observe(requireActivity(), error ->
                Toast.makeText(requireContext(), "Error: " + error, Toast.LENGTH_SHORT).show()
        );

        viewModel.getLoadingLiveData().observe(requireActivity(), isLoading -> {
            if (isLoading) {
                binding.progressBar.setVisibility(View.VISIBLE);
            } else {
                binding.progressBar.setVisibility(View.GONE);
            }
        });

        viewModel.getChunkProcessingLiveData().observe(requireActivity(), isProcessing -> {
            if (isProcessing) {
                Toast.makeText(requireContext(), "Processing chunks...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to add a question mark if the sentence starts with specific words
    private String formatSentence(String sentence) {
        if (sentence == null || sentence.trim().isEmpty()) {
            return sentence;
        }

        sentence = sentence.trim(); // Trim whitespace

        // Define question words
        String[] questionWords = {"what", "why", "how", "when", "where", "is", "does", "do", "can", "could", "should", "would", "are", "will", "was", "were"};

        for (String word : questionWords) {
            if (sentence.toLowerCase().startsWith(word)) {
                // If it starts with a question word but doesn't end with '?', add it
                if (!sentence.endsWith("?")) {
                    return sentence + "?";
                }
                return sentence;
            }
        }

        // If it doesn't end with a punctuation, add a full stop
        if (!sentence.endsWith(".") && !sentence.endsWith("?")) {
            return sentence + ".";
        }

        return sentence;
    }

    // Auto-scroll method to scroll to bottom
    private void autoScrollToBottom() {
        binding.nestedScrollView.post(() ->
                binding.nestedScrollView.fullScroll(View.FOCUS_DOWN)
        );
    }
}