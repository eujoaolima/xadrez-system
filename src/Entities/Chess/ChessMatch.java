package Entities.Chess;

import Entities.Boardgame.Board;
import Entities.Boardgame.Piece;
import Entities.Boardgame.Position;
import Entities.Pieces.*;
import Entities.piece.Queen;
import Enums.Color;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChessMatch {
    private Board board;
    private int turn;
    private Color currentPlayer;
    private boolean check;
    private boolean checkMate;
    private ChessPiece enPassantVulnerable;
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

    public ChessPiece getEnPassantVulnerable() {
        return enPassantVulnerable;
    }

    public ChessPiece getPromoted() {
        return promoted;
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

        ChessPiece movedPiece = (ChessPiece) board.piece(target);

        // Promoção
        promoted = null;
        if (movedPiece instanceof Pawn) {
            if ((movedPiece.getColor() == Color.WHITE && target.getRow() == 0)) {
                promoted = (ChessPiece)board.piece(target);
                promoted = replacePromotedPiece("Q");
            }

            if ((movedPiece.getColor() == Color.BLACK && target.getRow() == 7)) {

            }

        }


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

        // En Passant

        if (movedPiece instanceof Pawn && (target.getRow() == source.getRow() - 2 || target.getRow() == source.getRow() + 2)) {
            enPassantVulnerable = movedPiece;
        } else {
            enPassantVulnerable = null;
        }

        return (ChessPiece) capturedPiece;
    }

    public ChessPiece replacePromotedPiece(String type) {
        if (promoted == null) {
            throw new IllegalArgumentException("Não é possível promover a peça");
        }
        if (!type.equals("B") && !type.equals("C") && !type.equals("T") && !type.equals("Q")) {
            throw new InvalidParameterException("Não é possível promover a peça");
        }

        Position position = promoted.getChessPosition().toPosition();
        Piece p = board.removePiece(position);
        piecesOnTheBoard.remove(p);

        ChessPiece newPiece = newPiece(type, promoted.getColor());
        board.placePiece(newPiece, position);
        piecesOnTheBoard.add(newPiece);

        return newPiece;
    }

    private ChessPiece newPiece(String type, Color color) {
        if(type.equals("B")) {
            return new Bishop(board, color);
        }
        if(type.equals("C")) {
            return new Knight(board, color);
        }
        if(type.equals("T")) {
            return new Rook(board, color);
        }
        return new Queen(board, color);
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

        // En Passant

        if (p instanceof Pawn) {
            if (source.getCol() != target.getCol() && capturedPiece == null) {
                Position pawnPosition;
                if (p.getColor() == Color.WHITE) {
                    pawnPosition = new Position(target.getRow() + 1, target.getCol());
                } else {
                    pawnPosition = new Position(target.getRow() - 1, target.getCol());
                }

                capturedPiece = board.removePiece(pawnPosition);
                capturedPieces.add(capturedPiece);
                piecesOnTheBoard.remove(capturedPiece);
            }
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

        // En Passant

        if (p instanceof Pawn) {
            if (source.getCol() != target.getCol() && capturedPiece == enPassantVulnerable) {
                ChessPiece pawn = (ChessPiece)board.removePiece(target);
                Position pawnPosition;
                if (p.getColor() == Color.WHITE) {
                    pawnPosition = new Position(3, target.getCol());
                } else {
                    pawnPosition = new Position(4, target.getCol());
                }
                board.placePiece(pawn, pawnPosition);

                capturedPiece = board.removePiece(pawnPosition);
                capturedPieces.add(capturedPiece);
                piecesOnTheBoard.remove(capturedPiece);
            }
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
        placeNewPieces('a', 2, new Pawn(board, Color.WHITE, this));
        placeNewPieces('b', 2, new Pawn(board, Color.WHITE, this));
        placeNewPieces('c', 2, new Pawn(board, Color.WHITE, this));
        placeNewPieces('d', 2, new Pawn(board, Color.WHITE, this));
        placeNewPieces('e', 2, new Pawn(board, Color.WHITE, this));
        placeNewPieces('f', 2, new Pawn(board, Color.WHITE, this));
        placeNewPieces('g', 2, new Pawn(board, Color.WHITE, this));
        placeNewPieces('h', 2, new Pawn(board, Color.WHITE, this));

        placeNewPieces('b', 1, new Knight(board, Color.WHITE));
        placeNewPieces('g', 1, new Knight(board, Color.WHITE));
        placeNewPieces('c', 1, new Bishop(board, Color.WHITE));
        placeNewPieces('f', 1, new Bishop(board, Color.WHITE));
        placeNewPieces('a', 1, new Rook(board, Color.WHITE));
        placeNewPieces('h', 1, new Rook(board, Color.WHITE));
        placeNewPieces('e', 1, new King(board, Color.WHITE));
        placeNewPieces('d', 1, new Queen(board, Color.WHITE));

        placeNewPieces('c', 8, new Bishop(board, Color.BLACK));
        placeNewPieces('f', 8, new Bishop(board, Color.BLACK));
        placeNewPieces('b', 8, new Knight(board, Color.BLACK));
        placeNewPieces('g', 8, new Knight(board, Color.BLACK));
        placeNewPieces('a', 8, new Rook(board, Color.BLACK));
        placeNewPieces('h', 8, new Rook(board, Color.BLACK));
        placeNewPieces('d', 8, new King(board, Color.BLACK));
        placeNewPieces('e', 8, new Queen(board, Color.BLACK));

        placeNewPieces('a', 7, new Pawn(board, Color.BLACK, this));
        placeNewPieces('b', 7, new Pawn(board, Color.BLACK, this));
        placeNewPieces('c', 7, new Pawn(board, Color.BLACK, this));
        placeNewPieces('d', 7, new Pawn(board, Color.BLACK, this));
        placeNewPieces('e', 7, new Pawn(board, Color.BLACK, this));
        placeNewPieces('f', 7, new Pawn(board, Color.BLACK, this));
        placeNewPieces('g', 7, new Pawn(board, Color.BLACK, this));
        placeNewPieces('h', 7, new Pawn(board, Color.BLACK, this));
    }

    private void nextTurn() {
        turn++;
        currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

}
