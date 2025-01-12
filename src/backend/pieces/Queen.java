package backend.pieces;

import java.awt.Color;
import java.util.ArrayList;

import backend.Board;
import backend.Field;
import backend.Piece;

public class Queen extends Piece {
	public int[][] possibleFields = {{0, 4}, {7, 4}};
	
    public Queen(Color color, Board board) {
        super("Q", color, board);
    }

    @Override
    public Field[] getReachableFields() {
        ArrayList<Field> reachableFields = new ArrayList<>();
        int currentRow = this.getField().row;
        int currentColumn = this.getField().column;

        // Acht Richtungen prüfen
        int[][] directions = {
            {0, 1},   // Rechts
            {0, -1},  // Links
            {1, 0},   // Unten
            {-1, 0},  // Oben
            {1, 1},   // Diagonal rechts unten
            {-1, -1}, // Diagonal links oben
            {1, -1},  // Diagonal links unten
            {-1, 1}   // Diagonal rechts oben
        };

        for (int[] direction : directions) {
            int dRow = direction[0];
            int dCol = direction[1];
            int newRow = currentRow + dRow;
            int newCol = currentColumn + dCol;

            // In der jeweiligen Richtung bewegen
            while (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) {
                Field nextField = board.board[newRow][newCol];

                if (nextField.onField == null) {
                    // Feld ist leer, Dame kann sich hierhin bewegen
                    reachableFields.add(nextField);
                } else if (nextField.onField.getColor() != this.getColor()) {
                    // Gegnerische Figur: Dame kann das Feld erreichen
                    reachableFields.add(nextField);
                    break; // Schleife für diese Richtung abbrechen
                } else {
                    // Eigene Figur: Blockade, Schleife abbrechen
                    break;
                }

                // Nächster Schritt in die Richtung
                newRow += dRow;
                newCol += dCol;
            }
        }

        return reachableFields.toArray(new Field[0]);
    }

	@Override
	public void create() {
        new Queen(Color.WHITE, board).setField(7, 3);

        new Queen(Color.BLACK, board).setField(0, 3);
	}
}
