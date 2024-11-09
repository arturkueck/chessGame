package backend;

import java.awt.Color;

import backend.pieces.Rook;

public class Main {
	public static void main(String[] args) {
		Board board = new Board();
		Rook rook = new Rook(Color.BLACK, board);
		Rook rook2 = new Rook(Color.BLACK, board);
		rook.setField(3, 3); // Setzt den Turm auf D4
		rook2.setField(3, 4);
		
		Field[] reachableFields = rook.getReachableFields();
		for (Field field : reachableFields) {
		    System.out.println(field);
		}

		System.out.println(board.toString());
		
		board.movePiece(3, 4, 3, 3);
		
		System.out.println(board.toString());
		
		board.movePiece(3, 4, 3, 5);
		
		System.out.println(board.toString());

	}
}
