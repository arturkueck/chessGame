package frontend;

import backend.Board;

public class Main {
    public static void main(String[] args) {
        Board board = new Board();
        board.createChessGame();

        View view = new View();
        new Controller(board, view);

        view.setVisible(true);
    }
}
