package com.resons.app;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.method.ScrollingMovementMethod;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.resons.app.R;

import java.util.ArrayList;
import java.util.Locale;

public class Translater extends AppCompatActivity {

    private static final int REQUEST_CODE_SPEECH_INPUT = 1;
    private EditText speechText;
    private ImageButton btnMic, btnClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translater);

        speechText = findViewById(R.id.speech_text);
        speechText.setMovementMethod(new ScrollingMovementMethod());
        btnMic = findViewById(R.id.btn_mic);
        btnClear = findViewById(R.id.btn_clear);

        // Microphone button listener
        btnMic.setOnClickListener(view -> startSpeechToText());

        // Clear button listener
        btnClear.setOnClickListener(view -> speechText.setText(""));
    }

    private void startSpeechToText() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now...");

        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        } catch (Exception e) {
            Toast.makeText(this, "Speech recognition is not supported on this device.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SPEECH_INPUT && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && !result.isEmpty()) {

                  speechText.setText(result.get(0));
            }
        }
    }
}
