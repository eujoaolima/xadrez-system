import Entities.Chess.ChessException;
import Entities.Chess.ChessMatch;
import Entities.Chess.ChessPiece;
import Entities.Chess.ChessPosition;
import Entities.UI;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
//        Position pos = new Position(3, 5);
//        System.out.println(pos);

        Scanner r = new Scanner(System.in);
        ChessMatch chessMatch = new ChessMatch();
        List<ChessPiece> captured = new ArrayList<>();

        while (!chessMatch.getCheckMate()) {
            try {
                UI.clearScreen();
                UI.printMatch(chessMatch, captured);
                System.out.println();
                System.out.println("Fonte: ");
                ChessPosition source = UI.readChessPosition(r);
                System.out.println();

                boolean[][] possibleMoves = chessMatch.possibleMoves(source);
                UI.clearScreen();
                UI.printBoard(chessMatch.getPieces(), possibleMoves);

                System.out.println("Destino: ");
                ChessPosition target = UI.readChessPosition(r);
                ChessPiece piece = chessMatch.performChessMove(source, target);

                if (piece != null) {
                    captured.add(piece);
                }

            } catch (ChessException e) {
                System.out.println(e.getMessage());
                r.nextLine();
            }
        }

        UI.clearScreen();
        UI.printMatch(chessMatch, captured);
    }
}

