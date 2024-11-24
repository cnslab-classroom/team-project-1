package com.example.studysudoku2;

public class ScoreCalculator {
    private static final int BASE_SCORE = 1000; // 기본 점수
    private static final int TIME_PENALTY = 1; // 초당 감점
    private static final int HINT_PENALTY = 50; // 힌트 사용 시 감점

    public static int calculateScore(int elapsedTime, int hintsUsed) {
        int timePenalty = elapsedTime * TIME_PENALTY;
        int hintPenalty = hintsUsed * HINT_PENALTY;
        int finalScore = BASE_SCORE - timePenalty - hintPenalty;

        // 점수는 최소 0으로 설정
        return Math.max(finalScore, 0);
    }
}
