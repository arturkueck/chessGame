
package backend.pieces;

import java.awt.Color;
import java.util.ArrayList;

import backend.Board;
import backend.Field;
import backend.Piece;

public class Pawn extends Piece {
    public boolean hasMovedTwoFields = false;

    public Pawn(Color color, Board board) {
        super("P", color, board);
    }

    @Override
    public Field[] getReachableFields() {
        ArrayList<Field> reachableFields = new ArrayList<>();
        int currentRow = this.getField().row;
        int currentColumn = this.getField().column;

        // Direction of the pawn based on color
        int direction = (this.getColor() == Color.WHITE) ? -1 : 1;

        // Normal move: one square forward
        int newRow = currentRow + direction;
        if (newRow >= 0 && newRow < 8) {
            Field forwardField = board.board[newRow][currentColumn];
            if (forwardField.onField == null) {
                reachableFields.add(forwardField);

                // Two squares forward (only from initial position)
                if ((this.getColor() == Color.WHITE && currentRow == 6) ||
                        (this.getColor() == Color.BLACK && currentRow == 1)) {
                    Field twoStepsField = board.board[newRow + direction][currentColumn];
                    if (twoStepsField.onField == null) {
                        reachableFields.add(twoStepsField);
                    }
                }
            }
        }

        // Capture: Diagonal left
        int newCol = currentColumn - 1;
        if (newCol >= 0 && newRow >= 0 && newRow < 8) {
            Field diagonalLeft = board.board[newRow][newCol];
            if (diagonalLeft.onField != null && diagonalLeft.onField.getColor() != this.getColor()) {
                reachableFields.add(diagonalLeft);
            }
//#ifdef enPassat
            // En-Passant: Check
            Field leftField = board.board[currentRow][newCol];
            if (leftField.onField instanceof Pawn) {
                Pawn leftPawn = (Pawn) leftField.onField;
                System.out.println("Checking En-Passant on diagonal left: " + leftPawn);
                System.out.println("lastMovedPiece: " + board.lastMovedPiece + ", hasMovedTwoFields: "
                        + leftPawn.hasMovedTwoFields);
                if (leftPawn.getColor() != this.getColor() && leftPawn.hasMovedTwoFields &&
                        board.lastMovedPiece == leftPawn) {
                    System.out.println("En-Passant available on diagonal left.");
                    reachableFields.add(diagonalLeft);
                }
            }
//#endif
        }

        // Capture: Diagonal right
        newCol = currentColumn + 1;
        if (newCol < 8 && newRow >= 0 && newRow < 8) {
            Field diagonalRight = board.board[newRow][newCol];
            if (diagonalRight.onField != null && diagonalRight.onField.getColor() != this.getColor()) {
                reachableFields.add(diagonalRight);
            }
//#ifdef enPassat
            // En-Passant: Check
            Field rightField = board.board[currentRow][newCol];
            if (rightField.onField instanceof Pawn) {
                Pawn rightPawn = (Pawn) rightField.onField;
                System.out.println("Checking En-Passant on diagonal right: " + rightPawn);
                System.out.println("lastMovedPiece: " + board.lastMovedPiece + ", hasMovedTwoFields: "
                        + rightPawn.hasMovedTwoFields);
                if (rightPawn.getColor() != this.getColor() && rightPawn.hasMovedTwoFields &&
                        board.lastMovedPiece == rightPawn) {
                    System.out.println("En-Passant available on diagonal right.");
                    reachableFields.add(diagonalRight);
                }
            }
//#endif
        }

        return reachableFields.toArray(new Field[0]);
    }

    @Override
    public void moveTo(Field targetField) {
        int startRow = this.getField().row;
        int startCol = this.getField().column;

        // Check if moving two squares forward
        if (Math.abs(startRow - targetField.row) == 2) {
            this.hasMovedTwoFields = true;
            System.out.println("Pawn moved two fields: " + this + " from " + startRow + " to " + targetField.row);
        } else {
            this.hasMovedTwoFields = false;
        }

//#ifdef enPassat
        // Handle En-Passant captures
        if (Math.abs(startCol - targetField.column) == 1 && targetField.onField == null) {
            int direction = (this.getColor() == Color.WHITE) ? 1 : -1;
            int capturedRow = targetField.row + direction;
            int capturedCol = targetField.column;
            Field capturedField = board.board[capturedRow][capturedCol];

            if (capturedField.onField instanceof Pawn) {
                System.out.println("En-Passant capture executed: " + capturedField.onField);
                capturedField.takePieceFromField(); // Entfernt die Figur aus dem Feld
                board.board[capturedRow][capturedCol].onField = null; // Aktualisiert den Zustand des Boards
            }
        }
//#endif

        // Call the superclass to handle the actual movement
        super.moveTo(targetField);
        
//#ifdef pawnPromotion
        int targetRow = targetField.row;
        if ((this.getColor() == Color.WHITE && targetRow == 0) || 
            (this.getColor() == Color.BLACK && targetRow == 7)) {
            System.out.println("Pawn reached the last rank: " + this);

            // Notify the controller for promotion
            board.lastMovedPiece = this;
            board.notifyPawnPromotion(this, targetField);
        }
//#endif
    }
}
