import Entities.Chess.ChessMatch;
import Entities.UI;

public class Main {
    public static void main(String[] args) {
//        Position pos = new Position(3, 5);
//        System.out.println(pos);

        ChessMatch chessMatch = new ChessMatch();
        UI.printBoard(chessMatch.getPieces());
    }
}