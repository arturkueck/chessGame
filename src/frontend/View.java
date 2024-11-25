package frontend;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
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
    private JCheckBox rotateBlackPiecesCheckbox;
    private JButton whiteSquareColorButton;
    private JButton darkSquareColorButton;
    private JComboBox<String> pieceFontDropdown;
    private JPanel[][] chessBoardPanels;
    private JButton[][] chessBoardButtons;
    private Map<String, ImageIcon> pieceIcons;
    private Controller controller;
    
    public View(boolean one, boolean two, boolean three) {
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
        
        if(one) {
        	rightPanel.add(showCoordinatesCheckbox);
        }
        
        JLabel whiteSquareLabel = new JLabel("Weiße Felder:");
        whiteSquareColorButton = new JButton("Wählen");
        JLabel darkSquareLabel = new JLabel("Dunkle Felder:");
        darkSquareColorButton = new JButton("Wählen");
        
        if(two) {
            // Add color pickers
            rightPanel.add(Box.createVerticalStrut(10)); // Add some spacing
            rightPanel.add(whiteSquareLabel);
            rightPanel.add(whiteSquareColorButton);

            rightPanel.add(Box.createVerticalStrut(10)); // Add some spacing
            rightPanel.add(darkSquareLabel);
            rightPanel.add(darkSquareColorButton);

        }
        
        JLabel rotateLabel = new JLabel("Schwarze Figuren drehen:");
        rotateBlackPiecesCheckbox = new JCheckBox("Drehen");

        if(three) {
            // Checkbox für Figurenrotation
            rightPanel.add(Box.createVerticalStrut(20)); // Platzhalter
            rightPanel.add(rotateLabel);
            rightPanel.add(rotateBlackPiecesCheckbox);
        }

        // Add panels to main layout
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(chessBoardPanel, BorderLayout.CENTER);
        mainPanel.add(rightPanel, BorderLayout.EAST);

        frame.add(mainPanel);
        loadPieceIcons(true);
    }
    
    public void setController(Controller controller) {
        this.controller = controller;
    }


    private void initializeChessBoard() {
        chessBoardPanel = new JPanel(null) { // Use null layout for precise control
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
                int size = Math.min(getWidth(), getHeight());
                setSize(size, size); // Ensure the panel itself is square
            }

            @Override
            public Dimension getPreferredSize() {
                int size = Math.min(getParent().getWidth(), getParent().getHeight());
                return new Dimension(size, size);
            }
        };

        chessBoardPanels = new JPanel[8][8];
        chessBoardButtons = new JButton[8][8];

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                JPanel fieldPanel = new JPanel(new BorderLayout());
                fieldPanel.setOpaque(true);

                if ((row + col) % 2 == 0) {
                    fieldPanel.setBackground(Color.WHITE); // White square
                } else {
                    fieldPanel.setBackground(new Color(139, 69, 19)); // Dark square
                }

                String coordinates = (char) ('A' + col) + String.valueOf(8 - row); // Chess notation
                JLabel coordinateLabel = new JLabel(coordinates);
                coordinateLabel.setFont(new Font("Arial", Font.PLAIN, 10));
                coordinateLabel.setForeground(new Color(155, 155, 155, 155)); // Semi-transparent white for dark squares
                coordinateLabel.setBounds(5, fieldPanel.getHeight() - 15, 30, 10); // Position at bottom-left corner
                coordinateLabel.setVisible(false); // Initially hidden
                
                JButton button = new JButton();
                button.setFont(new Font("Arial", Font.BOLD, 20));
                button.setOpaque(false);
                button.setContentAreaFilled(false); // Transparent background
                button.setBorderPainted(false);

                fieldPanel.add(coordinateLabel, BorderLayout.NORTH);
                fieldPanel.add(button, BorderLayout.CENTER);
                chessBoardButtons[row][col] = button;

                chessBoardPanels[row][col] = fieldPanel;
                chessBoardPanel.add(fieldPanel);

                // Set layout and position for the field panel
                fieldPanel.setBounds(
                    col * (chessBoardPanel.getWidth() / 8),
                    row * (chessBoardPanel.getHeight() / 8),
                    chessBoardPanel.getWidth() / 8,
                    chessBoardPanel.getHeight() / 8
                );
            }
        }

        // Add a resize listener to dynamically adjust squares
        chessBoardPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                int size = Math.min(chessBoardPanel.getWidth(), chessBoardPanel.getHeight());
                chessBoardPanel.setPreferredSize(new Dimension(size, size));
                for (int row = 0; row < 8; row++) {
                    for (int col = 0; col < 8; col++) {
                        chessBoardPanels[row][col].setBounds(
                            col * (size / 8),
                            row * (size / 8),
                            size / 8,
                            size / 8
                        );
                    }
                }
                chessBoardPanel.revalidate();
                chessBoardPanel.repaint();
            }
        });
    }

    public JCheckBox getRotateBlackPiecesCheckbox() {
        return rotateBlackPiecesCheckbox;
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

    public ImageIcon getPieceIcon(String pieceSymbol, Color color) {
        String colorSuffix = color.equals(Color.WHITE) ? "WHITE" : "BLACK";
        String key = pieceSymbol + "_" + colorSuffix;
        return pieceIcons.getOrDefault(key, null); // Return the icon or null if not found
    }

    public void resetButtonColors(Color whiteColor, Color darkColor) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                JPanel fieldPanel = chessBoardPanels[row][col];
                if ((row + col) % 2 == 0) {
                    fieldPanel.setBackground(whiteColor);
                    chessBoardButtons[row][col].setBackground(whiteColor);
                } else {
                    fieldPanel.setBackground(darkColor);
                    chessBoardButtons[row][col].setBackground(darkColor);
                }
            }
        }
    }

    void loadPieceIcons(boolean rotateBlackPieces) {
        pieceIcons = new HashMap<>();
        String basePath = System.getProperty("user.dir") + "/src/frontend/chessfont1/";

        String[] pieceNames = { "K", "Q", "R", "B", "N", "P" };
        String[] colors = { "WHITE", "BLACK" };

        // Ermitteln der gewünschten Größe basierend auf den Schachfeldern
        int fieldWidth = chessBoardButtons[0][0].getWidth();
        int fieldHeight = chessBoardButtons[0][0].getHeight();
        if (fieldWidth == 0 || fieldHeight == 0) {
            // Standardgröße, falls die Felder noch nicht sichtbar sind
            fieldWidth = 60; // Beispielwert, kann angepasst werden
            fieldHeight = 60;
        }

        for (String piece : pieceNames) {
            for (String color : colors) {
                String filePath = basePath + piece + (color.equals("WHITE") ? "" : piece) + ".png"; // Updated to PNG
                try {
                    // PNG laden
                    BufferedImage bufferedImage = ImageIO.read(new File(filePath));
                    if (bufferedImage != null) {
                        // Bild skalieren
                        Image scaledImage = bufferedImage.getScaledInstance(fieldWidth, fieldHeight, Image.SCALE_SMOOTH);

                        // Falls schwarze Figuren gedreht werden sollen
                        if (color.equals("BLACK") && !rotateBlackPieces) {
                            BufferedImage rotatedImage = new BufferedImage(
                                bufferedImage.getWidth(),
                                bufferedImage.getHeight(),
                                bufferedImage.getType()
                            );
                            Graphics2D g2d = rotatedImage.createGraphics();
                            AffineTransform transform = AffineTransform.getRotateInstance(
                                Math.PI, // 180 Grad in Radiant
                                bufferedImage.getWidth() / 2.0,
                                bufferedImage.getHeight() / 2.0
                            );
                            g2d.drawImage(bufferedImage, transform, null);
                            g2d.dispose();

                            // Skaliertes Bild aktualisieren
                            scaledImage = rotatedImage.getScaledInstance(fieldWidth, fieldHeight, Image.SCALE_SMOOTH);
                        }

                        // Bild als Icon speichern
                        ImageIcon icon = new ImageIcon(scaledImage);
                        pieceIcons.put(piece + "_" + color, icon);
                        System.out.println("Successfully loaded icon: " + piece + "_" + color);
                    }
                } catch (Exception e) {
                    System.out.println("Failed to load icon: " + piece + "_" + color + " (" + e.getMessage() + ")");
                }
            }
        }
    }

}
