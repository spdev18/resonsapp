package com.resons.app.activity;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.resons.app.PrefManager;
import com.resons.app.R;
import com.resons.app.databinding.ActivityDashboardBinding;
import com.resons.app.fragment.ContactsFragment;
import com.resons.app.fragment.HistoryFragment;
import com.resons.app.fragment.HomeFragment;
import com.resons.app.fragment.NotificationsFragment;
import com.resons.app.fragment.ProfileFragment;
import com.resons.app.fragment.SettingsFragment;

public class DashboardActivity extends AppCompatActivity {

    private ActivityDashboardBinding binding;
    private DashboardActivity mContext;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);
        mContext = this;
        initUI();
        addListner();
    }

    private void initUI() {


        setHomeData();
    }

    private void addListner() {
        binding.llHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHomeData();
            }
        });

        binding.llProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setProfileData();
            }
        });
        binding.llNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNotificationsData();
            }
        });

        binding.llHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHistoryData();
            }
        });

        binding.llSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSettingData();
            }
        });

        binding.btnContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContactsData();
            }
        });
    }

    private void setContactsData() {
        replaceFragment(new ContactsFragment());
    }

    private void setHomeData() {
        binding.tvHome.setTextColor(ContextCompat.getColor(mContext, R.color.blue));
        binding.tvProfile.setTextColor(ContextCompat.getColor(mContext, R.color.grey));
        binding.tvHistory.setTextColor(ContextCompat.getColor(mContext, R.color.grey));
        binding.tvNotifications.setTextColor(ContextCompat.getColor(mContext, R.color.grey));
        binding.tvSettings.setTextColor(ContextCompat.getColor(mContext, R.color.grey));

        binding.ivHome.setColorFilter(ContextCompat.getColor(mContext, R.color.blue), android.graphics.PorterDuff.Mode.MULTIPLY);
        binding.ivProfile.setColorFilter(ContextCompat.getColor(mContext, R.color.grey), android.graphics.PorterDuff.Mode.MULTIPLY);
        binding.ivHistory.setColorFilter(ContextCompat.getColor(mContext, R.color.grey), android.graphics.PorterDuff.Mode.MULTIPLY);
        binding.ivNotifications.setColorFilter(ContextCompat.getColor(mContext, R.color.grey), android.graphics.PorterDuff.Mode.MULTIPLY);
        binding.ivSettings.setColorFilter(ContextCompat.getColor(mContext, R.color.grey), android.graphics.PorterDuff.Mode.MULTIPLY);

        replaceFragment(new HomeFragment());

    }


    private void setProfileData() {
        binding.tvHome.setTextColor(ContextCompat.getColor(mContext, R.color.grey));
        binding.tvProfile.setTextColor(ContextCompat.getColor(mContext, R.color.blue));
        binding.tvHistory.setTextColor(ContextCompat.getColor(mContext, R.color.grey));
        binding.tvNotifications.setTextColor(ContextCompat.getColor(mContext, R.color.grey));
        binding.tvSettings.setTextColor(ContextCompat.getColor(mContext, R.color.grey));

        binding.ivHome.setColorFilter(ContextCompat.getColor(mContext, R.color.grey), android.graphics.PorterDuff.Mode.MULTIPLY);
        binding.ivProfile.setColorFilter(ContextCompat.getColor(mContext, R.color.blue), android.graphics.PorterDuff.Mode.MULTIPLY);
        binding.ivHistory.setColorFilter(ContextCompat.getColor(mContext, R.color.grey), android.graphics.PorterDuff.Mode.MULTIPLY);
        binding.ivNotifications.setColorFilter(ContextCompat.getColor(mContext, R.color.grey), android.graphics.PorterDuff.Mode.MULTIPLY);
        binding.ivSettings.setColorFilter(ContextCompat.getColor(mContext, R.color.grey), android.graphics.PorterDuff.Mode.MULTIPLY);

        replaceFragment(new ProfileFragment());
    }


    private void setNotificationsData() {
        binding.tvHome.setTextColor(ContextCompat.getColor(mContext, R.color.grey));
        binding.tvProfile.setTextColor(ContextCompat.getColor(mContext, R.color.grey));
        binding.tvHistory.setTextColor(ContextCompat.getColor(mContext, R.color.grey));
        binding.tvNotifications.setTextColor(ContextCompat.getColor(mContext, R.color.blue));
        binding.tvSettings.setTextColor(ContextCompat.getColor(mContext, R.color.grey));

        binding.ivHome.setColorFilter(ContextCompat.getColor(mContext, R.color.grey), android.graphics.PorterDuff.Mode.MULTIPLY);
        binding.ivProfile.setColorFilter(ContextCompat.getColor(mContext, R.color.grey), android.graphics.PorterDuff.Mode.MULTIPLY);
        binding.ivHistory.setColorFilter(ContextCompat.getColor(mContext, R.color.grey), android.graphics.PorterDuff.Mode.MULTIPLY);
        binding.ivNotifications.setColorFilter(ContextCompat.getColor(mContext, R.color.blue), android.graphics.PorterDuff.Mode.MULTIPLY);
        binding.ivSettings.setColorFilter(ContextCompat.getColor(mContext, R.color.grey), android.graphics.PorterDuff.Mode.MULTIPLY);

        replaceFragment(new NotificationsFragment());
    }


    private void setHistoryData() {
        binding.tvHome.setTextColor(ContextCompat.getColor(mContext, R.color.grey));
        binding.tvProfile.setTextColor(ContextCompat.getColor(mContext, R.color.grey));
        binding.tvHistory.setTextColor(ContextCompat.getColor(mContext, R.color.blue));
        binding.tvNotifications.setTextColor(ContextCompat.getColor(mContext, R.color.grey));
        binding.tvSettings.setTextColor(ContextCompat.getColor(mContext, R.color.grey));

        binding.ivHome.setColorFilter(ContextCompat.getColor(mContext, R.color.grey), android.graphics.PorterDuff.Mode.MULTIPLY);
        binding.ivProfile.setColorFilter(ContextCompat.getColor(mContext, R.color.grey), android.graphics.PorterDuff.Mode.MULTIPLY);
        binding.ivHistory.setColorFilter(ContextCompat.getColor(mContext, R.color.blue), android.graphics.PorterDuff.Mode.MULTIPLY);
        binding.ivNotifications.setColorFilter(ContextCompat.getColor(mContext, R.color.grey), android.graphics.PorterDuff.Mode.MULTIPLY);
        binding.ivSettings.setColorFilter(ContextCompat.getColor(mContext, R.color.grey), android.graphics.PorterDuff.Mode.MULTIPLY);

        replaceFragment(new HistoryFragment());
    }

    private void setSettingData() {
        binding.tvHome.setTextColor(ContextCompat.getColor(mContext, R.color.grey));
        binding.tvProfile.setTextColor(ContextCompat.getColor(mContext, R.color.grey));
        binding.tvHistory.setTextColor(ContextCompat.getColor(mContext, R.color.grey));
        binding.tvNotifications.setTextColor(ContextCompat.getColor(mContext, R.color.grey));
        binding.tvSettings.setTextColor(ContextCompat.getColor(mContext, R.color.blue));

        binding.ivHome.setColorFilter(ContextCompat.getColor(mContext, R.color.grey), android.graphics.PorterDuff.Mode.MULTIPLY);
        binding.ivProfile.setColorFilter(ContextCompat.getColor(mContext, R.color.grey), android.graphics.PorterDuff.Mode.MULTIPLY);
        binding.ivHistory.setColorFilter(ContextCompat.getColor(mContext, R.color.grey), android.graphics.PorterDuff.Mode.MULTIPLY);
        binding.ivNotifications.setColorFilter(ContextCompat.getColor(mContext, R.color.grey), android.graphics.PorterDuff.Mode.MULTIPLY);
        binding.ivSettings.setColorFilter(ContextCompat.getColor(mContext, R.color.blue), android.graphics.PorterDuff.Mode.MULTIPLY);

        replaceFragment(new SettingsFragment());
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}