package backend;

import java.awt.Color;

public abstract class Piece {
    String name;
    Color color;
    protected Board board;
    Field field;
    public boolean hasMoved;
    
    public Piece(String name, Color color, Board board) {
        this.name = name;
        this.color = color;
        this.board = board;
        this.hasMoved = false; // Standardmäßig hat sich die Figur noch nicht bewegt
    }
    
    public Color getColor() {
        return this.color;
    }
    
    public Field getField() {
        return this.field;
    }
    
    public void setField(int col, int row) {
        this.board.board[col][row].onField = this;
        this.field = board.board[col][row];
    }
    
    public abstract Field[] getReachableFields();
    
    public String getColorName(Color color) {
        if (color.equals(Color.BLACK)) return "BLACK";
        if (color.equals(Color.WHITE)) return "WHITE";
        return "RGB(" + color.getRed() + ", " + color.getGreen() + ", " + color.getBlue() + ")";
    }
    
    public boolean hasMoved() {
        return this.hasMoved;
    }
    
    public void moveTo(Field targetField) {
        // Entferne die Figur vom aktuellen Feld
        if (this.field != null) {
            this.field.takePieceFromField();
        }

        // Setze die Figur auf das neue Feld
        targetField.placePiece(this);
        this.field = targetField;
    }
    
    public String getSymbol() {
        return name.substring(0, 1).toUpperCase(); // Erster Buchstabe des Namens
    }

    
    @Override
    public String toString() {
        return name + "_" + getColorName(color);
    }
}

