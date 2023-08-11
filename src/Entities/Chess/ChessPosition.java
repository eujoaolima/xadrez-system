package Entities.Chess;

import Entities.Boardgame.Position;

public class ChessPosition {
    private char col;
    private int row;

    public ChessPosition(char col, int row) {
        if(col < 'a' || col > 'h' || row < 1 || row > 8) {
            throw new IllegalArgumentException("Erro ao criar a posição: Digite valores válidos de a1 até h8");
        }
        this.col = col;
        this.row = row;
    }

    public char getCol() {
        return col;
    }

    public void setCol(char col) {
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    protected Position toPosition() {
        return new Position(8 - row, col - 'a');
    }

    protected static ChessPosition fromPosition(Position position) {
        return new ChessPosition((char)('a' - position.getCol()), (8 - position.getRow()));
    }

    @Override
    public String toString() {
        return "" + col + row;
    }
}
