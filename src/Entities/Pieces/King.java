package Entities.Pieces;

import Entities.Boardgame.Board;
import Entities.Chess.ChessPiece;
import Enums.Color;

public class King extends ChessPiece {
    public King(Board board, Color color) {
        super(board, color);
    }

    @Override
    public String toString() {
        return "K";
    }
}
