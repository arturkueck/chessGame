package frontend;

import backend.Board;

public class Main {
    public static void main(String[] args) {
    	boolean one = false;
    	boolean two = false;
    	boolean three = false;
    	
//    	for(String arg : args) {
//    		switch (arg) {
//			case "fieldnames": {
//				one = true;
//				break;
//			}
//			case "colorpicker": {
//				two = true;
//				break;
//			}
//			case "rotator": {
//				three = true;
//				break;
//			}
//			default:
//				throw new IllegalArgumentException("Unexpected value: " + arg);
//			}
//    	}
    	
        Board board = new Board();
        board.createChessGame();

        View view = new View(one, two, three);
        Controller controller = new Controller(board, view);
        
        board.setController(controller);

        view.setController(controller);

        view.setVisible(true);
    }
}
