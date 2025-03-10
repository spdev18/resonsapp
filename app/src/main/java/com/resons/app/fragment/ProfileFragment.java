package com.resons.app.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.resons.app.PrefManager;
import com.resons.app.R;
import com.resons.app.databinding.FragmentProfileBinding;
import com.resons.app.model.request.response.UserProfile;
import com.resons.app.viewmodel.ProfilePictureViewModel;
import com.resons.app.viewmodel.UpdateProfileViewModel;
import com.resons.app.viewmodel.UserProfileViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    private boolean isCamera = false;
    private boolean isGallery = false;
    private String profilePath = "";

    private int REQUEST_ID_MULTIPLE_PERMISSIONS = 2;
    private ProfilePictureViewModel viewModel;
    private UserProfileViewModel userProfileViewModel;
    private UpdateProfileViewModel updateProfileViewModel;

    private String gender = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        initUI();
        addListner();
        return binding.getRoot();
    }

    private void initUI() {
        viewModel = new ViewModelProvider(this).get(ProfilePictureViewModel.class);
        userProfileViewModel = new ViewModelProvider(this).get(UserProfileViewModel.class);
        updateProfileViewModel = new ViewModelProvider(this).get(UpdateProfileViewModel.class);

        userProfileViewModel.postData("Bearer " + new PrefManager(requireContext())
                .getvalue("token"));


        observeLiveData();
    }

    private void addListner() {
        binding.rrProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    alertDialogForImagePicker();
                } else {
                    checkPermission();
                }
            }
        });

        binding.btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidate()) {
                    updateProfileViewModel.postData("Bearer " + new PrefManager(requireContext()).getvalue("token"),
                            binding.edtName.getText().toString().trim(),
                            binding.edtDob.getText().toString().trim(), gender);
                }
            }
        });

        binding.edtDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePickerDialog();
            }
        });

        binding.tvMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMaleData();
            }
        });

        binding.tvFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFemaleData();
            }
        });
        binding.tvOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOtherData();
            }
        });
    }

    private boolean isValidate() {
        boolean isValid = true;
        if (binding.edtName.getText().toString().trim().isEmpty()) {
            Toast.makeText(requireContext(), "Please enter name", Toast.LENGTH_SHORT).show();
            isValid = false;
        }/* else if (binding.edtEmail.getText().toString().trim().isEmpty()) {
            Toast.makeText(requireContext(), "Please enter email", Toast.LENGTH_SHORT).show();
            isValid = false;
        }*/ else if (binding.edtDob.getText().toString().trim().isEmpty()) {
            Toast.makeText(requireContext(), "Please select dob", Toast.LENGTH_SHORT).show();
            isValid = false;
        } else if (gender.isEmpty()) {
            Toast.makeText(requireContext(), "Please select gender", Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        return isValid;
    }

    private void setMaleData() {
        binding.tvMale.setBackgroundResource(R.drawable.rectangle_white);
        binding.tvFemale.setBackgroundResource(0);
        binding.tvOther.setBackgroundResource(0);

        gender = "male";
    }

    private void setFemaleData() {

        binding.tvMale.setBackgroundResource(0);
        binding.tvFemale.setBackgroundResource(R.drawable.rectangle_white);
        binding.tvOther.setBackgroundResource(0);

        gender = "female";
    }

    private void setOtherData() {

        binding.tvMale.setBackgroundResource(0);
        binding.tvFemale.setBackgroundResource(0);
        binding.tvOther.setBackgroundResource(R.drawable.rectangle_white);

        gender = "other";
    }

    private void openDatePickerDialog() {
        // Get the current date
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                        // Create a Calendar instance with the selected date
                        Calendar selectedCalendar = Calendar.getInstance();
                        selectedCalendar.set(selectedYear, selectedMonth, selectedDay);

                        // Format the date to "yyyy-MM-dd"
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        String formattedDate = simpleDateFormat.format(selectedCalendar.getTime());

                        // Set the formatted date to the Button
                        binding.edtDob.setText(formattedDate);
                    }
                },
                year,
                month,
                day
        );
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        datePickerDialog.show();
    }

    private boolean checkPermission() {
        int writePermission;
        int cameraPermission = ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            writePermission = ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_MEDIA_IMAGES);
        } else {
            writePermission = ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        ArrayList<String> listPermissionsNeeded = new ArrayList<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (writePermission != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.READ_MEDIA_IMAGES);
            }
            if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.CAMERA);
            }
        } else {
            if (writePermission != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.CAMERA);
            }
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(requireActivity(), listPermissionsNeeded.toArray(new String[0]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_ID_MULTIPLE_PERMISSIONS) {
            HashMap<String, Integer> perms = new HashMap<>();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_MEDIA_IMAGES, PackageManager.PERMISSION_GRANTED);
            } else {
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
            }

            for (int i = 0; i < permissions.length; i++) {
                perms.put(permissions[i], grantResults[i]);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                        perms.get(Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                    alertDialogForImagePicker();
                }
            } else {
                if (perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                        perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    alertDialogForImagePicker();
                }
            }
        }
    }

    private void alertDialogForImagePicker() {
        Dialog dialogView = new Dialog(requireContext());
        dialogView.setContentView(R.layout.image_picker);
        dialogView.setCancelable(false);

        TextView txtCamera = dialogView.findViewById(R.id.txtcamera);
        TextView txtGallery = dialogView.findViewById(R.id.txtGallery);
        TextView txtCancel = dialogView.findViewById(R.id.txtCancel);

        txtCamera.setOnClickListener(v -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startForProfileImageResult.launch(intent);
            isCamera = true;
            isGallery = false;
            dialogView.dismiss();
        });

        txtGallery.setOnClickListener(v -> {
            ImagePicker.with(this)
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .galleryOnly()
                    .createIntent(intent -> {
                        startForProfileImageResult.launch(intent);
                        return null;
                    });
            isCamera = false;
            isGallery = true;
            dialogView.dismiss();
        });

        txtCancel.setOnClickListener(v -> dialogView.dismiss());
        dialogView.show();
    }

    private final ActivityResultLauncher<Intent> startForProfileImageResult =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), this::handleImageResult);

    private void handleImageResult(ActivityResult result) {
        int resultCode = result.getResultCode();
        Intent data = result.getData();

        if (resultCode == Activity.RESULT_OK) {
            if (isCamera) {
                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                profilePath = saveImageToStorage(imageBitmap);
                // Update your ImageView with the image
                binding.cvProfile.setImageURI(Uri.parse(profilePath));

                MultipartBody.Part profileBody = null;
                if (!profilePath.isEmpty()) {
                    File file = new File(profilePath);
                    // create RequestBody instance from file
                    RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

                    // Create the MultipartBody.Part
                    profileBody = MultipartBody.Part.createFormData(
                            "photo", file.getName(), requestFile
                    );

                    viewModel.postData("Bearer " + new PrefManager(requireContext())
                            .getvalue("token"), profileBody);
                }


            } else if (isGallery) {
                Uri uri = data.getData();
                profilePath = uri.getPath();
                binding.cvProfile.setImageURI(uri);
                MultipartBody.Part profileBody = null;
                if (!profilePath.isEmpty()) {
                    File file = new File(profilePath);
                    // create RequestBody instance from file
                    RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

                    // Create the MultipartBody.Part
                    profileBody = MultipartBody.Part.createFormData(
                            "photo", file.getName(), requestFile
                    );

                    viewModel.postData("Bearer " + new PrefManager(requireContext())
                            .getvalue("token"), profileBody);
                }


                // Update your ImageView with the image
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    private String saveImageToStorage(Bitmap bitmap) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + ".jpg";
        File storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        try {
            File imageFile = new File(storageDir, imageFileName);
            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.close();
            return imageFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    private void observeLiveData() {
        viewModel.getResponseLiveData().observe(requireActivity(), response -> {
            if (response.getStatus() == 200) {
                Toast.makeText(requireContext(), response.getMessage(), Toast.LENGTH_SHORT).show();
                userProfileViewModel.postData("Bearer " + new PrefManager(requireContext())
                        .getvalue("token"));

            } else {
                Toast.makeText(requireContext(), response.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });


        updateProfileViewModel.getResponseLiveData().observe(requireActivity(), response -> {
            if (response.getStatus() == 200) {
                Toast.makeText(requireContext(), response.getMessage(), Toast.LENGTH_SHORT).show();
                userProfileViewModel.postData("Bearer " + new PrefManager(requireContext())
                        .getvalue("token"));

            } else {
                Toast.makeText(requireContext(), response.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });

        updateProfileViewModel.getErrorLiveData().observe(requireActivity(), error -> Toast.makeText(requireContext(), "Error: " + error, Toast.LENGTH_SHORT).show());

        updateProfileViewModel.getLoadingLiveData().observe(requireActivity(), isLoading -> {
            if (isLoading) {
                binding.progressBar.setVisibility(View.VISIBLE);
            } else {
                binding.progressBar.setVisibility(View.GONE);
            }
        });

        userProfileViewModel.getResponseLiveData().observe(requireActivity(), response -> {
            setProfileData(response);

        });

        userProfileViewModel.getErrorLiveData().observe(requireActivity(), error -> Toast.makeText(requireContext(), "Error: " + error, Toast.LENGTH_SHORT).show());

        userProfileViewModel.getLoadingLiveData().observe(requireActivity(), isLoading -> {
            if (isLoading) {
                binding.progressBar.setVisibility(View.VISIBLE);
            } else {
                binding.progressBar.setVisibility(View.GONE);
            }
        });

        viewModel.getErrorLiveData().observe(requireActivity(), error -> Toast.makeText(requireContext(), "Error: " + error, Toast.LENGTH_SHORT).show());

        viewModel.getLoadingLiveData().observe(requireActivity(), isLoading -> {
            if (isLoading) {
                binding.progressBar.setVisibility(View.VISIBLE);
            } else {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void setProfileData(UserProfile response) {
        if (response.getPhoto()!=null){
            Glide.with(requireContext()).load("http://45.79.40.87:9595/" + response.getPhoto())
                    .placeholder(R.drawable.ic_profile)
                    .error(R.drawable.ic_profile)
                    .into(binding.cvProfile);
        }



        binding.edtName.setText(response.getName());
        binding.edtEmail.setText(response.getEmail());

        if (response.getDob()!=null){
            binding.edtDob.setText(response.getDob());
        }

        if (response.getGender()!=null){
            if (response.getGender().equals("male")) {
                setMaleData();
            } else if (response.getGender().equals("female")) {
                setFemaleData();
            } else if (response.getGender().equals("other")) {
                setOtherData();
            }
        }

    }

}