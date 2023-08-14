package Entities.Pieces;

import Entities.Boardgame.Board;
import Entities.Boardgame.Position;
import Entities.Chess.ChessPiece;
import Enums.Color;

public class Knight extends ChessPiece {
    public Knight(Board board, Color color) {
        super(board, color);
    }

    @Override
    public String toString() {
        return "C";
    }

    private boolean canMove(Position position) {
        ChessPiece p = (ChessPiece) getBoard().piece(position);
        return p == null || p.getColor() != getColor();
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getCols()];

        Position p = new Position(0, 0);

        // Para cima e para a direita
        p.setValues(position.getRow() + 2, position.getCol() + 1);
        if (getBoard().positionExists(p) && canMove(p)) {
            mat[p.getRow()][p.getCol()] = true;
        }

        // Para cima e para a esquerda
        p.setValues(position.getRow() + 2, position.getCol() - 1);
        if (getBoard().positionExists(p) && canMove(p)) {
            mat[p.getRow()][p.getCol()] = true;
        }

        // Para baixo e para a direita
        p.setValues(position.getRow() - 2, position.getCol() + 1);
        if (getBoard().positionExists(p) && canMove(p)) {
            mat[p.getRow()][p.getCol()] = true;
        }

        // Para baixo e para a esquerda
        p.setValues(position.getRow() - 2, position.getCol() - 1);
        if (getBoard().positionExists(p) && canMove(p)) {
            mat[p.getRow()][p.getCol()] = true;
        }

        // Para direita e para cima
        p.setValues(position.getRow() + 1, position.getCol() + 2);
        if (getBoard().positionExists(p) && canMove(p)) {
            mat[p.getRow()][p.getCol()] = true;
        }

        // Para direita e para baixo
        p.setValues(position.getRow() - 1, position.getCol() + 2);
        if (getBoard().positionExists(p) && canMove(p)) {
            mat[p.getRow()][p.getCol()] = true;
        }

        // Para esquerda e para cima
        p.setValues(position.getRow() + 1, position.getCol() - 2);
        if (getBoard().positionExists(p) && canMove(p)) {
            mat[p.getRow()][p.getCol()] = true;
        }

        // Para esquerda e para baixo
        p.setValues(position.getRow() - 1, position.getCol() - 2);
        if (getBoard().positionExists(p) && canMove(p)) {
            mat[p.getRow()][p.getCol()] = true;
        }

        return mat;
    }
}
