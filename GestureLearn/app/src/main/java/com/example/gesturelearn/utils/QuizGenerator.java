package com.example.gesturelearn.utils;

import android.content.Context;
import com.example.gesturelearn.model.QuizQuestion;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizGenerator {

    // Model internal untuk menyimpan pasangan kata dan URL
    private static class QuizData {
        String word;
        String url;

        QuizData(String word, String url) {
            this.word = word;
            this.url = url;
        }
    }

    // Metode utama untuk menghasilkan daftar pertanyaan
    public static List<QuizQuestion> generateQuestions(Context context, int numberOfQuestions) {
        List<QuizData> allQuizData = loadQuizData(context);
        if (allQuizData.size() < 4) {
            return new ArrayList<>(); // Tidak cukup data untuk membuat kuis
        }

        Collections.shuffle(allQuizData);

        List<QuizQuestion> questions = new ArrayList<>();
        for (int i = 0; i < numberOfQuestions && i < allQuizData.size(); i++) {
            QuizData currentData = allQuizData.get(i);
            String correctAnswer = currentData.word;
            String gifUrl = currentData.url;

            // Siapkan 4 pilihan jawaban
            List<String> options = new ArrayList<>();
            options.add(correctAnswer);

            // Ambil 3 jawaban salah secara acak
            List<QuizData> tempWrongAnswers = new ArrayList<>(allQuizData);
            tempWrongAnswers.remove(i); // Hapus jawaban benar dari daftar
            Collections.shuffle(tempWrongAnswers);

            for (int j = 0; j < 3; j++) {
                options.add(tempWrongAnswers.get(j).word);
            }

            // Acak urutan pilihan jawaban
            Collections.shuffle(options);
            questions.add(new QuizQuestion(gifUrl, correctAnswer, options));
        }

        return questions;
    }

    // Metode untuk membaca file dari folder assets
    private static List<QuizData> loadQuizData(Context context) {
        List<QuizData> dataList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open("GestureLearnGifList.txt")))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    dataList.add(new QuizData(parts[0], parts[1]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataList;
    }
}