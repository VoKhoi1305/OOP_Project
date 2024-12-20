// GameConsole.java
package application;

import java.util.Scanner;
import logic.OAnQuanLogic;
import logic.OAnQuanLogic.Direction;


public class GameConsole {
    private static OAnQuanLogic game;
    private static Scanner scanner;

    public static void main(String[] args) {
        game = new OAnQuanLogic();
        scanner = new Scanner(System.in);
        boolean gameRunning = true;

        printWelcomeMessage();

        while (gameRunning) {
            // Hiển thị trạng thái game
            printGameState();
            
            // Kiểm tra xem các ô của người chơi hiện tại có trống không
            if (isPlayerCellsEmpty()) {
                System.out.println("Các ô của bạn đã hết quân!");
                System.out.println("Bạn sẽ phải sử dụng điểm số để lấy quân.");
                
                // Thông báo số điểm của người chơi
                int currentPlayer = game.getCurrentPlayer().getNumber();
                int playerScore = game.getPlayer(currentPlayer).getScore();
                System.out.println("Điểm số của bạn: " + playerScore);
                
                // Nếu không có điểm số để lấy quân
                if (playerScore == 0) {
                    System.out.println("Bạn không còn điểm để lấy quân. Lượt của bạn bị bỏ qua.");
                    game.switchPlayer(); // Chuyển lượt
                    continue; // Bắt đầu vòng lặp mới
                }
            }
            
            // Lấy nước đi từ người chơi
            int cell = getValidCellInput();
            Direction direction = getDirectionInput();

            // Thực hiện nước đi
            gameRunning = game.makeMove(cell, direction);
        }

        announceWinner();
        scanner.close();
    }

    
    private static boolean isPlayerCellsEmpty() {
        int currentPlayer = game.getCurrentPlayer().getNumber();
        int start = (currentPlayer == 1) ? 1 : 7;
        int end = (currentPlayer == 1) ? 5 : 11;
        
        for (int i = start; i <= end; i++) {
            if (game.getBoard().getCell(i).getStones() > 0) {
                return false;
            }
        }
        return true;
    }
    
    private static void printWelcomeMessage() {
        System.out.println("Chào mừng đến với game Ô Ăn Quan!");
        System.out.println("Ô số 0 và 6 là ô quan (không được chọn)");
        System.out.println("Mỗi ô quan có 10 điểm");
    }

    private static void printGameState() {
        System.out.println("\nTrạng thái bàn cờ:");
        printBoard();
        
        int currentPlayer = game.getCurrentPlayer().getNumber();
        System.out.println("Lượt người chơi " + currentPlayer);
        System.out.println("Điểm người chơi 1: " + game.getPlayer(1).getScore());
        System.out.println("Điểm người chơi 2: " + game.getPlayer(2).getScore());
        System.out.println("Số quan còn lại: " + game.getNumberOfBigStones());
    }

    private static void printBoard() {
        System.out.println("  11  10   9   8   7   6");
        System.out.print("[");
        for (int i = 11; i >= 6; i--) {
            printCell(i);
        }
        System.out.println("]");
        
        System.out.print("[");
        for (int i = 0; i <= 5; i++) {
            printCell(i);
        }
        System.out.println("]");
        System.out.println("   0   1   2   3   4   5");
    }

    private static void printCell(int i) {
        String cell = game.getBoard().getCell(i).isBigStoneCell() ? 
                     "Q" + game.getBoard().getCell(i).getStones() :
                     String.format("%2d", game.getBoard().getCell(i).getStones());
        System.out.print(cell + " ");
    }

    private static int getValidCellInput() {
        int currentPlayer = game.getCurrentPlayer().getNumber();
        int cell;
        while (true) {
            System.out.print("Người chơi " + currentPlayer + " chọn ô: ");
            cell = scanner.nextInt();

            // Kiểm tra ô quan
            if (cell == 0 || cell == 6) {
                System.out.println("Không thể chọn ô quan! Vui lòng chọn ô khác.");
                continue;
            }

            // Kiểm tra ô phù hợp với người chơi
            if (currentPlayer == 1 && (cell < 1 || cell > 5)) {
                System.out.println("Người chơi 1 chỉ được chọn các ô từ 1 đến 5!");
                continue;
            }

            if (currentPlayer == 2 && (cell < 7 || cell > 11)) {
                System.out.println("Người chơi 2 chỉ được chọn các ô từ 7 đến 11!");
                continue;
            }

            // Kiểm tra ô có sỏi
            
            if (isPlayerCellsEmpty()) {
            	printBoard();
                // Nếu các ô đều rỗng, cho phép chọn ô để lấy quân từ điểm số
                return cell;
            }

            // Kiểm tra ô có sỏi
            if (game.getBoard().getCell(cell).getStones() == 0) {
                 System.out.println("Ô này không còn sỏi! Vui lòng chọn ô khác.");
                continue;
            }
            
     
            // Nếu vượt qua tất cả các kiểm tra, trả về ô hợp lệ
            return cell;
        }
    }

    private static Direction getDirectionInput() {
        System.out.print("Chọn hướng (1: Theo chiều kim đồng hồ, 2: Ngược chiều): ");
        int directionChoice = scanner.nextInt();
        return (directionChoice == 1) ? Direction.CLOCKWISE : Direction.COUNTERCLOCKWISE;
    }

    private static void announceWinner() {
        System.out.println("\nGame kết thúc!");
        System.out.println("Điểm cuối cùng:");
        System.out.println("Người chơi 1: " + game.getPlayer(1).getScore());
        System.out.println("Người chơi 2: " + game.getPlayer(2).getScore());
        
        if (game.getPlayer(1).getScore() > game.getPlayer(2).getScore()) {
            System.out.println("Người chơi 1 thắng!");
        } else if (game.getPlayer(1).getScore() < game.getPlayer(2).getScore()) {
            System.out.println("Người chơi 2 thắng!");
        } else {
            System.out.println("Hòa!");
        }
    }
}