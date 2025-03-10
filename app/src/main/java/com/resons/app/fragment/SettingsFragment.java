package com.resons.app.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.resons.app.PrefManager;
import com.resons.app.R;
import com.resons.app.activity.LoginActivity;
import com.resons.app.databinding.FragmentSettingsBinding;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings,
                container, false);
        initUI();
        addListner();
        return binding.getRoot();
    }

    private void initUI() {
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        mFirebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        String uploadWordLimit = mFirebaseRemoteConfig.getString("upload_word_limit");
                        binding.tvUploadLimitValue.setText(uploadWordLimit);
                    } else {
                    }
                });


        binding.tvAppVersionValue.setText("Version:- " + "5.0");
    }

    private void addListner() {
        binding.tvLogout.setOnClickListener(v -> showLogoutDialog());
    }

    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");

        builder.setPositiveButton("Yes", (dialog, which) -> {
            dialog.dismiss();
            new PrefManager(requireContext()).Clear();
            startActivity(new Intent(requireContext(), LoginActivity.class));
            requireActivity().finishAffinity();
        });

        builder.setNegativeButton("No", (dialog, which) -> {
            dialog.dismiss();
        });

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}