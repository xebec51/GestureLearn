package com.example.gesturelearn.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gesturelearn.R;
import com.example.gesturelearn.data.DatabaseHelper;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText etCurrentPassword, etNewPassword, etConfirmNewPassword;
    private Button btnSavePassword;
    private ImageButton btnBack;
    private DatabaseHelper databaseHelper;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        databaseHelper = new DatabaseHelper(this);
        etCurrentPassword = findViewById(R.id.et_current_password);
        etNewPassword = findViewById(R.id.et_new_password);
        etConfirmNewPassword = findViewById(R.id.et_confirm_new_password);
        btnSavePassword = findViewById(R.id.btn_save_password);
        btnBack = findViewById(R.id.btn_back);
        userEmail = getIntent().getStringExtra("USER_EMAIL");
        btnBack.setOnClickListener(v -> finish());
        btnSavePassword.setOnClickListener(v -> updatePassword());
    }

    private void updatePassword() {
        String currentPassword = etCurrentPassword.getText().toString().trim();
        String newPassword = etNewPassword.getText().toString().trim();
        String confirmNewPassword = etConfirmNewPassword.getText().toString().trim();

        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
            Toast.makeText(this, "Semua kolom harus diisi", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPassword.equals(confirmNewPassword)) {
            Toast.makeText(this, "Password baru tidak cocok", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verifikasi password saat ini
        if (userEmail != null && databaseHelper.checkUser(userEmail, currentPassword)) {
            // Jika password saat ini benar, update ke password baru
            boolean isUpdated = databaseHelper.updatePassword(userEmail, newPassword);
            if (isUpdated) {
                Toast.makeText(this, "Password berhasil diperbarui", Toast.LENGTH_SHORT).show();
                finish(); // Kembali ke halaman profil
            } else {
                Toast.makeText(this, "Gagal memperbarui password", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Jika password saat ini salah
            Toast.makeText(this, "Password saat ini salah", Toast.LENGTH_SHORT).show();
        }
    }
}