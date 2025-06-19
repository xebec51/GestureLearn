package com.example.gesturelearn.activity.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.gesturelearn.R;
import com.example.gesturelearn.activity.EditProfileActivity;
import com.example.gesturelearn.activity.auth.LoginActivity;
import com.example.gesturelearn.data.DatabaseHelper;

public class ProfileFragment extends Fragment {

    private TextView tvNameValue, tvEmailValue, tvPointValue;
    private Button btnEditProfile, btnLogout;
    private DatabaseHelper databaseHelper;
    private String userEmail;

    // DIUBAH: Deklarasikan ActivityResultLauncher
    private ActivityResultLauncher<Intent> editProfileLauncher;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // DIUBAH: Inisialisasi launcher di onCreate
        editProfileLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    // Cek apakah hasilnya OK dan ada data pembaruan
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        boolean isProfileUpdated = result.getData().getBooleanExtra("IS_PROFILE_UPDATED", false);
                        if (isProfileUpdated) {
                            // Jika profil diperbarui, muat ulang data pengguna
                            loadUserData();
                            Toast.makeText(getContext(), "Profil berhasil diperbarui.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        databaseHelper = new DatabaseHelper(getContext());
        tvNameValue = view.findViewById(R.id.tv_name_value);
        tvEmailValue = view.findViewById(R.id.tv_email_value);
        tvPointValue = view.findViewById(R.id.tv_point_value);
        btnEditProfile = view.findViewById(R.id.btn_editProfile);
        btnLogout = view.findViewById(R.id.btn_logout);

        loadUserData();

        btnEditProfile.setOnClickListener(v -> {
            // DIUBAH: Gunakan launcher untuk memulai EditProfileActivity
            Intent intent = new Intent(getActivity(), EditProfileActivity.class);
            intent.putExtra("USER_EMAIL", userEmail);
            editProfileLauncher.launch(intent);
        });

        btnLogout.setOnClickListener(v -> {
            logoutUser();
        });
    }

    private void loadUserData() {
        if (getActivity() != null && getActivity().getIntent() != null) {
            userEmail = getActivity().getIntent().getStringExtra("USER_EMAIL");
            if (userEmail != null && !userEmail.isEmpty()) {
                String userName = databaseHelper.getUserName(userEmail);
                if (userName != null) {
                    tvNameValue.setText(userName.toUpperCase());
                    tvEmailValue.setText(userEmail);
                    tvPointValue.setText("129");
                }
            }
        }
    }

    private void logoutUser() {
        Toast.makeText(getContext(), "Logout berhasil!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        if (getActivity() != null) {
            getActivity().finish();
        }
    }
}