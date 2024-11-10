package backend;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Board board = new Board();
        board.createChessGame();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Willkommen zum Schachspiel!");
        System.out.println("Eingabeformat: Startfeld und Zielfeld (z. B. e2 e4).");
        System.out.println("Gib 'exit' ein, um das Spiel zu beenden.");
        System.out.println(board.toString());

        while (true) {
            System.out.print("Dein Zug: ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("exit")) {
                System.out.println("Spiel beendet.");
                break;
            }

            String[] parts = input.split(" ");
            if (parts.length != 2) {
                System.out.println("Ungültige Eingabe. Beispiel: e2 e4");
                continue;
            }

            String start = parts[0];
            String target = parts[1];

            if (!isValidNotation(start) || !isValidNotation(target)) {
                System.out.println("Ungültige Eingabe. Beispiel: e2 e4");
                continue;
            }

            int startCol = notationToColumn(start.charAt(0));
            int startRow = notationToRow(start.charAt(1));
            int targetCol = notationToColumn(target.charAt(0));
            int targetRow = notationToRow(target.charAt(1));

            if (!board.movePiece(startRow, startCol, targetRow, targetCol)) {
                System.out.println("Ungültiger Zug. Versuche es erneut.");
            } else {
                System.out.println("Zug erfolgreich.");
                System.out.println(board.toString());
            }
        }

        scanner.close();
    }

    private static boolean isValidNotation(String notation) {
        if (notation.length() != 2) return false;

        char column = notation.charAt(0);
        char row = notation.charAt(1);

        return column >= 'a' && column <= 'h' && row >= '1' && row <= '8';
    }

    private static int notationToColumn(char column) {
        return column - 'a';
    }

    private static int notationToRow(char row) {
        return 8 - (row - '1') - 1; // Umwandlung von Schachnotation in Array-Index
    }
}
