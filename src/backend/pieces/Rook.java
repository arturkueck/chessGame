package backend.pieces;

import java.awt.Color;
import java.util.ArrayList;

import backend.Board;
import backend.Field;
import backend.Piece;

public class Rook extends Piece {
	public boolean hasMooved = false;
	
    public Rook(Color color, Board board) {
        super("R", color, board);
    }

    @Override
    public Field[] getReachableFields() {
        ArrayList<Field> reachableFields = new ArrayList<>();
        int currentRow = this.getField().row;
        int currentColumn = this.getField().column;

        // Vier Richtungen pr�fen
        int[][] directions = {
            {0, 1},  // Rechts
            {0, -1}, // Links
            {1, 0},  // Unten
            {-1, 0}  // Oben
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
                    // Feld ist leer, Turm kann sich hierhin bewegen
                    reachableFields.add(nextField);
                } else if (nextField.onField.getColor() != this.getColor()) {
                    // Gegnerische Figur: Turm kann das Feld erreichen
                    reachableFields.add(nextField);
                    break; // Schleife f�r diese Richtung abbrechen
                } else {
                    // Eigene Figur: Blockade, Schleife abbrechen
                    break;
                }

                // N�chster Schritt in die Richtung
                newRow += dRow;
                newCol += dCol;
            }
        }

        return reachableFields.toArray(new Field[0]);
    }
    
    @Override
    public void moveTo(Field targetField) {
        super.moveTo(targetField);
        this.hasMoved = true; // Sobald sich der Turm bewegt, wird hasMoved auf true gesetzt
    }
}
