
package backend;

import java.util.zip.Inflater;

public class Board {
    public Field[][] board = new Field[8][8];
    public Piece lastMovedPiece = null; // Tracks the last moved piece
    
    public Board() {
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                board[i][j] = new Field(i, j);
            }
        }
    }
    
    public void createChessGame() {
        // Logic for initializing a chess game can be implemented here.
    }
    
    public void createDameGame() {
        // Logic for initializing a checkers game can be implemented here.
    }
    
    public boolean checkIfEmpty(int col, int row) {
        return board[col][row].onField == null;
    }
    
    public Piece getPiece(int col, int row) {
        return board[col][row].onField;
    }
    
    public boolean movePiece(int col, int row, int col2, int row2) {
        if (col < 0 || col >= 8 || row < 0 || row >= 8 || col2 < 0 || col2 >= 8 || row2 < 0 || row2 >= 8) {
            System.out.println("Invalid move: out of board bounds.");
            return false;
        }

        Field startField = board[col][row];
        Field targetField = board[col2][row2];

        if (startField.onField == null) {
            System.out.println("No piece on the starting field.");
            return false;
        }

        Piece piece = startField.onField;
        Field[] reachableFields = piece.getReachableFields();

        boolean isReachable = false;
        for (Field field : reachableFields) {
            if (field.equals(targetField)) {
                isReachable = true;
                break;
            }
        }

        if (!isReachable) {
            System.out.println("Target field is not reachable.");
            return false;
        }

        // Verwende moveTo statt setField, um die Bewegung korrekt auszuführen
        piece.moveTo(targetField);

        // Update last moved piece
        lastMovedPiece = piece;
        System.out.println("Last moved piece updated to: " + piece);
        return true;
    }

    @Override
    public String toString() {
        String out = "";
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if(board[i][j].onField != null) {
                    out += board[i][j].onField.toString() + "; ";
                } else {
                    out += "_; ";
                }
            }
            out += "\n";
        }
        return out;
    }
}
