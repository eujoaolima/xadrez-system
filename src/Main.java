import Entities.Chess.ChessException;
import Entities.Chess.ChessMatch;
import Entities.Chess.ChessPiece;
import Entities.Chess.ChessPosition;
import Entities.UI;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
//        Position pos = new Position(3, 5);
//        System.out.println(pos);

        Scanner r = new Scanner(System.in);
        ChessMatch chessMatch = new ChessMatch();

        while (true) {
            try {
                UI.clearScreen();
                UI.printBoard(chessMatch.getPieces());
                System.out.println();
                System.out.println("Fonte: ");
                ChessPosition source = UI.readChessPosition(r);
                System.out.println();

                System.out.println("Destino: ");
                ChessPosition target = UI.readChessPosition(r);
                ChessPiece piece = chessMatch.performeChessMove(source, target);
            } catch (ChessException e) {
                System.out.println(e.getMessage());
                r.nextLine();
            }
        }
    }
}

