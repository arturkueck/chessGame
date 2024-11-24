
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
        }

        // Capture: Diagonal right
        newCol = currentColumn + 1;
        if (newCol < 8 && newRow >= 0 && newRow < 8) {
            Field diagonalRight = board.board[newRow][newCol];
            if (diagonalRight.onField != null && diagonalRight.onField.getColor() != this.getColor()) {
                reachableFields.add(diagonalRight);
            }

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
        }

        return reachableFields.toArray(new Field[0]);
    }

    @Override
    public void moveTo(Field targetField) {
        // Check if moving two squares forward
        if (Math.abs(this.getField().row - targetField.row) == 2) {
            this.hasMovedTwoFields = true;
            System.out.println(
                    "Pawn moved two fields: " + this + " from " + this.getField().row + " to " + targetField.row);
        } else {
            this.hasMovedTwoFields = false;
        }
        super.moveTo(targetField);

        // Handle En-Passant captures if applicable
        int colDiff = Math.abs(this.getField().column - targetField.column);
        if (colDiff == 1 && targetField.onField == null) {
            int direction = (this.getColor() == Color.WHITE) ? 1 : -1;
            Field capturedField = board.board[targetField.row + direction][targetField.column];
            if (capturedField.onField instanceof Pawn) {
                System.out.println("En-Passant capture executed: " + capturedField.onField);
                capturedField.takePieceFromField();
            }
        }
    }
}
