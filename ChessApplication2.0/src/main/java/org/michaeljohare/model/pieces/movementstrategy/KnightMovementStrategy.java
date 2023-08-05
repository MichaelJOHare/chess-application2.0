package org.michaeljohare.model.pieces.movementstrategy;

import org.michaeljohare.model.board.ChessBoard;
import org.michaeljohare.model.board.Square;
import org.michaeljohare.model.moves.Move;
import org.michaeljohare.model.moves.MoveHistory;
import org.michaeljohare.model.pieces.ChessPiece;
import org.michaeljohare.model.pieces.PieceWithMoveStatus;

import java.util.ArrayList;
import java.util.List;

public class KnightMovementStrategy implements MovementStrategy {

    Move tempMove;
    Square currentSquare;
    Square targetSquare;
    ChessPiece capturedPiece;

    @Override
    public List<Move> calculateLegalMoves(ChessBoard board, ChessPiece piece, MoveHistory move) {
        List<Move> legalMoves = new ArrayList<>();
        int row = piece.getCurrentSquare().getRow(), col = piece.getCurrentSquare().getCol();

        /*
        Knight Moves
         */

        // Knight North2East1
        if (row > 1 && col < 7 && board.isEmpty(row - 2, col + 1)) {
            currentSquare = piece.getCurrentSquare();
            targetSquare = new Square(row - 2, col + 1);
            tempMove = new Move(piece, currentSquare, targetSquare, null, board);
            move.makeMove(tempMove);
            if (!board.isKingInCheck(piece.getPlayer(), move)) {
                legalMoves.add(tempMove);
            }
            move.undoMove();
        }
        // Knight North1East2
        if (row > 0 && col < 6 && board.isEmpty(row - 1, col + 2)) {
            currentSquare = piece.getCurrentSquare();
            targetSquare = new Square(row - 1, col + 2);
            tempMove = new Move(piece, currentSquare, targetSquare, null, board);
            move.makeMove(tempMove);
            if (!board.isKingInCheck(piece.getPlayer(), move)) {
                legalMoves.add(tempMove);
            }
            move.undoMove();
        }
        // Knight North2West1
        if (row > 1 && col > 0 && board.isEmpty(row - 2, col - 1)) {
            currentSquare = piece.getCurrentSquare();
            targetSquare = new Square(row - 2, col - 1);
            tempMove = new Move(piece, currentSquare, targetSquare, null, board);
            move.makeMove(tempMove);
            if (!board.isKingInCheck(piece.getPlayer(), move)) {
                legalMoves.add(tempMove);
            }
            move.undoMove();
        }
        // Knight North1West2
        if (row > 0 && col > 1 && board.isEmpty(row - 1, col - 2)) {
            currentSquare = piece.getCurrentSquare();
            targetSquare = new Square(row - 1, col - 2);
            tempMove = new Move(piece, currentSquare, targetSquare, null, board);
            move.makeMove(tempMove);
            if (!board.isKingInCheck(piece.getPlayer(), move)) {
                legalMoves.add(tempMove);
            }
            move.undoMove();
        }
        // Knight South2East1
        if (row < 6 && col < 7 && board.isEmpty(row + 2, col + 1)) {
            currentSquare = piece.getCurrentSquare();
            targetSquare = new Square(row + 2, col + 1);
            tempMove = new Move(piece, currentSquare, targetSquare, null, board);
            move.makeMove(tempMove);
            if (!board.isKingInCheck(piece.getPlayer(), move)) {
                legalMoves.add(tempMove);
            }
            move.undoMove();
        }
        // Knight South1East2
        if (row < 7 && col < 6 && board.isEmpty(row + 1, col + 2)) {
            currentSquare = piece.getCurrentSquare();
            targetSquare = new Square(row + 1, col + 2);
            tempMove = new Move(piece, currentSquare, targetSquare, null, board);
            move.makeMove(tempMove);
            if (!board.isKingInCheck(piece.getPlayer(), move)) {
                legalMoves.add(tempMove);
            }
            move.undoMove();
        }
        // Knight South2West1
        if (row < 6 && col > 0 && board.isEmpty(row + 2, col - 1)) {
            currentSquare = piece.getCurrentSquare();
            targetSquare = new Square(row + 2, col - 1);
            tempMove = new Move(piece, currentSquare, targetSquare, null, board);
            move.makeMove(tempMove);
            if (!board.isKingInCheck(piece.getPlayer(), move)) {
                legalMoves.add(tempMove);
            }
            move.undoMove();
        }
        // Knight South1West2
        if (row < 7 && col > 1 && board.isEmpty(row + 1, col - 2)) {
            currentSquare = piece.getCurrentSquare();
            targetSquare = new Square(row + 1, col - 2);
            tempMove = new Move(piece, currentSquare, targetSquare, null, board);
            move.makeMove(tempMove);
            if (!board.isKingInCheck(piece.getPlayer(), move)) {
                legalMoves.add(tempMove);
            }
            move.undoMove();
        }

        /*
        Knight Captures
         */

        // Knight North2East1 Capture
        if (row > 1 && col < 7 && board.isOccupiedByOpponent(row - 2, col + 1, piece.getPlayer())) {
            currentSquare = piece.getCurrentSquare();
            targetSquare = new Square(row - 2, col + 1);
            capturedPiece = board.getPieceAt(row - 2, col + 1);
            tempMove = new Move(piece, currentSquare, targetSquare, capturedPiece, board);
            move.makeMove(tempMove);
            if (!board.isKingInCheck(piece.getPlayer(), move)) {
                legalMoves.add(tempMove);
            }
            move.undoMove();
        }
        // Knight North1East2 Capture
        if (row > 0 && col < 6 && board.isOccupiedByOpponent(row - 1, col + 2, piece.getPlayer())) {
            currentSquare = piece.getCurrentSquare();
            targetSquare = new Square(row - 1, col + 2);
            capturedPiece = board.getPieceAt(row - 1, col + 2);
            tempMove = new Move(piece, currentSquare, targetSquare, capturedPiece, board);
            move.makeMove(tempMove);
            if (!board.isKingInCheck(piece.getPlayer(), move)) {
                legalMoves.add(tempMove);
            }
            move.undoMove();
        }
        // Knight North2West1 Capture
        if (row > 1 && col > 0 && board.isOccupiedByOpponent(row - 2, col - 1, piece.getPlayer())) {
            currentSquare = piece.getCurrentSquare();
            targetSquare = new Square(row - 2, col - 1);
            capturedPiece = board.getPieceAt(row - 2, col - 1);
            tempMove = new Move(piece, currentSquare, targetSquare, capturedPiece, board);
            move.makeMove(tempMove);
            if (!board.isKingInCheck(piece.getPlayer(), move)) {
                legalMoves.add(tempMove);
            }
            move.undoMove();
        }
        // Knight North1West2 Capture
        if (row > 0 && col > 1 && board.isOccupiedByOpponent(row - 1, col - 2, piece.getPlayer())) {
            currentSquare = piece.getCurrentSquare();
            targetSquare = new Square(row - 1, col - 2);
            capturedPiece = board.getPieceAt(row - 1, col - 2);
            tempMove = new Move(piece, currentSquare, targetSquare, capturedPiece, board);
            move.makeMove(tempMove);
            if (!board.isKingInCheck(piece.getPlayer(), move)) {
                legalMoves.add(tempMove);
            }
            move.undoMove();
        }
        // Knight South2East1 Capture
        if (row < 6 && col < 7 && board.isOccupiedByOpponent(row + 2, col + 1, piece.getPlayer())) {
            currentSquare = piece.getCurrentSquare();
            targetSquare = new Square(row + 2, col + 1);
            capturedPiece = board.getPieceAt(row + 2, col + 1);
            tempMove = new Move(piece, currentSquare, targetSquare, capturedPiece, board);
            move.makeMove(tempMove);
            if (!board.isKingInCheck(piece.getPlayer(), move)) {
                legalMoves.add(tempMove);
            }
            move.undoMove();
        }
        // Knight South1East2 Capture
        if (row < 7 && col < 6 && board.isOccupiedByOpponent(row + 1, col + 2, piece.getPlayer())) {
            currentSquare = piece.getCurrentSquare();
            targetSquare = new Square(row + 1, col + 2);
            capturedPiece = board.getPieceAt(row + 1, col + 2);
            tempMove = new Move(piece, currentSquare, targetSquare, capturedPiece, board);
            move.makeMove(tempMove);
            if (!board.isKingInCheck(piece.getPlayer(), move)) {
                legalMoves.add(tempMove);
            }
            move.undoMove();
        }
        // Knight South2West1 Capture
        if (row < 6 && col > 0 && board.isOccupiedByOpponent(row + 2, col - 1, piece.getPlayer())) {
            currentSquare = piece.getCurrentSquare();
            targetSquare = new Square(row + 2, col - 1);
            capturedPiece = board.getPieceAt(row + 2, col - 1);
            tempMove = new Move(piece, currentSquare, targetSquare, capturedPiece, board);
            move.makeMove(tempMove);
            if (!board.isKingInCheck(piece.getPlayer(), move)) {
                legalMoves.add(tempMove);
            }
            move.undoMove();
        }
        // Knight South1West2 Capture
        if (row < 7 && col > 1 && board.isOccupiedByOpponent(row + 1, col - 2, piece.getPlayer())) {
            currentSquare = piece.getCurrentSquare();
            targetSquare = new Square(row + 1, col - 2);
            capturedPiece = board.getPieceAt(row + 1, col - 2);
            tempMove = new Move(piece, currentSquare, targetSquare, capturedPiece, board);
            move.makeMove(tempMove);
            if (!board.isKingInCheck(piece.getPlayer(), move)) {
                legalMoves.add(tempMove);
            }
            move.undoMove();
        }
        return legalMoves;
    }
}