package backend.pieces;

import java.awt.Color;
import java.util.ArrayList;

import backend.Board;
import backend.Field;
import backend.Piece;

public class King extends Piece {

    public King(Color color, Board board) {
        super("K", color, board);
    }

    @Override
    public Field[] getReachableFields() {
        ArrayList<Field> reachableFields = new ArrayList<>();
        int currentRow = this.getField().row;
        int currentColumn = this.getField().column;

        // Alle möglichen Bewegungen eines Königs
        int[][] moves = {
            {0, 1},   // Rechts
            {0, -1},  // Links
            {1, 0},   // Unten
            {-1, 0},  // Oben
            {1, 1},   // Diagonal rechts unten
            {-1, -1}, // Diagonal links oben
            {1, -1},  // Diagonal links unten
            {-1, 1}   // Diagonal rechts oben
        };

        for (int[] move : moves) {
            int newRow = currentRow + move[0];
            int newCol = currentColumn + move[1];

            // Prüfen, ob das neue Feld innerhalb des Schachbretts liegt
            if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) {
                Field nextField = board.board[newRow][newCol];

                if (nextField.onField == null) {
                    // Feld ist leer, König kann sich hierhin bewegen
                    reachableFields.add(nextField);
                } else if (nextField.onField.getColor() != this.getColor()) {
                    // Gegnerische Figur: König kann das Feld betreten
                    reachableFields.add(nextField);
                }
                // Eigene Figuren blockieren, daher keine zusätzliche Logik notwendig
            }
        }

        return reachableFields.toArray(new Field[0]);
    }
}
