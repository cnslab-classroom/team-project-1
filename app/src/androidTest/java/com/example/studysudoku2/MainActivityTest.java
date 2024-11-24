package com.example.studysudoku2;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.runner.AndroidJUnit4;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = 
        new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testFinishButton() {
        // 게임 종료 버튼 클릭
        Espresso.onView(ViewMatchers.withId(R.id.finishButton)).perform(ViewActions.click());

        // 타이머가 멈추고 점수가 표시되는지 확인
        Espresso.onView(ViewMatchers.withText("최종 점수:")).check(matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testTimerUpdate() {
        // 타이머가 1초 후 업데이트 되는지 확인
        Espresso.onView(ViewMatchers.withId(R.id.timerTextView))
                .check(matches(ViewMatchers.withText("00:00")));  // 초기 값 확인

        // 1초 대기 후 타이머 값 확인
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Espresso.onView(ViewMatchers.withId(R.id.timerTextView))
                .check(matches(ViewMatchers.withText("00:01")));  // 1초 후
    }
}
