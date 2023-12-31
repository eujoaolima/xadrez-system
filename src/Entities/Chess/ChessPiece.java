package Entities.Chess;

import Entities.Boardgame.Board;
import Entities.Boardgame.Piece;
import Entities.Boardgame.Position;
import Enums.Color;

public abstract class ChessPiece extends Piece {
    private Color color;
    private int moveCount;

    public ChessPiece(Board board, Color color) {
        super(board);
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    protected void increaseMoveCount() {
        moveCount++;
    }

    protected void decreaseMoveCount() {
        moveCount--;
    }

    public int getMoveCount() {
        return moveCount;
    }

    public ChessPosition getChessPosition() {
       return ChessPosition.fromPosition(position);
    }

    protected boolean isThereOpponentPiece(Position position) {
        ChessPiece p = (ChessPiece) getBoard().piece(position);
        return p != null && p.getColor() != color;
    }
}
