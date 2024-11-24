package com.example.studysudoku2;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Handler;

public class MainActivity extends AppCompatActivity implements Timer.TimerListener {

    private class Cell {
        private int value;
        private boolean fixed;
        private Button button;

        public Cell(int intvalue, Context context) {
            value = intvalue;
            if (value != 0) {
                fixed = true;
            } else {
                fixed = false;
            }

            button = new Button(context);
            if (fixed) {
                button.setText(String.valueOf(value));
                button.setEnabled(false);
                button.setBackgroundColor(Color.WHITE);
                button.setTextColor(Color.rgb(78, 89, 104));
            } else {
                button.setTextColor(Color.rgb(27, 100, 218));
                button.setBackgroundColor(Color.rgb(192, 217, 254));
            }

            button.setOnClickListener(view -> {
                if (fixed) {
                    return;
                }
                value++;
                if (value > 9) {
                    value = 1;
                }
                button.setText(String.valueOf(value));
                if (!SudokuValidator.isValidMove(board, getRow(), getCol(), value)) {
                    button.setBackgroundColor(Color.RED);
                } else {
                    button.setBackgroundColor(Color.rgb(192, 217, 254));
                }
            });
        }

        public int getRow() {
            return ((GridLayout.LayoutParams) button.getLayoutParams()).rowSpec.getIndex();
        }

        public int getCol() {
            return ((GridLayout.LayoutParams) button.getLayoutParams()).columnSpec.getIndex();
        }
    }

    private Cell[][] table;
    private String input;
    private GridLayout layout;
    private Timer timer; // 타이머 인스턴스
    private int hintCount = 0; // 힌트 사용 횟수

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input = "3 8 ? 7 5 4 2 1 9 " +
                "7 ? 4 5 1 2 6 9 3 " +
                "2 1 6 3 9 8 7 5 4 " +
                "5 7 3 4 8 9 1 2 6 " +
                "9 4 1 2 ? 6 5 ? 8 " +
                "? 6 2 1 5 3 9 4 7 " +
                "? ? 8 9 2 5 4 7 1 " +
                "1 5 9 8 4 7 ? 6 2 " +
                "4 2 7 6 3 1 8 9 5 ";

        String[] split = input.split(" ");
        board = SudokuGenerator.generatePuzzle(2); // 난이도 2로 퍼즐 생성

        table = new Cell[9][9];
        layout = new GridLayout(this);
        layout.setColumnCount(9);
        layout.setRowCount(9);

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                String s = split[i * 9 + j];
                char c = s.charAt(0);
                table[i][j] = new Cell(c == '?' ? 0 : c - '0', this);

                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 100;  // 셀 크기
                params.height = 100;
                params.rowSpec = GridLayout.spec(i);
                params.columnSpec = GridLayout.spec(j);

                layout.addView(table[i][j].button, params);
            }
        }

        setContentView(layout);

        // 타이머 시작
        timer = new Timer(this);
        timer.start();

        // 게임 종료 시점에 점수 계산
        Button endGameButton = findViewById(R.id.endGameButton); // 게임 종료 버튼
        endGameButton.setOnClickListener(v -> {
            timer.stop();
            int finalScore = ScoreCalculator.calculateScore(timer.getElapsedTime(), hintCount);
            Toast.makeText(MainActivity.this, "최종 점수: " + finalScore, Toast.LENGTH_LONG).show();
        });
    }

    @Override
    public void onTick(String time) {
        // 타이머 값이 바뀔 때마다 호출되어 화면에 표시
        // 예를 들어 TextView에 타이머 표시
        TextView timerTextView = findViewById(R.id.timerTextView);
        timerTextView.setText(time);
    }

    // 힌트 사용
    public void onHintUsed() {
        hintCount++; // 힌트 사용 횟수 증가
        String hint = SudokuHelper.provideHint(board);
        Toast.makeText(this, hint, Toast.LENGTH_SHORT).show();
    }
}
