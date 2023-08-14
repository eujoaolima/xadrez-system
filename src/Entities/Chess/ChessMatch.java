package Entities.Chess;

import Entities.Boardgame.Board;
import Entities.Boardgame.Piece;
import Entities.Boardgame.Position;
import Entities.Pieces.King;
import Entities.Pieces.Pawn;
import Entities.Pieces.Rook;
import Enums.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChessMatch {
    private Board board;
    private int turn;
    private Color currentPlayer;
    private boolean check;
    private boolean checkMate;
    private ChessPiece enPassant;
    private ChessPiece promoted;
    private List<Piece> piecesOnTheBoard = new ArrayList<>();
    private List<Piece> capturedPieces = new ArrayList<>();

    public ChessMatch() {
        board = new Board(8, 8);
        turn = 1;
        currentPlayer = Color.WHITE;
        check = false;
        initialSetup();
    }

    public int getTurn() {
        return turn;
    }

    public Color getCurrentColor() {
        return currentPlayer;
    }

    public boolean getCheck() {
        return check;
    }

    public boolean getCheckMate() {
        return checkMate;
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

    public boolean[][] possibleMoves(ChessPosition sourcePosition) {
        Position position = sourcePosition.toPosition();
        validateSourcePosition(position);
        return board.piece(position).possibleMoves();
    }

    public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
        Position source = sourcePosition.toPosition();
        Position target = targetPosition.toPosition();
        validateSourcePosition(source);
        validateTargetPosition(source, target);
        Piece capturedPiece = makeMove(source, target);

        if (testCheck(currentPlayer)) {
            undoMove(source, target, capturedPiece);
            throw new ChessException("Você não pode se colocar em check!");
        }

        check = (testCheck(opponent(currentPlayer))) ? true : false;

        if (testCheckMate(opponent(currentPlayer))) {
            checkMate = true;
        } else {
            nextTurn();
        }

        return (ChessPiece) capturedPiece;
    }

    private Piece makeMove(Position source, Position target) {
        ChessPiece p = (ChessPiece) board.removePiece(source);
        p.increaseMoveCount();
        Piece capturedPiece = board.removePiece(target);
        board.placePiece(p, target);

        if (capturedPiece!= null) {
            piecesOnTheBoard.remove(capturedPiece);
            capturedPieces.add(capturedPiece);
        }

        return capturedPiece;
    }

    private void undoMove(Position source, Position target, Piece capturedPiece) {
        ChessPiece p = (ChessPiece) board.removePiece(target);
        p.decreaseMoveCount();
        board.placePiece(p, source);

        if (capturedPiece != null) {
            board.placePiece(capturedPiece, target);
            capturedPieces.remove(capturedPiece);
            piecesOnTheBoard.add(capturedPiece);
        }
    }

    private void validateSourcePosition(Position position) {
        if (!board.positionExists(position)) {
            throw new ChessException("Não existe peça nesta posição");
        }
        if(currentPlayer != ((ChessPiece)board.piece(position)).getColor()){
            throw new ChessException("Esta peça é do adversário!");
        }
        if(!board.piece(position).isThereAnyPossibleMove()) {
            throw new ChessException("Não existem movimentos possíveis para esta peça!");
        }

    }

    private void validateTargetPosition(Position source, Position target) {
        if (!board.piece(source).possibleMove(target)) {
            throw new ChessException("A peça escolhida não pode se mover para esta posição");
        }
    }

    private Color opponent(Color color) {
        return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    private ChessPiece king(Color color) {
        List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
        for (Piece piece : list) {
            if (piece instanceof King) {
                return (ChessPiece) piece;
            }
        }
        throw new IllegalArgumentException("Não existe nenhum rei " + color + " no tabuleiro!");
    }

    private boolean testCheck(Color color) {
        Position kingPosition = king(color).getChessPosition().toPosition();
        List<Piece> opponentPieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == opponent(color)).toList();
        for (Piece piece : opponentPieces) {
            boolean[][] mat = piece.possibleMoves();
            if(mat[kingPosition.getRow()][kingPosition.getCol()]) {
                return true;
            }
        }
        return false;
    }

    private boolean testCheckMate(Color color) {
        if(!testCheck(color)) {
            return false;
        }

        List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
        for (Piece piece : list) {
            boolean[][] mat = piece.possibleMoves();
            for (int i = 0; i < board.getRows(); i++) {
                for (int j = 0; j < board.getCols(); j++) {
                    if (mat[i][j]) {
                        Position source = ((ChessPiece)piece).getChessPosition().toPosition();
                        Position target = new Position(i, j);
                        Piece capturedPiece = makeMove(source, target);

                        boolean testCheck = testCheck(color);
                        undoMove(source, target, capturedPiece);

                        if (!testCheck) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private void placeNewPieces(char column, int row, ChessPiece piece) {
        board.placePiece(piece, new ChessPosition(column, row).toPosition());
        piecesOnTheBoard.add(piece);
    }

    private void initialSetup() {
        placeNewPieces('a', 2, new Pawn(board, Color.WHITE));
        placeNewPieces('b', 2, new Pawn(board, Color.WHITE));
        placeNewPieces('c', 2, new Pawn(board, Color.WHITE));
        placeNewPieces('d', 2, new Pawn(board, Color.WHITE));
        placeNewPieces('e', 2, new Pawn(board, Color.WHITE));
        placeNewPieces('f', 2, new Pawn(board, Color.WHITE));
        placeNewPieces('g', 2, new Pawn(board, Color.WHITE));
        placeNewPieces('h', 2, new Pawn(board, Color.WHITE));

        placeNewPieces('a', 1, new Rook(board, Color.WHITE));
        placeNewPieces('h', 1, new Rook(board, Color.WHITE));
        placeNewPieces('e', 1, new King(board, Color.WHITE));

        placeNewPieces('a', 8, new Rook(board, Color.BLACK));
        placeNewPieces('h', 8, new Rook(board, Color.BLACK));
        placeNewPieces('d', 8, new King(board, Color.BLACK));

        placeNewPieces('a', 7, new Pawn(board, Color.BLACK));
        placeNewPieces('b', 7, new Pawn(board, Color.BLACK));
        placeNewPieces('c', 7, new Pawn(board, Color.BLACK));
        placeNewPieces('d', 7, new Pawn(board, Color.BLACK));
        placeNewPieces('e', 7, new Pawn(board, Color.BLACK));
        placeNewPieces('f', 7, new Pawn(board, Color.BLACK));
        placeNewPieces('g', 7, new Pawn(board, Color.BLACK));
        placeNewPieces('h', 7, new Pawn(board, Color.BLACK));
    }

    private void nextTurn() {
        turn++;
        currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

}
