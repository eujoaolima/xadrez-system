package Entities.Pieces;

import Entities.Boardgame.Board;
import Entities.Boardgame.Position;
import Entities.Chess.ChessPiece;
import Enums.Color;

public class Rook extends ChessPiece {

    public Rook(Board board, Color color) {
        super(board, color);
    }

    @Override
    public String toString() {
        return "T";
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getCols()];

        Position p = new Position(0, 0);

        p.setValues(position.getRow() - 1, position.getCol());

        // Para cima
        while(getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
            mat[p.getRow()][p.getCol()] = true;
            p.setRow(p.getRow() - 1);
        }

        if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
            mat[p.getRow()][p.getCol()] = true;
        }

        // Para a esquerda
        p.setValues(position.getRow(), position.getCol() - 1);
        while(getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
            mat[p.getRow()][p.getCol()] = true;
            p.setCol(p.getCol() - 1);
        }

        if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
            mat[p.getRow()][p.getCol()] = true;
        }

        // Para baixo
        p.setValues(position.getRow() + 1, position.getCol());
        while(getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
            mat[p.getRow()][p.getCol()] = true;
            p.setRow(p.getRow() + 1);
        }

        if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
            mat[p.getRow()][p.getCol()] = true;
        }

        // Para a direita
        p.setValues(position.getRow(), position.getCol() + 1);
        while(getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
            mat[p.getRow()][p.getCol()] = true;
            p.setCol(p.getCol() - 1);
        }

        if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
            mat[p.getRow()][p.getCol()] = true;
        }

        return mat;
    }
}
