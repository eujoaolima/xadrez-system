package Entities.Chess;

import Entities.Boardgame.Board;
import Entities.Boardgame.Piece;
import Enums.Color;

public class ChessPiece extends Piece {
    private Color color;

    public ChessPiece(Board board, Color color) {
        super(board);
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

}
