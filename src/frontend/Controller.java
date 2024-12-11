package frontend;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JColorChooser;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import backend.Board;
import backend.Field;
import backend.Piece;
import backend.pieces.Bishop;
import backend.pieces.Knight;
import backend.pieces.Pawn;
import backend.pieces.Queen;
import backend.pieces.Rook;

public class Controller {
    private Board board;
    private View view;
    private String selectedField = null;
    private List<String> moveHistory = new ArrayList<>(); // List to track moves

    
    private Color currentPlayer;

    private Color whiteSquareColor = Color.WHITE; // Default white color
    private Color darkSquareColor = new Color(139, 69, 19); // Default dark color

    public Controller(Board board, View view) {
        this.board = board;
        this.view = view;

        this.currentPlayer = Color.WHITE; // White always starts

        addListeners();
        updateChessBoard();
    }
    
//#ifdef pawnPromotion
    public void handlePawnPromotion(Pawn pawn, Field field) {
        String[] options = {"Queen", "Rook", "Bishop", "Knight"};
        String choice = (String) JOptionPane.showInputDialog(
            null,
            "Choose a piece for promotion:",
            "Pawn Promotion",
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
        );

        if (choice != null) {
            Piece newPiece = null;
            switch (choice) {
                case "Queen":
                    newPiece = new Queen(pawn.getColor(), board);
                    break;
                case "Rook":
                    newPiece = new Rook(pawn.getColor(), board);
                    break;
                case "Bishop":
                    newPiece = new Bishop(pawn.getColor(), board);
                    break;
                case "Knight":
                    newPiece = new Knight(pawn.getColor(), board);
                    break;
            }

            if (newPiece != null) {
                // Set the new piece on the field
                field.placePiece(newPiece);
                board.lastMovedPiece = newPiece; // Update the last moved piece
                newPiece.setField(field.row, field.column);
            }
        }

        updateChessBoard(); // Refresh the board view
    }
//#endif


    private void handleClick(int row, int col) {
        String fieldNotation = columnToNotation(col) + rowToNotation(row);

        if (selectedField == null) {
            if (board.getField(row, col).onField != null) {
                Piece selectedPiece = board.getField(row, col).onField;
                // Check if it's the current player's turn
                if (!selectedPiece.getColor().equals(currentPlayer)) {
                    JOptionPane.showMessageDialog(null, "It's " + getColorName(currentPlayer) + "'s turn!");
                    return;
                }
                selectedField = fieldNotation;
                view.getChessBoardButtons()[row][col].setBackground(Color.YELLOW);
            }
        } else {
            String targetField = fieldNotation;
            int startCol = notationToColumn(selectedField.charAt(0));
            int startRow = notationToRow(selectedField.charAt(1));
            int targetCol = notationToColumn(targetField.charAt(0));
            int targetRow = notationToRow(targetField.charAt(1));

            Piece movedPiece = board.getPiece(startRow, startCol);
            Piece targetPiece = board.getPiece(targetRow, targetCol);

            if (board.movePiece(startRow, startCol, targetRow, targetCol)) {
                System.out
                        .println("Move executed: " + startRow + "," + startCol + " to " + targetRow + "," + targetCol);
                String moveNotation = generateMoveNotation(startRow, startCol, targetRow, targetCol, movedPiece,
                        targetPiece);
                moveHistory.add(moveNotation);
                //#ifdef notation
                updateMoveNotationArea();
                //#endif
                updateChessBoard();
                checkGameStatus(movedPiece);

                // Switch to the next player only if the move was successful
                switchPlayer();
            } else {
                JOptionPane.showMessageDialog(null, "Invalid move. Try again.");
            }

            selectedField = null;
            view.resetButtonColors(whiteSquareColor, darkSquareColor);
        }
    }

    private void switchPlayer() {
        currentPlayer = currentPlayer.equals(Color.WHITE) ? Color.BLACK : Color.WHITE;
        System.out.println("It's now " + getColorName(currentPlayer) + "'s turn.");
    }

    private String getColorName(Color color) {
        return color.equals(Color.WHITE) ? "White" : "Black";
    }

    private String generateMoveNotation(int startRow, int startCol, int targetRow, int targetCol, Piece movedPiece,
            Piece targetPiece) {
        StringBuilder notation = new StringBuilder();

        // Pr?fen auf kurze und lange Rochade
        if (movedPiece.getSymbol().equals("K") && Math.abs(startCol - targetCol) == 2) {
            if (targetCol > startCol) {
                return "O-O"; // Kurze Rochade
            } else {
                return "O-O-O"; // Lange Rochade
            }
        }

        // En-Passant erkennen
        if (movedPiece instanceof Pawn && targetPiece == null &&
                Math.abs(startCol - targetCol) == 1 && Math.abs(startRow - targetRow) == 1) {
            notation.append(columnToNotation(startCol)); // Spalte des schlagenden Bauern
            notation.append("x").append(columnToNotation(targetCol)).append(rowToNotation(targetRow)); // Ziel
            notation.append(" e.p."); // En-Passant-Kennzeichnung
            return notation.toString();
        }

        // 1. Add piece symbol unless it's a pawn
        if (!movedPiece.getSymbol().equals("P")) {
            notation.append(movedPiece.getSymbol());
        } else if (targetPiece != null) {
            notation.append(columnToNotation(startCol)); // Add starting column for a pawn capture
        }

        // 2. Add "x" for a capture
        if (targetPiece != null) {
            notation.append("x");
        }

        // 3. Add target square
        notation.append(columnToNotation(targetCol)).append(rowToNotation(targetRow));

        // 4. Add "+" for check or "#" for checkmate
        Color opponentColor = movedPiece.getColor().equals(Color.WHITE) ? Color.BLACK : Color.WHITE;
        if (board.isKingInCheckmate(opponentColor)) {
            notation.append("#");
        } else if (board.isKingInCheck(opponentColor)) {
            notation.append("+");
        }

        return notation.toString();
    }


