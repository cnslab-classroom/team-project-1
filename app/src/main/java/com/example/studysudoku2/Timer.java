package com.example.studysudoku2;

import android.os.Handler;

public class Timer {
    private int elapsedTime; // 경과 시간 (초 단위)
    private final Handler handler = new Handler();
    private boolean isRunning = false;
    private TimerListener listener;

    public interface TimerListener {
        void onTick(String time); // 매 초마다 호출
    }

    public Timer(TimerListener listener) {
        this.listener = listener;
    }

    public void start() {
        isRunning = true;
        elapsedTime = 0; // 초기화
        handler.post(tickRunnable);
    }

    public void stop() {
        isRunning = false;
        handler.removeCallbacks(tickRunnable);
    }

    public int getElapsedTime() {
        return elapsedTime; // 경과 시간 반환
    }

    private final Runnable tickRunnable = new Runnable() {
        @Override
        public void run() {
            if (isRunning) {
                elapsedTime++; // 경과 시간 증가

                // 시간 포맷: MM:SS
                int minutes = elapsedTime / 60;
                int seconds = elapsedTime % 60;
                String timeFormatted = String.format("%02d:%02d", minutes, seconds);

                if (listener != null) {
                    listener.onTick(timeFormatted);
                }

                // 1초 후 다시 실행
                handler.postDelayed(this, 1000);
            }
        }
    };
}

