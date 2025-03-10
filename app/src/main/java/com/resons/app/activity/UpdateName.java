package com.resons.app.activity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.resons.app.R;
import com.resons.app.UpdateRequest;
import com.resons.app.UpdateResponse;
import com.resons.app.UserDefaultsManager;
import com.resons.app.databinding.ActivityUpdateNameBinding;
import com.resons.app.network.ApiService;
import com.resons.app.viewmodel.UpdateNameViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UpdateName extends AppCompatActivity {

    private ActivityUpdateNameBinding binding;
    private UpdateName mContext;
    private String token;
    private ApiService apiService;

    private UpdateNameViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_name);
        mContext = this;

        viewModel = new ViewModelProvider(this).get(UpdateNameViewModel.class);


        if (getIntent().getExtras() != null) {
            token = getIntent().getStringExtra("TOKEN");
        }

        binding.next.setOnClickListener(v -> {
            if (binding.editName.getText().toString().trim().isEmpty()) {
                Toast.makeText(UpdateName.this, "Please enter your name", Toast.LENGTH_SHORT).show();
            } else {

                viewModel.postData("Bearer " + token, new UpdateRequest(binding.editName.getText().toString().trim()));


            }
        });

        observeLiveData();
    }

    private void observeLiveData() {
        viewModel.getResponseLiveData().observe(this, setPassResponse -> {
            if (setPassResponse.getStatus() == 200) {
                Toast.makeText(mContext, setPassResponse.getMessage(), Toast.LENGTH_SHORT).show();

                // Proceed to the next activity
                Intent intent = new Intent(UpdateName.this, Setpassword.class);
                intent.putExtra("TOKEN", token);
                startActivity(intent);
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

}
