package com.example.gesturelearn.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.gesturelearn.R;
import com.example.gesturelearn.activity.kuis.QuizStartActivity;

public class KuisFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate layout yang sudah kita desain
        return inflater.inflate(R.layout.fragment_kuis, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Contoh menambahkan listener untuk salah satu tombol "Mulai"
        CardView cardKuisSibi = view.findViewById(R.id.card_kuis_sibi);
        cardKuisSibi.setOnClickListener(v -> {
            // TODO: Tambahkan logika untuk pindah ke halaman Kuis SIBI
            Toast.makeText(getContext(), "Memulai Kuis SIBI...", Toast.LENGTH_SHORT).show();
        });

        CardView cardKuisBisindo = view.findViewById(R.id.card_kuis_bisindo);
        cardKuisBisindo.setOnClickListener(v -> {
            // TODO: Tambahkan logika untuk pindah ke halaman Kuis BISINDO
            Toast.makeText(getContext(), "Memulai Kuis BISINDO...", Toast.LENGTH_SHORT).show();
        });

        CardView cardKuisKosakata = view.findViewById(R.id.card_kuis_kosakata);
        cardKuisKosakata.setOnClickListener(v -> {
            // Ganti Toast dengan Intent untuk membuka QuizStartActivity
            Intent intent = new Intent(getActivity(), QuizStartActivity.class);
            startActivity(intent);
        });
    }
}