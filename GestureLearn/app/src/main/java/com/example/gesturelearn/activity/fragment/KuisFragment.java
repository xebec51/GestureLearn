package com.example.gesturelearn.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        return inflater.inflate(R.layout.fragment_kuis, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CardView cardKuisSibi = view.findViewById(R.id.card_kuis_sibi);
        CardView cardKuisBisindo = view.findViewById(R.id.card_kuis_bisindo);
        CardView cardKuisKosakata = view.findViewById(R.id.card_kuis_kosakata);

        cardKuisSibi.setOnClickListener(v -> {
            startQuiz("ABJAD_SIBI", "Kuis Abjad SIBI");
        });

        cardKuisBisindo.setOnClickListener(v -> {
            startQuiz("ABJAD_BISINDO", "Kuis Abjad BISINDO");
        });

        cardKuisKosakata.setOnClickListener(v -> {
            startQuiz("KOSAKATA", "Kuis Kosa Kata");
        });
    }

    private void startQuiz(String category, String title) {
        Intent intent = new Intent(getActivity(), QuizStartActivity.class);
        intent.putExtra("QUIZ_CATEGORY", category);
        intent.putExtra("QUIZ_TITLE", title);
        startActivity(intent);
    }
}