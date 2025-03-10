package com.resons.app;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

//import com.squareup.picasso.Picasso;

import com.resons.app.model.request.response.UserProfile;
import com.resons.app.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Profile extends AppCompatActivity {

    private static final String BASE_URL = "http://45.79.40.87:9595/api/v2/"; // Replace with your actual API base URL

    private ImageView profileImage;
    private EditText editName, editEmail, editDob;
    private RadioGroup genderGroup;
    private RadioButton maleButton, femaleButton, otherButton;

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize Views
        profileImage = findViewById(R.id.profileImage);
        editName = findViewById(R.id.editName);
        editEmail = findViewById(R.id.editEmail);
        editDob = findViewById(R.id.editDob);
        genderGroup = findViewById(R.id.genderGroup);
        maleButton = findViewById(R.id.radioMale);
        femaleButton = findViewById(R.id.radioFemale);
        otherButton = findViewById(R.id.radioOther);

        // Setup Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);

        // Fetch User Profile Data
        fetchUserProfile();
    }

    private void fetchUserProfile() {
        Call<UserProfile> call = apiService.getUserProfile("");
        call.enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Populate the UI with fetched data
                    UserProfile userProfile = response.body();
                    populateUserProfile(userProfile);
                } else {
                    Toast.makeText(Profile.this, "Failed to fetch profile", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                Log.e("ProfileAPI", "Error: " + t.getMessage());
                Toast.makeText(Profile.this, "Failed to connect to server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateUserProfile(UserProfile userProfile) {
        // Set Name
        editName.setText(userProfile.getName());

        // Set Email
        editEmail.setText(userProfile.getEmail());

        // Set Date of Birth
        editDob.setText(userProfile.getDob());

        // Set Gender
        String gender = userProfile.getGender();
        if (gender.equalsIgnoreCase("male")) {
            maleButton.setChecked(true);
        } else if (gender.equalsIgnoreCase("female")) {
            femaleButton.setChecked(true);
        } else if (gender.equalsIgnoreCase("other")) {
            otherButton.setChecked(true);
        }

//        // Load profile image if your API includes an image URL
//        String imageUrl = userProfile.getProfileImageUrl(); // Replace with the actual field name in your API
//        Picasso.get().load(imageUrl).placeholder(R.drawable.ic_camera).into(profileImage);
    }
}
