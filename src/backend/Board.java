
package backend;

import java.awt.Color;

import backend.pieces.Bishop;
import backend.pieces.King;
import backend.pieces.Knight;
import backend.pieces.Pawn;
import backend.pieces.Queen;
import backend.pieces.Rook;
import frontend.Controller;

public class Board {
    public Field[][] board = new Field[8][8];
    private Field[][] savedState;

    public Piece lastMovedPiece = null; // Tracks the last moved piece
    public Controller controller;

    public Board() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = new Field(i, j);
            }
        }
    }
    
    public void setController(Controller controller) {
    	this.controller = controller;
    }

    public void createChessGame() {
        // Farben definieren
        Color white = Color.WHITE;
        Color black = Color.BLACK;

        // Wei?e Figuren platzieren (unten)
        new Rook(white, this).setField(7, 0);
        new Knight(white, this).setField(7, 1);
        new Bishop(white, this).setField(7, 2);
        new Queen(white, this).setField(7, 3);
        new King(white, this).setField(7, 4);
        new Bishop(white, this).setField(7, 5);
        new Knight(white, this).setField(7, 6);
        new Rook(white, this).setField(7, 7);

        for (int i = 0; i < 8; i++) {
            new Pawn(white, this).setField(6, i);
        }

        // Schwarze Figuren platzieren (oben)
        new Rook(black, this).setField(0, 0);
        new Knight(black, this).setField(0, 1);
        new Bishop(black, this).setField(0, 2);
        new Queen(black, this).setField(0, 3);
        new King(black, this).setField(0, 4);
        new Bishop(black, this).setField(0, 5);
        new Knight(black, this).setField(0, 6);
        new Rook(black, this).setField(0, 7);

        for (int i = 0; i < 8; i++) {
            new Pawn(black, this).setField(1, i);
        }
    }

    public boolean checkIfEmpty(int col, int row) {
        return board[col][row].onField == null;
    }

    public Piece getPiece(int col, int row) {
        return board[col][row].onField;
    }

    public void saveState() {
        savedState = new Field[8][8];
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Field original = board[row][col];
                Field copy = new Field(original.row, original.column);

                if (original.onField != null) {
                    Piece originalPiece = original.onField;
                    Piece clonedPiece = originalPiece.clone(); // Implement clone in Piece class
                    copy.placePiece(clonedPiece);
                }

                savedState[row][col] = copy;
            }
        }
    }

    public void restoreState() {
        if (savedState == null) {
            throw new IllegalStateException("No saved state to restore.");
        }

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Field savedField = savedState[row][col];
                Field currentField = board[row][col];

                currentField.takePieceFromField(); // Clear current field
                if (savedField.onField != null) {
                    Piece savedPiece = savedField.onField;
                    Piece clonedPiece = savedPiece.clone(); // Clone to maintain object separation
                    currentField.placePiece(clonedPiece);
                }
            }
        }
    }

    
    public void undoMove() {
        try {
            // Restore the board to the previously saved state
            restoreState();
            System.out.println("Move undone: Board state restored to the last saved state.");
        } catch (IllegalStateException e) {
            System.err.println("Cannot undo move: " + e.getMessage());
        }
    }



    public boolean movePiece(int col, int row, int col2, int row2) {
        if (col < 0 || col >= 8 || row < 0 || row >= 8 || col2 < 0 || col2 >= 8 || row2 < 0 || row2 >= 8) {
            //System.out.println("Invalid move: out of board bounds.");
            return false;
        }

        Field startField = board[col][row];
        Field targetField = board[col2][row2];

        if (startField.onField == null) {
            //System.out.println("No piece on the starting field.");
            return false;
        }

        Piece piece = startField.onField;
        Field[] reachableFields = piece.getReachableFields();
        //System.out.println("Reachable fields for " + piece + ":");
        for (Field f : reachableFields) {
            //System.out.println("Field: (" + f.row + ", " + f.column + ")");
        }

        boolean isReachable = false;
        for (Field field : reachableFields) {
            if (field.equals(targetField)) {
                isReachable = true;
                break;
            }
        }

        if (!isReachable) {
            //System.out.println("Target field is not reachable.");
            return false;
        }

        // Verwende moveTo statt setField, um die Bewegung korrekt auszuf?hren
        piece.moveTo(targetField);

        // Update last moved piece
        lastMovedPiece = piece;
        //System.out.println("Last moved piece updated to: " + piece);
        return true;
    }

    private Field findKingField(Color kingColor) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board[row][col].onField;
                if (piece != null && piece.getSymbol().equals("K") && piece.getColor().equals(kingColor)) {
                    return board[row][col];
                }
            }
        }
        return null; // No king found
    }
//#ifdef check
    public boolean isKingInCheck(Color kingColor) {
        Field kingField = findKingField(kingColor);
        if (kingField == null)
            return false; // No king found (should not happen)

        // Check if any opposing piece can reach the king's position
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board[row][col].onField;
                if (piece != null && !piece.getColor().equals(kingColor)) {
                    Field[] reachableFields = piece.getReachableFields();
                    for (Field reachableField : reachableFields) {
                        if (reachableField.equals(kingField)) {
                        	System.out.println("check");
                            return true; // The king is in check
                        }
                    }
                }
            }
        }
        return false;
    }
//#endif
    
//#ifdef check && checkmate
    public boolean isKingInCheckmate(Color kingColor) {
        if (!isKingInCheck(kingColor))
            return false; // Not in check -> Not in checkmate

        Field kingField = findKingField(kingColor);
        if (kingField == null)
            return false; // No king found (should not happen)

        // 1. Check if the king can escape
        Piece king = kingField.onField;
        Field[] kingMoves = king.getReachableFields();
        for (Field move : kingMoves) {
            // Simulate the move
            Piece originalPiece = move.onField;
            move.placePiece(king);
            kingField.takePieceFromField();

            boolean stillInCheck = isKingInCheck(kingColor);

            // Restore the board state
            kingField.placePiece(king);
            move.placePiece(originalPiece);

            if (!stillInCheck) {
                return false; // The king can escape
            }
        }

        // 2. Check if another piece can block the check or capture the attacker
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board[row][col].onField;
                if (piece != null && piece.getColor().equals(kingColor)) {
                    Field[] reachableFields = piece.getReachableFields();
                    for (Field targetField : reachableFields) {
                        // Simulate the move
                        Field startField = board[row][col];
                        Piece originalPiece = targetField.onField;
                        targetField.placePiece(piece);
                        startField.takePieceFromField();

                        boolean stillInCheck = isKingInCheck(kingColor);

                        // Restore the board state
                        startField.placePiece(piece);
                        targetField.placePiece(originalPiece);

                        if (!stillInCheck) {
                            return false; // A piece can block or capture to save the king
                        }
                    }
                }
            }
        }
        System.out.println("checkmate");
        return true; // No moves to escape or block -> Checkmate
    }
//#endif
    
//#ifdef pawnPromotion
    public void notifyPawnPromotion(Pawn pawn, Field field) {
        if (this.controller != null) {
            this.controller.handlePawnPromotion(pawn, field);
        }
    }
//#endif

    @Override
    public String toString() {
        String out = "";
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j].onField != null) {
                    out += board[i][j].onField.toString() + "; ";
                } else {
                    out += "_; ";
                }
            }
            out += "\n";
        }
        return out;
    }

    public Field getField(int row, int col) {
        if (row < 0 || row >= 8 || col < 0 || col >= 8) {
            throw new IllegalArgumentException("Ung?ltige Koordinaten: Die Werte m?ssen zwischen 0 und 7 liegen.");
        }
        return board[row][col];
    }

}
