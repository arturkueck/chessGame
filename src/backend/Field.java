package backend;

public class Field {
    public int row;
    public int column;
    public Piece onField = null;

    public Field(int row, int column) {
    	this.row = row;
    	this.column = column;
    }
    
    public Piece placePiece(Piece piece) {
    	Piece prevPiece = this.onField;
    	this.onField = piece;
    	return prevPiece;
    }
    
    public Piece takePieceFromField() {
    	Piece prevPiece = this.onField;
    	this.onField = null;
    	return prevPiece;
    }
    
    @Override
    public String toString() {
        String fieldString = row + "," + column + "," + (onField != null ? onField.toString() : "_");
        return fieldString;
    }
}