    private void checkGameStatus(Piece movedPiece) {
        Color opponentColor = movedPiece.getColor().equals(Color.WHITE) ? Color.BLACK : Color.WHITE;
        if (board.isKingInCheckmate(opponentColor)) {
            JOptionPane.showMessageDialog(null,
                    (opponentColor.equals(Color.WHITE) ? "Wei?" : "Schwarz") + " ist Schachmatt!");
        } // else if (board.isKingInCheck(opponentColor)) {
          // JOptionPane.showMessageDialog(null,
          // (opponentColor.equals(Color.WHITE) ? "Wei?" : "Schwarz") + " ist im
          // Schach!");
          // }
    }

    private void updateMoveNotationArea() {
        StringBuilder notationText = new StringBuilder();

        // Create a 2xN table structure
        notationText.append("Move#  White\tBlack\n");
        for (int i = 0; i < moveHistory.size(); i += 2) {
            int moveNumber = (i / 2) + 1; // Calculate the move number
            String whiteMove = moveHistory.get(i);
            String blackMove = (i + 1 < moveHistory.size()) ? moveHistory.get(i + 1) : ""; // Handle odd number of moves

            // Format moves into columns
            notationText.append(String.format("%-6d %-7s %s\n", moveNumber, whiteMove, blackMove));
        }

        // Update the text area
        view.getMoveNotationArea().setText(notationText.toString());
    }

    private void addListeners() {
        JButton[][] chessBoardButtons = view.getChessBoardButtons();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                final int currentRow = row;
                final int currentCol = col;
                chessBoardButtons[row][col].addActionListener(e -> handleClick(currentRow, currentCol));
            }
        }

        // Listener f?r Figurenrotation
        view.getRotateBlackPiecesCheckbox().addActionListener(e -> {
            boolean rotateBlackPieces = view.getRotateBlackPiecesCheckbox().isSelected();
            view.loadPieceIcons(!rotateBlackPieces); // Aktualisiere Icons
            updateChessBoard(); // Neu rendern
        });

        view.getShowCoordinatesCheckbox().addActionListener(e -> {
            boolean showCoordinates = view.getShowCoordinatesCheckbox().isSelected();
            toggleFieldCoordinates(showCoordinates);
        });

        // Color Picker Listeners bleiben unver?ndert
        view.getWhiteSquareColorButton().addActionListener(e -> {
            Color newWhiteColor = JColorChooser.showDialog(null, "Choose White Square Color", whiteSquareColor);
            if (newWhiteColor != null) {
                whiteSquareColor = newWhiteColor;
                view.resetButtonColors(whiteSquareColor, darkSquareColor);
            }
        });

        view.getDarkSquareColorButton().addActionListener(e -> {
            Color newDarkColor = JColorChooser.showDialog(null, "Choose Dark Square Color", darkSquareColor);
            if (newDarkColor != null) {
                darkSquareColor = newDarkColor;
                view.resetButtonColors(whiteSquareColor, darkSquareColor);
            }
        });
    }

    private void updateChessBoard() {
        JButton[][] chessBoardButtons = view.getChessBoardButtons(); // Get buttons
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board.getField(row, col).onField; // Get piece
                if (piece != null) {
                    ImageIcon icon = view.getPieceIcon(piece.getSymbol(), piece.getColor());
                    chessBoardButtons[row][col].setIcon(icon); // Set icon
                    chessBoardButtons[row][col].setText(""); // Clear text
                    System.out.println("Set icon for " + piece + " at " + row + "," + col);
                } else {
                    chessBoardButtons[row][col].setIcon(null); // Clear icon
                    System.out.println("Cleared icon at " + row + "," + col);
                }
            }
        }
    }

    void toggleFieldCoordinates(boolean showCoordinates) {
    	System.out.println(showCoordinates);
        JPanel[][] chessBoardPanels = view.getChessBoardPanels();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                JPanel panel = chessBoardPanels[row][col];
                Component[] components = panel.getComponents();
                for (Component component : components) {
                    if (component instanceof JLabel) {
                        component.setVisible(showCoordinates);
                    }
                }
            }
        }
    }

    private int notationToColumn(char column) {
        return column - 'a';
    }

    private int notationToRow(char row) {
        return 8 - (row - '1') - 1;
    }

    private String columnToNotation(int col) {
        return String.valueOf((char) ('a' + col));
    }

    private String rowToNotation(int row) {
        return String.valueOf(8 - row);
    }
}
