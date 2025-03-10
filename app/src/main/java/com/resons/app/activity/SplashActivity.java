package com.resons.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.resons.app.PrefManager;
import com.resons.app.R;
import com.resons.app.StaticData;
import com.resons.app.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {
    private ActivitySplashBinding binding;
    private SplashActivity mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        mContext = this;

        // Load static first frame of GIF
        Glide.with(this)
                .asGif() // Loads only the first frame as a static image
                .load(R.drawable.splashscreen) // Place your GIF in res/drawable folder
                .into(binding.ivImage);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (new PrefManager(mContext).getvalue(StaticData.islogin, false)) {
                    startActivity(new Intent(mContext, DashboardActivity.class));
                } else {
                    startActivity(new Intent(mContext, LoginActivity.class));
                }
                finish();
            }
        }, 3000);

    }
}