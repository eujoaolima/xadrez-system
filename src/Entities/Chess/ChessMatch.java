package Entities.Chess;

import Entities.Boardgame.Board;
import Entities.Boardgame.Position;
import Entities.Pieces.King;
import Entities.Pieces.Rook;
import Enums.Color;

public class ChessMatch {
    private Board board;
    private int turn;
    private Color currentPlayer;
    private boolean check;
    private boolean checkMate;
    private ChessPiece enPassant;
    private ChessPiece promoted;

    public ChessMatch() {
        board = new Board(8, 8);
        initialSetup();
    }

    public ChessPiece[][] getPieces() {
        ChessPiece[][] pieces = new ChessPiece[board.getRows()][board.getCols()];
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getCols(); j++) {
                pieces[i][j] = (ChessPiece) board.piece(i, j);
            }
        }
        return pieces;
    }

    private void placeNewPieces(char column, int row, ChessPiece piece) {
        board.placePiece(piece, new ChessPosition(column, row).toPosition());
    }

    private void initialSetup() {
        placeNewPieces('c', 1, new Rook(board, Color.WHITE));
        placeNewPieces('c', 2, new Rook(board, Color.WHITE));
        placeNewPieces('d', 2, new Rook(board, Color.WHITE));
        placeNewPieces('e', 2, new Rook(board, Color.WHITE));
        placeNewPieces('e', 1, new Rook(board, Color.WHITE));
        placeNewPieces('d', 1, new King(board, Color.WHITE));

        placeNewPieces('c', 7, new Rook(board, Color.BLACK));
        placeNewPieces('c', 8, new Rook(board, Color.BLACK));
        placeNewPieces('d', 7, new Rook(board, Color.BLACK));
        placeNewPieces('e', 7, new Rook(board, Color.BLACK));
        placeNewPieces('e', 8, new Rook(board, Color.BLACK));
        placeNewPieces('d', 8, new King(board, Color.BLACK));
    }

}
