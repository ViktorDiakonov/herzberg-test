package com.example.herzberg.model;

public class ScoreCalculator {
    public static int calculatePercentage(int actualScore, int maxScore) {
        int minScore = maxScore / 5; // т.к. 5 - максимальный балл за вопрос
        if (actualScore <= minScore) return 0;
        if (actualScore >= maxScore) return 100;

        return (int) Math.round(((double)(actualScore - minScore) / (maxScore - minScore)) * 100);
    }
}
