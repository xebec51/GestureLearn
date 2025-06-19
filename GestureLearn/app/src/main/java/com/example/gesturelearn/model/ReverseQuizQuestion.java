package com.example.gesturelearn.model;

import java.util.List;

public class ReverseQuizQuestion {
    private final Sign correctAnswer;
    private final List<Sign> options;

    public ReverseQuizQuestion(Sign correctAnswer, List<Sign> options) {
        this.correctAnswer = correctAnswer;
        this.options = options;
    }

    public Sign getCorrectAnswer() {
        return correctAnswer;
    }

    public List<Sign> getOptions() {
        return options;
    }
}