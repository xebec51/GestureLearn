package com.example.gesturelearn.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gesturelearn.R;
import com.example.gesturelearn.data.DatabaseHelper;

public class EditProfileActivity extends AppCompatActivity {

    private EditText etName;
    private TextView tvEmail;
    private Button btnSave;
    private ImageButton btnBack;
    private DatabaseHelper databaseHelper;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        databaseHelper = new DatabaseHelper(this);

        // Inisialisasi Views
        etName = findViewById(R.id.et_name_edit);
        tvEmail = findViewById(R.id.tv_email_edit);
        btnSave = findViewById(R.id.btn_save);
        btnBack = findViewById(R.id.btn_back);

        // Ambil email dari intent
        userEmail = getIntent().getStringExtra("USER_EMAIL");

        // Tampilkan data awal
        loadInitialData();

        // Listener untuk tombol
        btnBack.setOnClickListener(v -> finish());
        btnSave.setOnClickListener(v -> saveProfileChanges());
    }

    private void loadInitialData() {
        if (userEmail != null && !userEmail.isEmpty()) {
            String currentName = databaseHelper.getUserName(userEmail);
            etName.setText(currentName);
            tvEmail.setText(userEmail);
        }
    }

    private void saveProfileChanges() {
        String newName = etName.getText().toString().trim();

        if (newName.isEmpty()) {
            Toast.makeText(this, "Nama tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }

        if (userEmail != null) {
            boolean isUpdated = databaseHelper.updateUser(userEmail, newName);
            if (isUpdated) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("IS_PROFILE_UPDATED", true);
                setResult(Activity.RESULT_OK, resultIntent);
                finish(); // Kembali ke halaman profil
            } else {
                Toast.makeText(this, "Gagal memperbarui profil", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
