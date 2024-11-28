package com.example.studysudoku2;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.lang.ref.Cleaner;

public class MainActivity extends AppCompatActivity {

    private class Cell {
        private int value;
        private boolean fixed;
        private Button button;

        public Cell(int intvalue, Context context) {
            //셀이 채워졌는지 확인
            value = intvalue;
            if (value != 0) {
                fixed = true;
            } else {
                fixed = false;
            }

            button = new Button(context);
            if (fixed) {
                //이미 채워져 있는 셀
                button.setText(String.valueOf(value));
                button.setEnabled(false);

                button.setBackgroundColor(Color.WHITE);
                button.setTextColor(Color.rgb(78, 89, 104));
            } else {
                //비어 있는 셀 설정
                button.setText("");
                button.setBackgroundColor(Color.WHITE);
                button.setTextColor(Color.rgb(27, 100, 218));
                button.setStateListAnimator(null);
            }

            button.setOnClickListener(view -> {
                if (fixed) {
                    return;
                }

                if (selectedCell != null && selectedCell != this) {
                    selectedCell.button.setBackgroundColor(Color.WHITE);
                }

                selectedCell = this; //클릭한 셀 기억
                button.setBackgroundColor(Color.rgb(192, 217, 254));

            });
        }

        public void setValue(int newValue) {
            value = newValue;
            button.setText(value == 0 ? "" : String.valueOf(value));
        }
    }

    private Toolbar headerLayout; //헤더
    private Cell selectedCell = null; //선택된 셀
    private Cell[][] table; //게임 보드 2차원 배열
    private String input; //스도쿠 입력
    private FrameLayout boardLayout; //게임 보드 레이아웃
    private GridLayout numberPadLayout; //숫자 패드 레이아웃

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //수직의 메인 레이아웃
        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);

        //헤더 레이아웃
        headerLayout = createHeader("Sudoku Game 1");
        LinearLayout.LayoutParams headerParams
                = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        headerLayout.setLayoutParams(headerParams);

        //게임 보드 레이아웃
        boardLayout = createGameBoard();
        LinearLayout.LayoutParams boardParams
                = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
        boardParams.weight = 9; //화면의 9/17
        boardLayout.setLayoutParams(boardParams);

        //숫자 패드 레이아웃
        numberPadLayout = createNumberPad();
        LinearLayout.LayoutParams padParams
                = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
        padParams.weight = 8; //화면의 8/17
        numberPadLayout.setLayoutParams(padParams);

        //메인 레이아웃
        mainLayout.addView(headerLayout);
        mainLayout.addView(boardLayout);
        mainLayout.addView(numberPadLayout);

        setContentView(mainLayout);
    }

    //헤더 생성
    private Toolbar createHeader(String title){
        Toolbar toolbar = new Toolbar(this);
        toolbar.setTitle(title);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setBackgroundColor(Color.rgb(49, 130, 246));

        return toolbar;
    }

    //게임 보드 생성
    private FrameLayout createGameBoard(){
        //게임 보드 레이아웃
        FrameLayout gameBoardLayout = new FrameLayout(this);
        //gameBoardLayout.setOrientation(LinearLayout.VERTICAL);

        //게임 보드 스트로크
        GameBoardView gameBoardView = new GameBoardView(this);
        FrameLayout.LayoutParams gameBoardViewParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        gameBoardView.setLayoutParams(gameBoardViewParams);

        //9x9의 배열로 게임 보드 초기화
        GridLayout gridBoard = new GridLayout(this);
        gridBoard.setColumnCount(9);
        gridBoard.setRowCount(9);

        //디스플레이 화면 크기 계산, 비율 조정
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenWidth = metrics.widthPixels;
        int cellSize = screenWidth / 9;

        //임의의 스도쿠 입력
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

        table = new Cell[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                String s = split[i * 9 + j];
                char c = s.charAt(0);

                //셀 생성
                //c가 물음표면 0으로, 물음표가 아니라면 정수로 변환
                table[i][j] = new Cell(c == '?' ? 0 : c - '0', this);

                //셀의 레이아웃 수치 설정
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = cellSize; //정사각형 크기로 설정
                params.height = cellSize;
                params.rowSpec = GridLayout.spec(i);
                params.columnSpec = GridLayout.spec(j);

                gridBoard.addView(table[i][j].button, params);
            }
        }

        //GameBoardView의 크기를 GridLayout과 동일하게 설정
        FrameLayout.LayoutParams gridBoardParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        gridBoard.setLayoutParams(gridBoardParams);

        gridBoard.setBackgroundColor(Color.TRANSPARENT); // GridLayout 배경 설정

        //게임 보드 레이아웃에 추가
        gameBoardLayout.addView(gridBoard); // 실제 GridLayout 추가
        gameBoardLayout.addView(gameBoardView); // 스트로크 그리기

        gameBoardView.invalidate();

        return gameBoardLayout;
    }

    //숫자 패드 생성
    private GridLayout createNumberPad() {
        GridLayout numberPad = new GridLayout(this);
        numberPad.setColumnCount(4);
        numberPad.setRowCount(3);

        //디스플레이 크기 기준으로 버튼 크기 계산
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int buttonSize = metrics.widthPixels / 4;

        //숫자 버튼 생성
        for (int i = 1; i <= 9; i++) {
            final int number = i;

            Button numberButton = new Button(this);
            numberButton.setText(String.valueOf(number));

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = buttonSize;
            params.height = buttonSize;

            //숫자 위치 설정
            params.rowSpec = GridLayout.spec((i - 1) / 3);
            params.columnSpec = GridLayout.spec((i - 1) % 3);

            numberButton.setLayoutParams(params);
            numberButton.setOnClickListener(v -> {
                if (selectedCell != null) {
                    selectedCell.setValue(number);  //셀 선택 후 값 입력
                }
            });

            numberPad.addView(numberButton);
        }

        //지우기 버튼
        Button clearButton = new Button(this);
        clearButton.setText("X");

        GridLayout.LayoutParams deleteParams = new GridLayout.LayoutParams();
        deleteParams.width = buttonSize;
        deleteParams.height = buttonSize;

        //지우기 버튼 위치 설정
        deleteParams.rowSpec = GridLayout.spec(0);
        deleteParams.columnSpec = GridLayout.spec(3);
        clearButton.setLayoutParams(deleteParams);

        clearButton.setOnClickListener(v -> {
            if (selectedCell != null) {
                selectedCell.setValue(0); //선택된 셀 초기화
            }
        });

        numberPad.addView(clearButton);

        //숫자패드의 빈 공간
        for (int i = 0; i < 2; i++) {
            View emptySpace = new View(this);

            GridLayout.LayoutParams emptyParams = new GridLayout.LayoutParams();
            emptyParams.width = buttonSize;
            emptyParams.height = buttonSize;

            //빈 공간 위치 설정
            emptyParams.rowSpec = GridLayout.spec(2);
            emptyParams.columnSpec = GridLayout.spec(2 + i);
            emptySpace.setLayoutParams(emptyParams);

            numberPad.addView(emptySpace);
        }

        return numberPad;
    }
}