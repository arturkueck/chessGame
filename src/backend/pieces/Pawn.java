package backend.pieces;

import java.awt.Color;
import java.util.ArrayList;

import backend.Board;
import backend.Field;
import backend.Piece;

public class Pawn extends Piece {

    public Pawn(Color color, Board board) {
        super("P", color, board);
    }

    @Override
    public Field[] getReachableFields() {
        ArrayList<Field> reachableFields = new ArrayList<>();
        int currentRow = this.getField().row;
        int currentColumn = this.getField().column;

        // Richtung des Bauern basierend auf der Farbe
        int direction = (this.getColor() == Color.WHITE) ? -1 : 1;

        // Normale Bewegung: Ein Feld vorwärts
        int newRow = currentRow + direction;
        if (newRow >= 0 && newRow < 8) {
            Field forwardField = board.board[newRow][currentColumn];
            if (forwardField.onField == null) {
                reachableFields.add(forwardField);

                // Zwei Felder vorwärts (nur von der Ausgangsposition)
                if ((this.getColor() == Color.WHITE && currentRow == 6) ||
                    (this.getColor() == Color.BLACK && currentRow == 1)) {
                    Field twoStepsField = board.board[newRow + direction][currentColumn];
                    if (twoStepsField.onField == null) {
                        reachableFields.add(twoStepsField);
                    }
                }
            }
        }

        // Schlagen: Diagonal links
        int newCol = currentColumn - 1;
        if (newCol >= 0 && newRow >= 0 && newRow < 8) {
            Field diagonalLeft = board.board[newRow][newCol];
            if (diagonalLeft.onField != null && diagonalLeft.onField.getColor() != this.getColor()) {
                reachableFields.add(diagonalLeft);
            }
        }

        // Schlagen: Diagonal rechts
        newCol = currentColumn + 1;
        if (newCol < 8 && newRow >= 0 && newRow < 8) {
            Field diagonalRight = board.board[newRow][newCol];
            if (diagonalRight.onField != null && diagonalRight.onField.getColor() != this.getColor()) {
                reachableFields.add(diagonalRight);
            }
        }

        return reachableFields.toArray(new Field[0]);
    }
}
