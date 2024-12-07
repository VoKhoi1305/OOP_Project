// GameBoard.java
package model;

public class GameBoard {
    private static final int BOARD_SIZE = 12;
    private static final int DEFAULT_STONES = 5;
    private Cell[] board;

    public GameBoard() {
        board = new Cell[BOARD_SIZE];
        initializeBoard();
    }

    private void initializeBoard() {
        // Initialize quan cells (big stone cells)
        board[0] = new Cell(0, true);  // quan
        board[6] = new Cell(0, true);  // quan

        // Initialize regular cells
        for (int i = 1; i < BOARD_SIZE; i++) {
            if (i != 6) {
                board[i] = new Cell(DEFAULT_STONES, false);
            }
        }
    }

    public Cell getCell(int index) {
        return board[index];
    }

    public static int getBoardSize() {
        return BOARD_SIZE;
    }
}