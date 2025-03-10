package com.resons.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
//
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.resons.app.PrefManager;
import com.resons.app.StaticData;
import com.resons.app.databinding.ActivityMainBinding;
import com.resons.app.model.request.EmailValidationRequest;
import com.resons.app.R;
import com.resons.app.Translater;
import com.resons.app.model.request.ForgotPasswordRequest;
import com.resons.app.model.request.GmailLoginRequest;
import com.resons.app.viewmodel.EmailValidationViewModel;
import com.resons.app.viewmodel.ForgotPasswordViewModel;
import com.resons.app.viewmodel.GmailLoginViewModel;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;

    public ActivityMainBinding binding;
    public MainActivity mContext;

    private EmailValidationViewModel viewModel;

    private GmailLoginViewModel gmailLoginViewModel;
    private ForgotPasswordViewModel forgotPasswordViewModel;

    private int is_forgotPassword = 0;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mContext = this;
        viewModel = new ViewModelProvider(this).get(EmailValidationViewModel.class);
        gmailLoginViewModel = new ViewModelProvider(this).get(GmailLoginViewModel.class);
        forgotPasswordViewModel = new ViewModelProvider(this).get(ForgotPasswordViewModel.class);
        observeLiveData();

        binding.move.setOnClickListener(v -> {


            if (binding.emailEditText.getText().toString().trim().isEmpty()) {
                Toast.makeText(MainActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                if (is_forgotPassword == 1) {
                    forgotPasswordViewModel.postData(new ForgotPasswordRequest(binding.emailEditText.getText().toString().trim()));
                } else {
                    viewModel.postData(new EmailValidationRequest(binding.emailEditText.getText().toString().trim(), "PASSWORD", "", "email"));
                }

            }
        });

        binding.googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });


        binding.loginLink.setOnClickListener(v -> {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
        );
        firebaseAuth = FirebaseAuth.getInstance();

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        gsc = GoogleSignIn.getClient(this, gso);

        if (getIntent().getExtras() != null) {
            is_forgotPassword = getIntent().getIntExtra("is_forgotPassword", 0);
        }
    }


    void signIn() {
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent, 1000);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    firebaseAuth(account.getIdToken());
                }
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void firebaseAuth(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            if (user != null) {
                                //  saveUserToDatabase(user);
                                gmailLoginViewModel.postData(new GmailLoginRequest(
                                        user.getEmail()
                                ));
                            } else {
                                Toast.makeText(MainActivity.this, "Firebase Authentication Failed", Toast.LENGTH_SHORT).show();
                            }
                        }


                    }
                });
    }

    private void saveUserToDatabase(FirebaseUser user) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", user.getUid());
        map.put("name", user.getDisplayName());
        map.put("profile", user.getPhotoUrl() != null ? user.getPhotoUrl().toString() : null);

        firebaseDatabase.getReference().child("users").child(user.getUid()).setValue(map);
    }

    void navigateToTranslaterActivity() {
        finish();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void observeLiveData() {
        viewModel.getResponseLiveData().observe(this, emailValidationResponse -> {
            if (emailValidationResponse.status() == 201) {
                Toast.makeText(MainActivity.this, emailValidationResponse.getMessage(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MainActivity.this, otpvarification.class);
                intent.putExtra("email", binding.emailEditText.getText().toString().trim());
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, emailValidationResponse.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        gmailLoginViewModel.getResponseLiveData().observe(this, emailValidationResponse -> {
            if (emailValidationResponse.status() == 201) {
                Toast.makeText(MainActivity.this, emailValidationResponse.getMessage(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MainActivity.this, otpvarification.class);
                intent.putExtra("email", binding.emailEditText.getText().toString().trim());
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, emailValidationResponse.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        gmailLoginViewModel.getErrorLiveData().observe(this, error -> Toast.makeText(mContext, "Error: " + error, Toast.LENGTH_SHORT).show());

        gmailLoginViewModel.getLoadingLiveData().observe(this, isLoading -> {
            if (isLoading) {
                binding.progressBar.setVisibility(View.VISIBLE);
            } else {
                binding.progressBar.setVisibility(View.GONE);
            }
        });

        forgotPasswordViewModel.getResponseLiveData().observe(this, response -> {
            if (response.getStatus() == 201) {
                Toast.makeText(MainActivity.this, response.getMessage(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MainActivity.this, otpvarification.class);
                intent.putExtra("email", binding.emailEditText.getText().toString().trim());
                intent.putExtra("is_forgotPassword", is_forgotPassword);
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, response.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        forgotPasswordViewModel.getErrorLiveData().observe(this, error -> Toast.makeText(mContext, "Error: " + error, Toast.LENGTH_SHORT).show());

        forgotPasswordViewModel.getLoadingLiveData().observe(this, isLoading -> {
            if (isLoading) {
                binding.progressBar.setVisibility(View.VISIBLE);
            } else {
                binding.progressBar.setVisibility(View.GONE);
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
