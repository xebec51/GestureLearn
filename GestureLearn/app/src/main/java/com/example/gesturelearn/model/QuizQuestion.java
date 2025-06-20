package com.example.gesturelearn.model;

import java.util.List;

public class QuizQuestion implements IQuizQuestion {
    private final String gifUrl;
    private final String correctAnswer;
    private final List<String> options;

    public QuizQuestion(String gifUrl, String correctAnswer, List<String> options) {
        this.gifUrl = gifUrl;
        this.correctAnswer = correctAnswer;
        this.options = options;
    }

    public String getGifUrl() {
        return gifUrl;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public List<String> getOptions() {
        return options;
    }

    @Override
    public String getQuestionType() {
        return "STANDARD";
    }
}