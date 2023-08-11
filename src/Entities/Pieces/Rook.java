package Entities.Pieces;

import Entities.Boardgame.Board;
import Entities.Chess.ChessPiece;
import Enums.Color;

public class Rook extends ChessPiece {

    public Rook(Board board, Color color) {
        super(board, color);
    }

    @Override
    public String toString() {
        return "R";
    }
}
