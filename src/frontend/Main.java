package frontend;

import backend.Board;
import backend.Piece;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    private static Board board;

    public static void main(String[] args) {
        board = new Board();
        board.createChessGame();

        // JFrame erstellen
        JFrame frame = new JFrame("Schach");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600); // Breiter, um Platz für die Checkbox zu haben

        // Hauptpanel mit BorderLayout erstellen
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Schachbrettpanel erstellen
        JPanel chessBoardPanel = new JPanel();
        chessBoardPanel.setLayout(new GridLayout(8, 8));

        // Felder (Panels) des Schachbretts erstellen
        JPanel[][] chessBoardPanels = new JPanel[8][8];
        JButton[][] chessBoardButtons = new JButton[8][8];

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                // Panel für das Feld erstellen
                JPanel fieldPanel = new JPanel();
                fieldPanel.setLayout(new BorderLayout());
                fieldPanel.setOpaque(true);

                // Farben der Felder
                if ((row + col) % 2 == 0) {
                    fieldPanel.setBackground(Color.WHITE);
                } else {
                    fieldPanel.setBackground(new Color(139, 69, 19)); // Braun
                }

                // Beschriftung des Felds
                JLabel label = new JLabel(columnToNotation(col) + rowToNotation(row));
                label.setFont(new Font("Arial", Font.PLAIN, 10));
                label.setForeground(Color.GRAY);
                label.setHorizontalAlignment(SwingConstants.RIGHT);
                label.setVerticalAlignment(SwingConstants.BOTTOM);
                label.setVisible(false); // Standardmäßig unsichtbar
                fieldPanel.add(label, BorderLayout.SOUTH);

                // Button für die Figur
                JButton button = new JButton();
                button.setFont(new Font("Arial", Font.BOLD, 20));
                button.setOpaque(false);
                button.setContentAreaFilled(false);
                button.setBorderPainted(false);

                final int currentRow = row;
                final int currentCol = col;

                button.addActionListener(e -> handleClick(currentRow, currentCol, chessBoardButtons));

                // Button und Beschriftung ins Feld-Panel einfügen
                fieldPanel.add(button, BorderLayout.CENTER);
                chessBoardPanels[row][col] = fieldPanel;
                chessBoardButtons[row][col] = button;
                chessBoardPanel.add(fieldPanel);
            }
        }

        // Checkbox für Feldbezeichnungen
        JCheckBox showCoordinatesCheckbox = new JCheckBox("Feldbezeichnungen anzeigen");
        showCoordinatesCheckbox.addActionListener(e -> {
            boolean showCoordinates = showCoordinatesCheckbox.isSelected();
            toggleFieldCoordinates(chessBoardPanels, showCoordinates);
        });

        // Schachbrett ins Hauptpanel hinzufügen
        mainPanel.add(chessBoardPanel, BorderLayout.CENTER);

        // Checkbox oben hinzufügen
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());
        controlPanel.add(showCoordinatesCheckbox);
        mainPanel.add(controlPanel, BorderLayout.NORTH);

        // Schachbrett initial aktualisieren
        updateChessBoard(chessBoardButtons);

        // Hauptpanel zum Frame hinzufügen
        frame.add(mainPanel);

        // Fenster sichtbar machen
        frame.setVisible(true);
    }

    // Methode, um das Schachbrett nach jedem Zug zu aktualisieren
    private static void updateChessBoard(JButton[][] chessBoardButtons) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board.getField(row, col).onField;
                if (piece != null) {
                    chessBoardButtons[row][col].setText(piece.getSymbol());
                } else {
                    chessBoardButtons[row][col].setText("");
                }
            }
        }
    }

    // Feldbeschriftungen anzeigen/verstecken
    private static void toggleFieldCoordinates(JPanel[][] chessBoardPanels, boolean showCoordinates) {
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

    // Logik für das Anklicken von Feldern
    private static String selectedField = null;

    private static void handleClick(int row, int col, JButton[][] chessBoardButtons) {
        String fieldNotation = columnToNotation(col) + rowToNotation(row);

        if (selectedField == null) {
            // Startfeld auswählen
            if (board.getField(row, col).onField != null) {
                selectedField = fieldNotation;
                chessBoardButtons[row][col].setBackground(Color.YELLOW); // Markierung
            }
        } else {
            // Zielfeld auswählen und Zug ausführen
            String targetField = fieldNotation;
            int startCol = notationToColumn(selectedField.charAt(0));
            int startRow = notationToRow(selectedField.charAt(1));
            int targetCol = notationToColumn(targetField.charAt(0));
            int targetRow = notationToRow(targetField.charAt(1));

            if (board.movePiece(startRow, startCol, targetRow, targetCol)) {
                updateChessBoard(chessBoardButtons);
            } else {
                JOptionPane.showMessageDialog(null, "Ungültiger Zug. Versuche es erneut.");
            }

            // Zurücksetzen
            selectedField = null;
            resetButtonColors(chessBoardButtons);
        }
    }

    // Hilfsmethoden für die Schachnotation
    private static int notationToColumn(char column) {
        return column - 'a';
    }

    private static int notationToRow(char row) {
        return 8 - (row - '1') - 1; // Umwandlung von Schachnotation in Array-Index
    }

    private static String columnToNotation(int col) {
        return String.valueOf((char) ('a' + col));
    }

    private static String rowToNotation(int row) {
        return String.valueOf(8 - row);
    }

    private static void resetButtonColors(JButton[][] chessBoardButtons) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if ((row + col) % 2 == 0) {
                    chessBoardButtons[row][col].setBackground(Color.WHITE);
                } else {
                    chessBoardButtons[row][col].setBackground(new Color(139, 69, 19)); // Braun
                }
            }
        }
    }
}
