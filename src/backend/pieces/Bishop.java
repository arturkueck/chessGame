package backend.pieces;

import java.awt.Color;
import java.util.ArrayList;

import backend.Board;
import backend.Field;
import backend.Piece;

public class Bishop extends Piece {
	public int[][] possibleFields = {{7, 2}, {7, 5}, {0, 2}, {0, 5}};
	
    public Bishop(Color color, Board board) {
        super("B", color, board);
    }

    @Override
    public Field[] getReachableFields() {
        ArrayList<Field> reachableFields = new ArrayList<>();
        int currentRow = this.getField().row;
        int currentColumn = this.getField().column;

        // Vier diagonale Richtungen prüfen
        int[][] directions = {
            {1, 1},   // Diagonal rechts unten
            {1, -1},  // Diagonal links unten
            {-1, 1},  // Diagonal rechts oben
            {-1, -1}  // Diagonal links oben
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
                    // Feld ist leer, Läufer kann sich hierhin bewegen
                    reachableFields.add(nextField);
                } else if (nextField.onField.getColor() != this.getColor()) {
                    // Gegnerische Figur: Läufer kann das Feld betreten
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
		new Bishop(Color.WHITE, board).setField(7, 2);
		new Bishop(Color.WHITE, board).setField(7, 5);
		
		new Bishop(Color.BLACK, board).setField(0, 2);
		new Bishop(Color.BLACK, board).setField(0, 5);
	}
}
