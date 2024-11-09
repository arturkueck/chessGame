package backend;

import java.util.zip.Inflater;

public class Board {
	public Field[][] board = new Field[8][8];
	
	public Board() {
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				board[i][j] = new Field(i, j);
			}
		}
	}
	
	public void createChessGame() {
		
	}
	
	public void createDameGame() {
		
	}
	
	public boolean checkIfEmpty(int col, int row) {
		return board[col][row].onField == null;
	}
	
	public Piece getPiece(int col, int row) {
		return board[col][row].onField;
	}
	
	public boolean movePiece(int col, int row, int col2, int row2) {
	    // Überprüfen, ob das Ausgangsfeld innerhalb des Schachbretts liegt
	    if (col < 0 || col >= 8 || row < 0 || row >= 8) {
	        return false; // Ungültiges Ausgangsfeld
	    }

	    // Überprüfen, ob das Zielfeld innerhalb des Schachbretts liegt
	    if (col2 < 0 || col2 >= 8 || row2 < 0 || row2 >= 8) {
	        return false; // Ungültiges Zielfeld
	    }

	    // Ausgangs- und Zielfeld abrufen
	    Field startField = board[col][row];
	    Field targetField = board[col2][row2];

	    // Überprüfen, ob auf dem Ausgangsfeld eine Figur steht
	    if (startField.onField == null) {
	        return false; // Keine Figur auf dem Ausgangsfeld
	    }

	    // Erreichbare Felder der Figur auf dem Ausgangsfeld abrufen
	    Piece piece = startField.onField;
	    Field[] reachableFields = piece.getReachableFields();

	    // Überprüfen, ob das Zielfeld in den erreichbaren Feldern liegt
	    boolean isReachable = false;
	    for (Field field : reachableFields) {
	        if (field.equals(targetField)) {
	            isReachable = true;
	            break;
	        }
	    }

	    if (!isReachable) {
	        return false; // Zielfeld ist nicht erreichbar
	    }

	    // Figur auf das Zielfeld bewegen
	    targetField.placePiece(piece); // Figur auf das Zielfeld setzen
	    startField.takePieceFromField(); // Figur vom Ausgangsfeld entfernen
	    piece.setField(col2, row2); // Die Figur weiß, dass sie bewegt wurde

	    return true; // Bewegung erfolgreich
	}

	
	@Override
	public String toString() {
		String out = "";
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				if(board[i][j].onField != null) {
					out += board[i][j].onField.toString() + "; ";
				} else {
					out += "_; ";
				}
				
			}
			out += "\n";
		}
		return out;
	}
}
