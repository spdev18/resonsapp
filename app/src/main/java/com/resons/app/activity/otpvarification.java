package com.resons.app.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.resons.app.databinding.ActivityOtpvarificationBinding;
import com.resons.app.model.request.EmailValidationRequest;
import com.resons.app.model.request.ForgotPasswordOtpRequest;
import com.resons.app.model.request.OtpVerificationRequest;
import com.resons.app.R;
import com.resons.app.viewmodel.EmailValidationViewModel;
import com.resons.app.viewmodel.ForgotOtpViewModel;
import com.resons.app.viewmodel.OtpVerificationViewModel;

public class otpvarification extends AppCompatActivity {

    private String email;
    private ActivityOtpvarificationBinding binding;
    private otpvarification mContext;
    private OtpVerificationViewModel viewModel;
    private ForgotOtpViewModel forgotOtpViewModel;


    private EditText[] otpFields;
    private int is_forgotPassword = 0;

    private String otp = "";

    private EmailValidationViewModel emailValidationViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_otpvarification);
        mContext = this;
        viewModel = new ViewModelProvider(this).get(OtpVerificationViewModel.class);
        emailValidationViewModel = new ViewModelProvider(this).get(EmailValidationViewModel.class);
        forgotOtpViewModel = new ViewModelProvider(this).get(ForgotOtpViewModel.class);
        if (getIntent().getExtras() != null) {
            email = getIntent().getStringExtra("email");
            is_forgotPassword = getIntent().getIntExtra("is_forgotPassword", 0);
        }

        observeLiveData();

        otpFields = new EditText[]{
                findViewById(R.id.otp_digit_1),
                findViewById(R.id.otp_digit_2),
                findViewById(R.id.otp_digit_3),
                findViewById(R.id.otp_digit_4),
                findViewById(R.id.otp_digit_5),
                findViewById(R.id.otp_digit_6)
        };
// Add GenericTextWatcher to each EditText
        for (int i = 0; i < otpFields.length; i++) {
            final int index = i;
            otpFields[i].addTextChangedListener(new GenericTextWatcher(otpFields, index));
        }


        binding.btnNext.setOnClickListener(v -> {

            otp = binding.otpDigit1.getText().toString().trim() +
                    binding.otpDigit2.getText().toString().trim() +
                    binding.otpDigit3.getText().toString().trim() +
                    binding.otpDigit4.getText().toString().trim() +
                    binding.otpDigit5.getText().toString().trim() +
                    binding.otpDigit6.getText().toString().trim();

            if (otp.isEmpty()){
                Toast.makeText(otpvarification.this, "Enter a valid otp", Toast.LENGTH_SHORT).show();
                return;
            }

            if (otp.length() != 6) {
                Toast.makeText(otpvarification.this, "Enter a valid 6-digit OTP", Toast.LENGTH_SHORT).show();
            } else {
                verifyOtp(otp);
            }
        });


        binding.tvResendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.tvResendOtp.getText().toString().trim().equals("Resend OTP")) {
                    emailValidationViewModel.postData(new EmailValidationRequest(email, "PASSWORD", "", "email"));
                }

            }
        });

        countDownTimer();
    }


    private void countDownTimer() {
        new CountDownTimer(300000, 1000) {

            public void onTick(long millisUntilFinished) {
                // Calculate minutes and seconds remaining
                long minutes = millisUntilFinished / 60000;
                long seconds = (millisUntilFinished % 60000) / 1000;

                // Update the TextView with the formatted time
                binding.tvResendOtp.setText("Resend OTP in " + minutes + ":" + String.format("%02d", seconds) + " Minutes");
                // logic to set the EditText could go here
            }

            public void onFinish() {
                binding.tvResendOtp.setText("Resend OTP");
                otp = "";
            }

        }.start();
    }


    private void verifyOtp(String otp) {

        if (is_forgotPassword == 1) {
            forgotOtpViewModel.postData(new ForgotPasswordOtpRequest(
                    otp, email, "1"
            ));
        } else {
            viewModel.postData(new OtpVerificationRequest(email, otp));
        }


    }

    private void observeLiveData() {
        viewModel.getResponseLiveData().observe(this, otpResponse -> {
            if (otpResponse.status() == 200) {
                Toast.makeText(mContext, otpResponse.getMessage(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(otpvarification.this, UpdateName.class);
                intent.putExtra("TOKEN", otpResponse.getToken());
                startActivity(intent);

            } else {
                Toast.makeText(otpvarification.this, otpResponse.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });

        emailValidationViewModel.getResponseLiveData().observe(this, emailValidationResponse -> {
            if (emailValidationResponse.status() == 201) {
                Toast.makeText(otpvarification.this, emailValidationResponse.getMessage(), Toast.LENGTH_SHORT).show();
                countDownTimer();
            } else {
                Toast.makeText(otpvarification.this, emailValidationResponse.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        emailValidationViewModel.getErrorLiveData().observe(this, error -> Toast.makeText(mContext, "Error: " + error, Toast.LENGTH_SHORT).show());

        emailValidationViewModel.getLoadingLiveData().observe(this, isLoading -> {
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

        forgotOtpViewModel.getResponseLiveData().observe(this, otpResponse -> {
            if (otpResponse.getStatus() == 200) {
                Toast.makeText(mContext, otpResponse.getMessage(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(otpvarification.this, Setpassword.class);
                intent.putExtra("TOKEN", otpResponse.getToken());
                startActivity(intent);

            } else {
                Toast.makeText(otpvarification.this, otpResponse.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });


        forgotOtpViewModel.getErrorLiveData().observe(this, error -> Toast.makeText(mContext, "Error: " + error, Toast.LENGTH_SHORT).show());

        forgotOtpViewModel.getLoadingLiveData().observe(this, isLoading -> {
            if (isLoading) {
                binding.progressBar.setVisibility(View.VISIBLE);
            } else {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }

    // GenericTextWatcher for OTP EditTexts
    private static class GenericTextWatcher implements TextWatcher {

        private final EditText[] editTexts;
        private final int currentIndex;

        public GenericTextWatcher(EditText[] editTexts, int currentIndex) {
            this.editTexts = editTexts;
            this.currentIndex = currentIndex;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // No operation
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // No operation
        }

        @Override
        public void afterTextChanged(Editable s) {
            String input = s.toString();

            if (input.length() == 1) {
                // Move to next EditText
                if (currentIndex < editTexts.length - 1) {
                    editTexts[currentIndex + 1].requestFocus();
                }
            } else if (input.isEmpty()) {
                // Move to previous EditText (optional)
                if (currentIndex > 0) {
                    editTexts[currentIndex - 1].requestFocus();
                }
            }
        }
    }

}
