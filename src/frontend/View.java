package frontend;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.MediaTracker;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class View {
    private JFrame frame;
    private JPanel mainPanel;
    private JPanel chessBoardPanel;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JTextArea moveNotationArea;
    private JCheckBox showCoordinatesCheckbox;
    private JButton whiteSquareColorButton;
    private JButton darkSquareColorButton;
    private JComboBox<String> pieceFontDropdown;
    private JPanel[][] chessBoardPanels;
    private JButton[][] chessBoardButtons;
    private Map<String, ImageIcon> pieceIcons;

    public View() {
        frame = new JFrame("Schach");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);

        // Main layout with 3 columns
        mainPanel = new JPanel(new BorderLayout());

        // Left panel for move notations
        leftPanel = new JPanel(new BorderLayout());
        moveNotationArea = new JTextArea();
        moveNotationArea.setEditable(false);
        moveNotationArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(moveNotationArea);
        leftPanel.add(scrollPane, BorderLayout.CENTER);
        leftPanel.setPreferredSize(new Dimension(200, 600));

        // Center panel for the chessboard
        chessBoardPanel = new JPanel(new GridLayout(8, 8));
        chessBoardPanels = new JPanel[8][8];
        chessBoardButtons = new JButton[8][8];
        initializeChessBoard();

        // Right panel for controls
        rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setPreferredSize(new Dimension(200, 600));

        // Add the "Show Coordinates" checkbox
        showCoordinatesCheckbox = new JCheckBox("Feldbezeichnungen anzeigen");
        rightPanel.add(showCoordinatesCheckbox);

        // Add color pickers
        rightPanel.add(Box.createVerticalStrut(10)); // Add some spacing
        JLabel whiteSquareLabel = new JLabel("Weiße Felder:");
        whiteSquareColorButton = new JButton("Wählen");
        rightPanel.add(whiteSquareLabel);
        rightPanel.add(whiteSquareColorButton);

        rightPanel.add(Box.createVerticalStrut(10)); // Add some spacing
        JLabel darkSquareLabel = new JLabel("Dunkle Felder:");
        darkSquareColorButton = new JButton("Wählen");
        rightPanel.add(darkSquareLabel);
        rightPanel.add(darkSquareColorButton);

        // Add dropdown for piece fonts
        rightPanel.add(Box.createVerticalStrut(20)); // Add larger spacing
        JLabel pieceFontLabel = new JLabel("Schriftart der Figuren:");
        String[] pieceFonts = { "Arial", "Times New Roman", "Courier New", "Verdana" };
        pieceFontDropdown = new JComboBox<>(pieceFonts);
        rightPanel.add(pieceFontLabel);
        rightPanel.add(pieceFontDropdown);

        // Add panels to main layout
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(chessBoardPanel, BorderLayout.CENTER);
        mainPanel.add(rightPanel, BorderLayout.EAST);

        frame.add(mainPanel);
        loadPieceIcons();
    }

    private void initializeChessBoard() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                JPanel fieldPanel = new JPanel(new BorderLayout());
                fieldPanel.setOpaque(true);
                if ((row + col) % 2 == 0) {
                    fieldPanel.setBackground(Color.WHITE);
                } else {
                    fieldPanel.setBackground(new Color(139, 69, 19));
                }

                JButton button = new JButton();
                button.setFont(new Font("Arial", Font.BOLD, 20));
                button.setOpaque(true);
                button.setContentAreaFilled(true); // Ensure background is shown
                button.setBorderPainted(false);
                button.setHorizontalTextPosition(JButton.CENTER);
                button.setVerticalTextPosition(JButton.CENTER);

                fieldPanel.add(button, BorderLayout.CENTER);
                chessBoardPanels[row][col] = fieldPanel;
                chessBoardButtons[row][col] = button;
                chessBoardPanel.add(fieldPanel);
            }
        }
    }

    public void setVisible(boolean visible) {
        frame.setVisible(visible);
    }

    public JButton[][] getChessBoardButtons() {
        return chessBoardButtons;
    }

    public JCheckBox getShowCoordinatesCheckbox() {
        return showCoordinatesCheckbox;
    }

    public JPanel[][] getChessBoardPanels() {
        return chessBoardPanels;
    }

    public JTextArea getMoveNotationArea() {
        return moveNotationArea;
    }

    public JButton getWhiteSquareColorButton() {
        return whiteSquareColorButton;
    }

    public JButton getDarkSquareColorButton() {
        return darkSquareColorButton;
    }

    public JComboBox<String> getPieceFontDropdown() {
        return pieceFontDropdown;
    }

    public void resetButtonColors(Color whiteColor, Color darkColor) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if ((row + col) % 2 == 0) {
                    chessBoardButtons[row][col].setBackground(whiteColor);
                } else {
                    chessBoardButtons[row][col].setBackground(darkColor);
                }
            }
        }
    }

    private String columnToNotation(int col) {
        return String.valueOf((char) ('a' + col));
    }

    private String rowToNotation(int row) {
        return String.valueOf(8 - row);
    }

    private void loadPieceIcons() {
        pieceIcons = new HashMap<>();
        String basePath = System.getProperty("user.dir") + "/src/frontend/chessfont1/";

        pieceIcons.put("K_WHITE", new ImageIcon(basePath + "K.png"));
        pieceIcons.put("K_BLACK", new ImageIcon(basePath + "KK.png"));
        pieceIcons.put("Q_WHITE", new ImageIcon(basePath + "Q.png"));
        pieceIcons.put("Q_BLACK", new ImageIcon(basePath + "QQ.png"));
        pieceIcons.put("R_WHITE", new ImageIcon(basePath + "R.png"));
        pieceIcons.put("R_BLACK", new ImageIcon(basePath + "RR.png"));
        pieceIcons.put("B_WHITE", new ImageIcon(basePath + "B.png"));
        pieceIcons.put("B_BLACK", new ImageIcon(basePath + "BB.png"));
        pieceIcons.put("N_WHITE", new ImageIcon(basePath + "N.png"));
        pieceIcons.put("N_BLACK", new ImageIcon(basePath + "NN.png"));
        pieceIcons.put("P_WHITE", new ImageIcon(basePath + "P.png"));
        pieceIcons.put("P_BLACK", new ImageIcon(basePath + "PP.png"));

        // Log loaded icons to verify paths
        pieceIcons.forEach((key, icon) -> {
            if (icon.getImageLoadStatus() != MediaTracker.COMPLETE) {
                System.out.println("Failed to load icon: " + key);
            } else {
                System.out.println("Successfully loaded icon: " + key);
            }
        });
    }

    public ImageIcon getPieceIcon(String pieceName, Color color) {
        String colorSuffix = (color.equals(Color.WHITE)) ? "WHITE" : "BLACK";
        return pieceIcons.get(pieceName + "_" + colorSuffix);
    }
}
