// OAnQuanLogic.java
package logic;

import model.Cell;
import model.GameBoard;
import model.Player;

public class OAnQuanLogic {
    private GameBoard board;
    private Player[] players;
    private int currentPlayerIndex;
    private int numberOfBigStones;
    public enum Direction {
        CLOCKWISE,
        COUNTERCLOCKWISE
    }
    
    public OAnQuanLogic() {
        board = new GameBoard();
        players = new Player[]{
            new Player(1),
            new Player(2)
        };
        currentPlayerIndex = 0;
        numberOfBigStones = 2;
    }

    public boolean makeMove(int startCell, Direction direction) {
        // Kiểm tra tính hợp lệ của nước đi theo người chơi
        if ((currentPlayerIndex == 0 && (startCell < 1 || startCell > 5)) || 
            (currentPlayerIndex == 1 && (startCell < 7 || startCell > 11))) {
            System.out.println("Bạn chỉ được chọn ô thuộc phạm vi của mình!");
            return true;
        }
        
        // Kiểm tra xem những ô của người chơi còn quân không
        if (isPlayerCellsEmpty()) {
             
            addStonesFromScore();
        }

        // Kiểm tra ô quan và ô không có sỏi
        if ((startCell == 0 || startCell == 6) || board.getCell(startCell).getStones() == 0) {
            return true;
        }
        
        int currentCell = distributeStones(startCell, direction);
       
        // Kiểm tra kết thúc game
        if (isGameOver()) {
            collectRemainingStones();
            return false;
        }

        // Chuyển lượt người chơi
        switchPlayer();
        return true;
    }

    private int distributeStones(int startCell, Direction direction) {
        // Lấy tất cả sỏi từ ô bắt đầu
        
        int currentCell = startCell;
        int currentCell1 = startCell;
        while (true) {
            // Lấy tất cả sỏi từ ô bắt đầu/ô hiện tại
            int stones = board.getCell(currentCell).removeAllStones();

            // Rải sỏi
            while (stones > 0) {
            	currentCell = getNextCell(currentCell, direction);
            	currentCell1 = getNextCell(currentCell1, direction);
                board.getCell(currentCell).addStone();
                stones--;
            }
            System.out.println(currentCell);
            System.out.println(currentCell1);
            // Kiểm tra và thực hiện ăn quân
            while (canCapture(currentCell1, direction)) {
                 capture(currentCell1, direction);
                 currentCell1 = getNextCell(currentCell1, direction);
                 currentCell1 = getNextCell(currentCell1, direction);
            }
           
            currentCell = getNextCell(currentCell, direction);
            currentCell1 = getNextCell(currentCell1, direction);
            if (currentCell == 0 || currentCell == 6 || board.getCell(currentCell).getStones() == 0) {
                break;
            }
        }

        return currentCell;
    }
    
    private boolean isPlayerCellsEmpty() {
        int start = (currentPlayerIndex == 0) ? 1 : 7;
        int end = (currentPlayerIndex == 0) ? 5 : 11;
        
        for (int i = start; i <= end; i++) {
            if (board.getCell(i).getStones() > 0) {
                return false;
            }
        }
        return true;
    }
    
    private void addStonesFromScore() {
        Player currentPlayer = players[currentPlayerIndex];
        int stonesToAdd = Math.min(5, currentPlayer.getScore());
        
        if (stonesToAdd > 0) {
            // Trừ điểm số
            currentPlayer.removeScore(stonesToAdd);
           
        int end1 =0+stonesToAdd;
        int end2 =6+stonesToAdd;
            // Thêm vào các ô 
            int start = (currentPlayerIndex == 0) ? 1 : 7;
            int end = (currentPlayerIndex == 0) ? end1 : end2;
            for (int i = start; i <= end; i++) {
            	board.getCell(i).addStone();
            }
            
            System.out.println("Đã lấy " + stonesToAdd + " quân từ điểm số để chơi.");
        }
    }

    private int getNextCell(int current, Direction direction) {
        if (direction == Direction.CLOCKWISE) {
            return (current + 1) % GameBoard.getBoardSize();
        } else {
            return (current - 1 + GameBoard.getBoardSize()) % GameBoard.getBoardSize();
        }
    }

    private boolean canCapture(int currentCell, Direction direction) {
        int nextCell = getNextCell(currentCell, direction);
        int nextNextCell = getNextCell(nextCell, direction);

        return board.getCell(nextCell).getStones() == 0 && 
               (board.getCell(nextNextCell).getStones() > 0 || 
                board.getCell(nextNextCell).isBigStoneCell());
    }

    private void capture(int currentCell, Direction direction) {
    	
        int nextCell = getNextCell(currentCell, direction);
        int nextNextCell = getNextCell(nextCell, direction);
        Cell targetCell = board.getCell(nextNextCell);
        
        if (targetCell.isBigStoneCell()) {
            players[currentPlayerIndex].addScore(10);
            numberOfBigStones--;
            targetCell.removeBigStones();
        }

        int capturedStones = targetCell.removeAllStones();
        players[currentPlayerIndex].addScore(capturedStones);

//        return nextNextCell;
    }

    private boolean isGameOver() {
        return numberOfBigStones == 0 &&
               board.getCell(0).getStones() == 0 && 
               board.getCell(6).getStones() == 0;
    }

    private void collectRemainingStones() {
        // Player 1's stones
        for (int i = 1; i < 6; i++) {
            players[0].addScore(board.getCell(i).removeAllStones());
        }
        // Player 2's stones
        for (int i = 7; i < 12; i++) {
            players[1].addScore(board.getCell(i).removeAllStones());
        }
    }

    public void switchPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % 2;
    }

    public Player getCurrentPlayer() {
        return players[currentPlayerIndex];
    }

    public Player getPlayer(int playerNumber) {
        return players[playerNumber - 1];
    }

    public GameBoard getBoard() {
        return board;
    }

    public int getNumberOfBigStones() {
        return numberOfBigStones;
    }
}