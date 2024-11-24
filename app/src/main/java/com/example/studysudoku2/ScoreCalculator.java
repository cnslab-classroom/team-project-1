package com.example.studysudoku2;

public class ScoreCalculator {

    // 게임 종료 후 점수 계산
    public int calculateScore(int elapsedTime, int hintUsed) {
        // 기본 점수 1000점
        int score = 1000;

        // 경과 시간에 따른 점수 차감 (경과 시간 1초마다 1점 차감)
        score -= elapsedTime;

        // 힌트 사용 횟수에 따른 추가 차감 (힌트 1번마다 20점 차감)
        score -= hintUsed * 20;

        // 점수는 최소 0점으로 제한
        if (score < 0) {
            score = 0;
        }

        return score;
    }
}
