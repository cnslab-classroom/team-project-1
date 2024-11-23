public class SudokuValidator {

    public static boolean isValidMove(int[][] board, int row, int col, int value) {
        // 행, 열, 3x3 박스에 중복 값이 없는지 체크
        return !isInRow(board, row, value) && !isInCol(board, col, value) && !isInBox(board, row, col, value);
    }

    private static boolean isInRow(int[][] board, int row, int value) {
        for (int i = 0; i < 9; i++) {
            if (board[row][i] == value) {
                return true;
            }
        }
        return false;
    }

    private static boolean isInCol(int[][] board, int col, int value) {
        for (int i = 0; i < 9; i++) {
            if (board[i][col] == value) {
                return true;
            }
        }
        return false;
    }

    private static boolean isInBox(int[][] board, int row, int col, int value) {
        int boxRow = (row / 3) * 3;
        int boxCol = (col / 3) * 3;
        for (int i = boxRow; i < boxRow + 3; i++) {
            for (int j = boxCol; j < boxCol + 3; j++) {
                if (board[i][j] == value) {
                    return true;
                }
            }
        }
        return false;
    }
}
