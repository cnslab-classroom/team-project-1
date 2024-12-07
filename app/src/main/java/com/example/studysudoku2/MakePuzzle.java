package com.example.studysudoku2;

import java.util.Random;

public class PuzzleGenerator {
    private static final int GRID_SIZE = 9;
    private int[][] board;

    public PuzzleGenerator() {
        board = new int[GRID_SIZE][GRID_SIZE];
    }

    public int[][] generateFullPuzzle() {
        fillBoard(0, 0);
        return board;
    }

    public int[][] generatePuzzle(int emptyCells) {
        generateFullPuzzle();
        Random random = new Random();
        for (int i = 0; i < emptyCells; ) {
            int row = random.nextInt(GRID_SIZE);
            int col = random.nextInt(GRID_SIZE);

            if (board[row][col] != 0) {
                board[row][col] = 0; // 빈칸 설정
                i++;
            }
        }
        return board;
    }

    private boolean fillBoard(int row, int col) {
        if (row == GRID_SIZE) return true; // 완료

        int nextRow = (col == GRID_SIZE - 1) ? row + 1 : row;
        int nextCol = (col == GRID_SIZE - 1) ? 0 : col + 1;

        if (board[row][col] != 0) return fillBoard(nextRow, nextCol);

        Random random = new Random();
        int[] nums = random.ints(1, GRID_SIZE + 1).distinct().limit(GRID_SIZE).toArray();

        for (int num : nums) {
            if (isSafe(row, col, num)) {
                board[row][col] = num;
                if (fillBoard(nextRow, nextCol)) {
                    return true;
                }
                board[row][col] = 0; // 백트래킹
            }
        }
        return false;
    }

    private boolean isSafe(int row, int col, int num) {
        for (int i = 0; i < GRID_SIZE; i++) {
            if (board[row][i] == num || board[i][col] == num) {
                return false;
            }
        }

        int boxRowStart = (row / 3) * 3;
        int boxColStart = (col / 3) * 3;

        for (int i = boxRowStart; i < boxRowStart + 3; i++) {
            for (int j = boxColStart; j < boxColStart + 3; j++) {
                if (board[i][j] == num) {
                    return false;
                }
            }
        }
        return true;
    }
}
