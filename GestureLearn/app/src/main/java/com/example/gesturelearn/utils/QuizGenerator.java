package com.example.gesturelearn.utils;

import com.example.gesturelearn.data.SignDao;
import com.example.gesturelearn.model.IQuizQuestion;
import com.example.gesturelearn.model.QuizQuestion;
import com.example.gesturelearn.model.ReverseQuizQuestion;
import com.example.gesturelearn.model.Sign;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizGenerator {

    /**
     * Metode untuk menghasilkan kuis format standar (Tebak Arti dari GIF).
     * @param signDao Objek DAO untuk mengakses database.
     * @param category Kategori kuis (misal: "KOSAKATA").
     * @param numberOfQuestions Jumlah pertanyaan.
     * @return Daftar pertanyaan kuis standar.
     */
    public static List<QuizQuestion> generateQuestions(SignDao signDao, String category, int numberOfQuestions) {
        List<Sign> categorySigns = signDao.getSignsByCategory(category);

        if (categorySigns.size() < 4) {
            return new ArrayList<>(); // Kembalikan list kosong jika data tidak cukup
        }

        Collections.shuffle(categorySigns);

        List<QuizQuestion> questions = new ArrayList<>();
        for (int i = 0; i < numberOfQuestions && i < categorySigns.size(); i++) {
            Sign currentSign = categorySigns.get(i);
            String correctAnswer = currentSign.word;
            String gifUrl = currentSign.gifUrl;

            List<String> options = new ArrayList<>();
            options.add(correctAnswer);

            List<Sign> wrongAnswerPool = new ArrayList<>(categorySigns);
            wrongAnswerPool.removeIf(sign -> sign.word.equals(correctAnswer));
            Collections.shuffle(wrongAnswerPool);

            for (int j = 0; j < 3 && j < wrongAnswerPool.size(); j++) {
                options.add(wrongAnswerPool.get(j).word);
            }

            Collections.shuffle(options);
            questions.add(new QuizQuestion(gifUrl, correctAnswer, options));
        }

        return questions;
    }

    /**
     * Metode untuk menghasilkan kuis format terbalik (Tebak GIF dari Arti).
     * @param signDao Objek DAO untuk mengakses database.
     * @param category Kategori kuis.
     * @param numberOfQuestions Jumlah pertanyaan.
     * @return Daftar pertanyaan kuis terbalik.
     */
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

    /**
     * Metode untuk menghasilkan kuis abjad campuran (5 standar, 5 terbalik).
     * @param signDao Objek DAO.
     * @param category Kategori abjad (SIBI atau BISINDO).
     * @param totalQuestions Jumlah total pertanyaan (direkomendasikan 10).
     * @return Daftar pertanyaan campuran yang sudah diacak.
     */
    public static List<IQuizQuestion> generateMixedAlphabetQuiz(SignDao signDao, String category, int totalQuestions) {
        List<Sign> categorySigns = signDao.getSignsByCategory(category);
        if (categorySigns.size() < 4) {
            return new ArrayList<>(); // Tidak cukup data
        }
        Collections.shuffle(categorySigns);

        List<IQuizQuestion> mixedQuestions = new ArrayList<>();
        int standardQuestionsCount = totalQuestions / 2;

        int availableSigns = categorySigns.size();

        // Buat 5 soal standar (tebak huruf)
        for (int i = 0; i < standardQuestionsCount && i < availableSigns; i++) {
            Sign currentSign = categorySigns.get(i);
            String correctAnswer = currentSign.word;
            String gifUrl = currentSign.gifUrl;

            List<String> options = new ArrayList<>();
            options.add(correctAnswer);

            List<Sign> wrongAnswerPool = new ArrayList<>(categorySigns);
            wrongAnswerPool.removeIf(sign -> sign.word.equals(correctAnswer));
            Collections.shuffle(wrongAnswerPool);

            for (int j = 0; j < 3 && j < wrongAnswerPool.size(); j++) {
                options.add(wrongAnswerPool.get(j).word);
            }
            Collections.shuffle(options);
            mixedQuestions.add(new QuizQuestion(gifUrl, correctAnswer, options));
        }

        // Buat 5 soal terbalik (tebak GIF), gunakan data sisa agar tidak sama
        for (int i = standardQuestionsCount; i < totalQuestions && i < availableSigns; i++) {
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
            mixedQuestions.add(new ReverseQuizQuestion(correctAnswer, options));
        }

        // Poin Kunci: Acak daftar gabungan untuk urutan yang tidak terduga
        Collections.shuffle(mixedQuestions);

        return mixedQuestions;
    }

    public static List<IQuizQuestion> generateMixedVocabularyQuiz(SignDao signDao, int totalQuestions) {
        // Kategori sudah pasti "KOSAKATA"
        List<Sign> categorySigns = signDao.getSignsByCategory("KOSAKATA");
        if (categorySigns.size() < 4) {
            return new ArrayList<>(); // Tidak cukup data
        }
        Collections.shuffle(categorySigns);

        List<IQuizQuestion> mixedQuestions = new ArrayList<>();
        int standardQuestionsCount = totalQuestions / 2;

        int availableSigns = categorySigns.size();

        // Buat 5 soal standar (Tebak Kata dari GIF)
        for (int i = 0; i < standardQuestionsCount && i < availableSigns; i++) {
            Sign currentSign = categorySigns.get(i);
            String correctAnswer = currentSign.word;
            String gifUrl = currentSign.gifUrl;

            List<String> options = new ArrayList<>();
            options.add(correctAnswer);

            List<Sign> wrongAnswerPool = new ArrayList<>(categorySigns);
            wrongAnswerPool.removeIf(sign -> sign.word.equals(correctAnswer));
            Collections.shuffle(wrongAnswerPool);

            for (int j = 0; j < 3 && j < wrongAnswerPool.size(); j++) {
                options.add(wrongAnswerPool.get(j).word);
            }
            Collections.shuffle(options);
            mixedQuestions.add(new QuizQuestion(gifUrl, correctAnswer, options));
        }

        // Buat 5 soal terbalik (Tebak GIF dari Kata)
        for (int i = standardQuestionsCount; i < totalQuestions && i < availableSigns; i++) {
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
            mixedQuestions.add(new ReverseQuizQuestion(correctAnswer, options));
        }

        // Acak daftar gabungan untuk urutan yang tidak terduga
        Collections.shuffle(mixedQuestions);

        return mixedQuestions;
    }
}