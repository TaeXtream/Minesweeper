package minesweeper;

import java.util.Scanner;

public class Game {
    private final Board board;
    Scanner scanner = new Scanner(System.in);


    public Game() {
        System.out.print("How many mines do you want on the field? > ");
        int minesCount = scanner.nextInt();
        this.board = new Board(minesCount);
        System.out.println(board);
    }

    public void play() {
        while (!board.isAllMineMarked() && !board.isBoardExplode() && !board.isAllExplored()) {
            System.out.print("Set/delete mines marks (x and y coordinates): > ");
            int x = scanner.nextInt() - 1;
            int y = scanner.nextInt() - 1;
            String command = scanner.next();
            if (command.equals("mine")) {
                board.markCell(x, y);
            }
            else if (command.equals("free")) {
                board.freeCell(x, y);
            }
            else {
                System.out.println("Unknown Command!");
            }
        }
        if (board.isAllMineMarked() || board.isAllExplored()) {
            System.out.println("Congratulations! You found all mines!");
        }
        else if (board.isBoardExplode()) {
            System.out.println("You stepped on a mine and failed!");
        }
        scanner.close();
    }
}
