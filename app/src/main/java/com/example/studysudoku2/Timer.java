package com.example.studysudoku2;

import android.os.Handler;

public class Timer {
    private int totalSeconds; // 총 남은 시간 (초 단위)
    private final Handler handler = new Handler();
    private TimerListener listener;

    // 인터페이스를 통해 타이머 상태를 전달
    public interface TimerListener {
        void onTick(String time); // 매 초마다 호출
        void onFinish(); // 타이머 종료 시 호출
    }

    public Timer(int minutes, int seconds, TimerListener listener) {
        this.totalSeconds = minutes * 60 + seconds;
        this.listener = listener;
    }

    public void start() {
        handler.post(tickRunnable);
    }

    public void stop() {
        handler.removeCallbacks(tickRunnable);
    }

    private final Runnable tickRunnable = new Runnable() {
        @Override
        public void run() {
            if (totalSeconds > 0) {
                totalSeconds--;

                // 시간 포맷: MM:SS
                int minutes = totalSeconds / 60;
                int seconds = totalSeconds % 60;
                String timeFormatted = String.format("%02d:%02d", minutes, seconds);

                if (listener != null) {
                    listener.onTick(timeFormatted);
                }

                // 1초 후 다시 실행
                handler.postDelayed(this, 1000);
            } else {
                if (listener != null) {
                    listener.onFinish();
                }
            }
        }
    };
}
