
package backend.pieces;

import java.awt.Color;
import java.util.ArrayList;

import backend.Board;
import backend.Field;
import backend.Piece;

public class King extends Piece {
    public boolean hasMooved = false;

    public King(Color color, Board board) {
        super("K", color, board);
    }

    @Override
    public Field[] getReachableFields() {
        ArrayList<Field> reachableFields = new ArrayList<>();
        int currentRow = this.getField().row;
        int currentColumn = this.getField().column;

        // Standardbewegungen des Königs
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
            }
        }

        // Rochade überprüfen
        if (!this.hasMoved) {
            // Königsseite Rochade
            if (canCastle(currentRow, 7)) {
                reachableFields.add(board.board[currentRow][6]);
            }
            // Damenseite Rochade
            if (canCastle(currentRow, 0)) {
                reachableFields.add(board.board[currentRow][2]);
            }
        }

        return reachableFields.toArray(new Field[0]);
    }

    private boolean canCastle(int kingRow, int rookColumn) {
        Rook rook = getRookForCastle(kingRow, rookColumn);
        if (rook == null || rook.hasMoved) return false;

        int step = (rookColumn > this.getField().column) ? 1 : -1;
        int col = this.getField().column + step;

        // Überprüfen, ob Felder zwischen König und Turm leer und nicht angegriffen sind
        while (col != rookColumn) {
            if (board.board[kingRow][col].onField != null || isFieldUnderAttack(kingRow, col)) {
                return false;
            }
            col += step;
        }

        // Überprüfen, ob Start- und Zielposition des Königs nicht angegriffen werden
        return !isFieldUnderAttack(kingRow, this.getField().column) &&
               !isFieldUnderAttack(kingRow, this.getField().column + step);
    }

    private Rook getRookForCastle(int row, int column) {
        if (column < 0 || column >= 8) return null;
        Piece piece = board.board[row][column].onField;
        if (piece instanceof Rook && piece.getColor() == this.getColor()) {
            return (Rook) piece;
        }
        return null;
    }

    private boolean isFieldUnderAttack(int row, int column) {
        for (Field[] fields : board.board) {
            for (Field field : fields) {
                Piece piece = field.onField;
                if (piece != null && piece.getColor() != this.getColor() && !(piece instanceof King)) {
                    Field[] reachableFields = piece.getReachableFields();
                    for (Field reachableField : reachableFields) {
                        if (reachableField.row == row && reachableField.column == column) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void moveTo(Field targetField) {
        int kingRow = this.getField().row;
        int kingCol = this.getField().column;
        int targetCol = targetField.column;

        // Überprüfen, ob es sich um eine Rochade handelt
        if (!this.hasMoved && Math.abs(targetCol - kingCol) == 2) {
            // Königsseite Rochade
            if (targetCol > kingCol) {
                Rook rook = (Rook) board.board[kingRow][7].onField;
                if (rook != null) {
                    rook.moveTo(board.board[kingRow][5]);
                }
            }
            // Damenseite Rochade
            else {
                Rook rook = (Rook) board.board[kingRow][0].onField;
                if (rook != null) {
                    rook.moveTo(board.board[kingRow][3]);
                }
            }
        }

        super.moveTo(targetField);
        this.hasMoved = true; // Sobald sich der König bewegt, wird hasMoved auf true gesetzt
    }
}
