package com.example.gesturelearn.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView; // DIUBAH: dari EditText ke TextView
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.gesturelearn.R;
import com.example.gesturelearn.activity.auth.LoginActivity;
import com.example.gesturelearn.data.DatabaseHelper;

public class ProfileFragment extends Fragment {

    // DIUBAH: Menggunakan TextView untuk menampilkan data, bukan EditText
    private TextView tvNameValue, tvEmailValue, tvPointValue;
    private Button btnEditProfile, btnLogout;
    private DatabaseHelper databaseHelper;
    private String userEmail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate layout yang sudah diperbaiki
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inisialisasi DatabaseHelper
        databaseHelper = new DatabaseHelper(getContext());

        // DIUBAH: Inisialisasi Views dengan ID dari layout baru
        tvNameValue = view.findViewById(R.id.tv_name_value);
        tvEmailValue = view.findViewById(R.id.tv_email_value);
        tvPointValue = view.findViewById(R.id.tv_point_value);
        btnEditProfile = view.findViewById(R.id.btn_editProfile);
        btnLogout = view.findViewById(R.id.btn_logout);

        // Memuat data pengguna yang sedang login
        loadUserData();

        // Menambahkan listener untuk tombol
        btnEditProfile.setOnClickListener(v -> {
            // TODO: Tambahkan logika untuk pindah ke halaman edit profil
            Toast.makeText(getContext(), "Fitur Edit Profile belum tersedia.", Toast.LENGTH_SHORT).show();
        });

        btnLogout.setOnClickListener(v -> {
            logoutUser();
        });
    }

    private void loadUserData() {
        if (getActivity() != null && getActivity().getIntent() != null) {
            userEmail = getActivity().getIntent().getStringExtra("USER_EMAIL");
            if (userEmail != null && !userEmail.isEmpty()) {
                // Ambil nama dari database menggunakan email
                String userName = databaseHelper.getUserName(userEmail);

                if (userName != null) {
                    // DIUBAH: Set teks ke TextView
                    tvNameValue.setText(userName.toUpperCase()); // Dibuat kapital sesuai desain
                    tvEmailValue.setText(userEmail);
                    // TODO: Ganti dengan logika poin yang sebenarnya jika sudah ada
                    tvPointValue.setText("129"); // Placeholder poin
                }
            }
        }
    }

    private void logoutUser() {
        // Hapus data sesi jika ada (misalnya menggunakan SharedPreferences)
        // Untuk saat ini, kita langsung kembali ke halaman Login

        Toast.makeText(getContext(), "Logout berhasil!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        // Membersihkan semua activity sebelumnya dari stack
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        if (getActivity() != null) {
            getActivity().finish();
        }
    }
}