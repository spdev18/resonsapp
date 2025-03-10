package com.resons.app.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.resons.app.PrefManager;
import com.resons.app.StaticData;
import com.resons.app.databinding.ActivityLoginpageBinding;
import com.resons.app.model.request.LoginRequest;
import com.resons.app.R;
import com.resons.app.Translater;
import com.resons.app.viewmodel.LoginViewModel;


public class LoginActivity extends AppCompatActivity {
    private ActivityLoginpageBinding binding;
    private LoginActivity mContext;
    private LoginViewModel viewModel;

    private PrefManager prefManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_loginpage);
        mContext = this;
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        prefManager = new PrefManager(this);

        observeLiveData();

        // Check if the user is already logged in
        if (prefManager.getvalue(StaticData.islogin, false)) {
            goToDashboard();
            return;
        }

        // Set default credentials
        binding.emailEditText.setText(StaticData.DEFAULT_EMAIL);
        binding.passwordEditText.setText(StaticData.DEFAULT_PASSWORD);

        // Auto-login
        loginUser(StaticData.DEFAULT_EMAIL, StaticData.DEFAULT_PASSWORD);

        binding.loginButton.setOnClickListener(v -> {
            String email = binding.emailEditText.getText().toString().trim();
            String password = binding.passwordEditText.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                loginUser(email, password);
            }
        });

        binding.RegisterLink.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        });

        binding.forgetpass.setOnClickListener(v -> {
            startActivity(new Intent(mContext, MainActivity.class).putExtra("is_forgotPassword", 1));
        });
    }

    private void loginUser(String email, String password) {
        viewModel.postData(new LoginRequest(email, password));
    }


    private void observeLiveData() {
        viewModel.getResponseLiveData().observe(this, loginResponse -> {
            if (loginResponse.status() == 200) {
                Toast.makeText(LoginActivity.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();

                // Save login status
                prefManager.setvalue(StaticData.islogin, true);
                prefManager.setvalue(StaticData.token, loginResponse.getToken());

                goToDashboard();
            } else {
                Toast.makeText(LoginActivity.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getErrorLiveData().observe(this, s ->
                Toast.makeText(mContext, "Error: " + s, Toast.LENGTH_SHORT).show()
        );

        viewModel.getLoadingLiveData().observe(this, isLoading -> {
            if (isLoading) {
                binding.progressBar.setVisibility(View.VISIBLE);
            } else {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void goToDashboard() {
        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
        intent.putExtra("TOKEN", prefManager.getStringValue(StaticData.token));
        startActivity(intent);
        finishAffinity();
    }

}
