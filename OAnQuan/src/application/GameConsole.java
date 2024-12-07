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
            printGameState();
            
            // Lấy nước đi từ người chơi
            int cell = getValidCellInput();
            Direction direction = getDirectionInput();

            // Thực hiện nước đi
            gameRunning = game.makeMove(cell, direction);
        }

        announceWinner();
        scanner.close();
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
        int cell;
        do {
            System.out.print("Chọn ô (1-5 hoặc 7-11): ");
            cell = scanner.nextInt();
            if (cell == 0 || cell == 6) {
                System.out.println("Không thể chọn ô quan! Vui lòng chọn ô khác.");
            } else if (game.getBoard().getCell(cell).getStones() == 0) {
                System.out.println("Ô này không còn sỏi! Vui lòng chọn ô khác.");
            }
        } while (cell == 0 || cell == 6 || game.getBoard().getCell(cell).getStones() == 0);
        return cell;
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