package com.resons.app.activity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.resons.app.R;
import com.resons.app.Translater;
import com.resons.app.databinding.ActivitySetpasswordBinding;
import com.resons.app.model.request.SetPassRequest;
import com.resons.app.model.request.response.SetPassResponse;
import com.resons.app.UserDefaultsManager;
import com.resons.app.network.ApiService;
import com.resons.app.viewmodel.SetPasswordViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Setpassword extends AppCompatActivity {

    public ActivitySetpasswordBinding binding;
    private Setpassword mContext;
    private SetPasswordViewModel viewModel;
    private String token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setpassword);
        mContext = this;

        viewModel = new ViewModelProvider(this).get(SetPasswordViewModel.class);


        if (getIntent().getExtras() != null) {
            token = getIntent().getStringExtra("TOKEN");
        }

        binding.startNowButton.setOnClickListener(v -> {

            if (isValidate()) {
                viewModel.postData("Bearer " + token, new SetPassRequest(
                        binding.passwordEditText.getText().toString().trim()
                ));
            }

        });

        binding.loginLink.setOnClickListener(v -> {
            Intent intent = new Intent(Setpassword.this, LoginActivity.class);
            startActivity(intent);
            finishAffinity();
        });

        observeLiveData();
    }

    private void observeLiveData() {
        viewModel.getResponseLiveData().observe(this, setPassResponse -> {
            if (setPassResponse.getStatus() == 200) {
                Toast.makeText(mContext, setPassResponse.getMessage(), Toast.LENGTH_SHORT).show();

                // Proceed to the next activity
                Intent intent = new Intent(Setpassword.this, LoginActivity.class);
                intent.putExtra("passward", binding.passwordEditText.getText().toString().trim());
                startActivity(intent);
                finishAffinity();
            } else {
                Toast.makeText(mContext, setPassResponse.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });

        viewModel.getErrorLiveData().observe(this, error -> Toast.makeText(mContext, "Error: " + error, Toast.LENGTH_SHORT).show());

        viewModel.getLoadingLiveData().observe(this, isLoading -> {
            if (isLoading) {
                binding.progressBar.setVisibility(View.VISIBLE);
            } else {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }

    private boolean isValidate() {
        boolean isValid = true;
        if (binding.passwordEditText.getText().toString().trim().isEmpty()) {
            Toast.makeText(mContext, "Please enter password", Toast.LENGTH_SHORT).show();
            isValid = false;
        } else if (binding.confirmPasswordEditText.getText().toString().trim().isEmpty()) {
            Toast.makeText(mContext, "Please enter confirm Password", Toast.LENGTH_SHORT).show();
            isValid = false;
        } else if (!binding.passwordEditText.getText().toString().trim().equals(binding.confirmPasswordEditText.getText().toString().trim())) {
            Toast.makeText(mContext, "Password and Confirm Password does not same", Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        return isValid;
    }
}
