package backend.pieces;

import java.awt.Color;
import java.util.ArrayList;

import backend.Board;
import backend.Field;
import backend.Piece;

public class Knight extends Piece {
	public int[][] possibleFields = {{0, 1}, {7, 1}, {0, 6}, {7, 6}};

    public Knight(Color color, Board board) {
        super("N", color, board);
    }

    @Override
    public Field[] getReachableFields() {
        ArrayList<Field> reachableFields = new ArrayList<>();
        int currentRow = this.getField().row;
        int currentColumn = this.getField().column;

        // Alle möglichen Bewegungen eines Springers
        int[][] moves = {
            {2, 1},   // Zwei Felder nach unten, eins nach rechts
            {2, -1},  // Zwei Felder nach unten, eins nach links
            {-2, 1},  // Zwei Felder nach oben, eins nach rechts
            {-2, -1}, // Zwei Felder nach oben, eins nach links
            {1, 2},   // Eins nach unten, zwei nach rechts
            {1, -2},  // Eins nach unten, zwei nach links
            {-1, 2},  // Eins nach oben, zwei nach rechts
            {-1, -2}  // Eins nach oben, zwei nach links
        };

        for (int[] move : moves) {
            int newRow = currentRow + move[0];
            int newCol = currentColumn + move[1];

            // Prüfen, ob das neue Feld innerhalb des Schachbretts liegt
            if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) {
                Field nextField = board.board[newRow][newCol];

                if (nextField.onField == null) {
                    // Feld ist leer, Springer kann sich hierhin bewegen
                    reachableFields.add(nextField);
                } else if (nextField.onField.getColor() != this.getColor()) {
                    // Gegnerische Figur: Springer kann das Feld betreten
                    reachableFields.add(nextField);
                }
                // Eigene Figuren blockieren, aber Springer überspringt keine Figuren
            }
        }

        return reachableFields.toArray(new Field[0]);
    }

	@Override
	public void create() {
		new Knight(Color.WHITE, board).setField(7, 1);
        new Knight(Color.WHITE, board).setField(7, 6);
        

        new Knight(Color.BLACK, board).setField(0, 1);
        new Knight(Color.BLACK, board).setField(0, 6);
	}
}
