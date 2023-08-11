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

    private void initialSetup() {
        board.placePiece(new Rook(board, Color.WHITE), new Position(0, 0));
        board.placePiece(new King(board, Color.WHITE), new Position(0, 4));
    }

}
