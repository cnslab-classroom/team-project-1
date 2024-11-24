package com.example.studysudoku2;

import org.junit.Test;
import static org.junit.Assert.*;

public class TimerTest {

    @Test
    public void testTimerStart() {
        Timer timer = new Timer(null);
        timer.start();
        assertTrue(timer.isRunning());  // 타이머가 실행 중인지 확인하는 가정된 메서드
    }

    @Test
    public void testTimerStop() {
        Timer timer = new Timer(null);
        timer.start();
        timer.stop();
        assertFalse(timer.isRunning());  // 타이머가 멈췄는지 확인
    }

    @Test
    public void testTimerElapsedTime() {
        Timer timer = new Timer(null);
        timer.start();
        try {
            Thread.sleep(2000);  // 2초 대기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        timer.stop();
        assertTrue(timer.getElapsedTime() > 1);  // 1초 이상 경과했는지 확인
    }
}
