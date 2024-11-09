package backend;

import java.awt.Color;

public abstract class Piece {
    String name;
    Color color;
    protected Board board;
    Field field;
    
    public Piece(String name, Color color, Board board) {
    	this.name = name;
    	this.color = color;
    	this.board = board;
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
        if (color.equals(Color.RED)) return "RED";
        if (color.equals(Color.GREEN)) return "GREEN";
        if (color.equals(Color.BLUE)) return "BLUE";

        // Für unbekannte Farben
        return "RGB(" + color.getRed() + ", " + color.getGreen() + ", " + color.getBlue() + ")";
    }
    
    @Override
    public String toString() {
        return name + "_" + getColorName(color);
    }
}
