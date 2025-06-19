package com.example.gesturelearn.utils;

import com.example.gesturelearn.data.SignDao;
import com.example.gesturelearn.model.QuizQuestion;
import com.example.gesturelearn.model.ReverseQuizQuestion;
import com.example.gesturelearn.model.Sign;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizGenerator {

    /**
     * Metode utama untuk menghasilkan daftar pertanyaan dari database melalui DAO.
     * @param signDao Objek DAO untuk mengakses database.
     * @param category Kategori kuis yang ingin dibuat (misal: "KOSAKATA").
     * @param numberOfQuestions Jumlah pertanyaan yang ingin dibuat.
     * @return Daftar pertanyaan kuis.
     */
    public static List<QuizQuestion> generateQuestions(SignDao signDao, String category, int numberOfQuestions) {
        // Ambil data berdasarkan kategori dari database
        List<Sign> categorySigns = signDao.getSignsByCategory(category);

        // --- PERBAIKAN 1: Hapus pengambilan semua data yang tidak perlu ---
        // List<Sign> allSigns = signDao.getAllSigns();

        // --- PERBAIKAN 2: Pastikan data di kategori ini cukup untuk membuat pilihan ganda ---
        // Kita butuh setidaknya 4 item untuk 1 jawaban benar dan 3 jawaban salah.
        if (categorySigns.size() < 4) {
            return new ArrayList<>(); // Kembalikan list kosong jika data tidak cukup
        }

        // Acak urutan pertanyaan dari kategori yang dipilih
        Collections.shuffle(categorySigns);

        List<QuizQuestion> questions = new ArrayList<>();
        // Buat pertanyaan sejumlah yang diminta atau sebanyak data yang ada
        for (int i = 0; i < numberOfQuestions && i < categorySigns.size(); i++) {
            Sign currentSign = categorySigns.get(i);
            String correctAnswer = currentSign.word;
            String gifUrl = currentSign.gifUrl;

            // Siapkan 4 pilihan jawaban, dimulai dengan jawaban yang benar
            List<String> options = new ArrayList<>();
            options.add(correctAnswer);

            // --- PERBAIKAN 3: Ambil jawaban salah dari kategori yang sama ---
            // Buat kumpulan jawaban salah yang tidak mengandung jawaban benar
            List<Sign> wrongAnswerPool = new ArrayList<>(categorySigns); // Menggunakan categorySigns, bukan allSigns
            wrongAnswerPool.removeIf(sign -> sign.word.equals(correctAnswer));
            Collections.shuffle(wrongAnswerPool);

            // Ambil 3 jawaban salah secara acak
            for (int j = 0; j < 3 && j < wrongAnswerPool.size(); j++) {
                options.add(wrongAnswerPool.get(j).word);
            }

            // Acak urutan dari 4 pilihan jawaban tersebut
            Collections.shuffle(options);
            questions.add(new QuizQuestion(gifUrl, correctAnswer, options));
        }

        return questions;
    }

    public static List<ReverseQuizQuestion> generateReverseQuestions(SignDao signDao, String category, int numberOfQuestions) {
        List<Sign> categorySigns = signDao.getSignsByCategory(category);

        if (categorySigns.size() < 4) {
            return new ArrayList<>(); // Tidak cukup data
        }

        Collections.shuffle(categorySigns);

        List<ReverseQuizQuestion> questions = new ArrayList<>();
        for (int i = 0; i < numberOfQuestions && i < categorySigns.size(); i++) {
            Sign correctAnswer = categorySigns.get(i);

            List<Sign> options = new ArrayList<>();
            options.add(correctAnswer);

            List<Sign> wrongAnswerPool = new ArrayList<>(categorySigns);
            wrongAnswerPool.remove(correctAnswer);
            Collections.shuffle(wrongAnswerPool);

            for (int j = 0; j < 3 && j < wrongAnswerPool.size(); j++) {
                options.add(wrongAnswerPool.get(j));
            }

            Collections.shuffle(options);
            questions.add(new ReverseQuizQuestion(correctAnswer, options));
        }
        return questions;
    }
}