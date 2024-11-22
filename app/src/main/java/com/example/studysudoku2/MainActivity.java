package com.example.studysudoku2;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.lang.ref.Cleaner;

public class MainActivity extends AppCompatActivity {

    private class Cell{
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
            }
            else{
                //비어 있는 셀 설정
                button.setText("");
                button.setTextColor(Color.rgb(27, 100, 218));
                button.setBackgroundColor(Color.rgb(192, 217, 254));
            }

            button.setOnClickListener(view -> {
                if (fixed){
                    return;
                }
                selectedCell = this; //클릭한 셀 기억
            });
        }

        public void setValue(int newValue){
            value = newValue;
            button.setText(value == 0 ? "" : String.valueOf(value));
        }
    }

    private Cell selectedCell = null;

    //숫자 패드
    private GridLayout createNumberPad(Context context){
        //숫자 버튼 생성
        GridLayout numberPad = new GridLayout(context);
        numberPad.setColumnCount(3);
        numberPad.setRowCount(4);

        //디스플레이 크기 기준으로 버튼 크기 계산
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int buttonSize = metrics.widthPixels / 6;

        for (int i = 1; i <= 9; i++){
            final int number = i;

            Button numberButton = new Button(context);

            numberButton.setText(String.valueOf(number));
            numberButton.setLayoutParams(new GridLayout.LayoutParams());
            numberButton.setWidth(buttonSize);
            numberButton.setHeight(buttonSize);

            numberButton.setOnClickListener(v -> {
                if (selectedCell != null){
                    selectedCell.setValue(number);  //셀 선택 후 값 입력
                }
            });

            numberPad.addView(numberButton);
        }

        //초기화 버튼
        Button clearButton = new Button(context);

        clearButton.setText("delete");
        clearButton.setLayoutParams(new GridLayout.LayoutParams());
        clearButton.setWidth(buttonSize);
        clearButton.setHeight(buttonSize);

        clearButton.setOnClickListener(v -> {
            if (selectedCell != null){
                selectedCell.setValue(0); //선택된 셀 초기화
            }
        });

        numberPad.addView(clearButton);

        return numberPad;
    }

    //2차원 배열 생성
    Cell[][] table;
    String input;
    GridLayout boardLayout;
    GridLayout numberPadLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        //setContentView(R.layout.activity_main);
        //ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
        //    Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
        //    v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
        //    return insets;
        //});

        //수직의 메인 레이아웃
        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);

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

        String[] split = input.split(" "); //공백

        //9x9의 배열로 게임 보드 초기화
        table = new Cell[9][9];
        boardLayout = new GridLayout(this);
        boardLayout.setColumnCount(9);
        boardLayout.setRowCount(9);

        // 디스플레이 화면 크기 계산, 비율 조정
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenWidth = metrics.widthPixels;
        int cellSize = screenWidth / 9;

        for (int i = 0; i < 9; i++){
            for (int j = 0; j < 9; j++) {
                String s = split[i * 9 + j];
                char c = s.charAt(0);
                //c가 물음표면 0으로, 물음표가 아니라면 정수로 변환
                table[i][j] = new Cell(c == '?' ? 0 : c - '0', this);

                //셀의 레이아웃 수치 설정
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = cellSize; //정사각형 크기로 설정
                params.height = cellSize;
                params.rowSpec = GridLayout.spec(i);
                params.columnSpec = GridLayout.spec(j);

                boardLayout.addView(table[i][j].button, params);
            }
        }

        numberPadLayout = createNumberPad(this);
        mainLayout.addView(boardLayout);
        mainLayout.addView(numberPadLayout);

        //셀들이 화면에 모두 보이도록 설정
        setContentView(mainLayout);
    }
}
