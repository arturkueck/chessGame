package backend;

import java.awt.Color;

import backend.pieces.Pawn;
import backend.pieces.Rook;

public class Main {
	public static void main(String[] args) {
		Board board = new Board();
		Pawn pawn = new Pawn(Color.BLACK, board);
		Pawn pawn2 = new Pawn(Color.WHITE, board);
		Pawn pawn3 = new Pawn(Color.BLACK, board);
		Pawn pawn4 = new Pawn(Color.WHITE, board);
		pawn.setField(1, 3); // Setzt den Turm auf D4
		pawn2.setField(3, 4);
		pawn3.setField(1, 0);
		pawn4.setField(6, 0);
		
		Field[] reachableFields = pawn.getReachableFields();
		for (Field field : reachableFields) {
		    System.out.println(field);
		}

		System.out.println(board.toString());
		
		board.movePiece(1, 3, 3, 3);
		
		reachableFields = pawn2.getReachableFields();
		for (Field field : reachableFields) {
		    System.out.println(field);
		}
		
		System.out.println(board.toString());
		
		board.movePiece(1, 3, 3, 3);
		
		reachableFields = pawn2.getReachableFields();
		for (Field field : reachableFields) {
		    System.out.println(field);
		}
		
		System.out.println(board.toString());
		
		board.movePiece(1, 3, 3, 3);
		
		reachableFields = pawn2.getReachableFields();
		for (Field field : reachableFields) {
		    System.out.println(field);
		}
		
		System.out.println(board.toString());

	}
}
