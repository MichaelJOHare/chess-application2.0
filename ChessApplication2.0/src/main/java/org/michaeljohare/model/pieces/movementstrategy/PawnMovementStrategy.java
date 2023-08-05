package org.michaeljohare.model.pieces.movementstrategy;

import org.michaeljohare.model.board.ChessBoard;
import org.michaeljohare.model.board.Square;
import org.michaeljohare.model.moves.Move;
import org.michaeljohare.model.moves.MoveHistory;
import org.michaeljohare.model.pieces.ChessPiece;
import org.michaeljohare.model.pieces.PieceType;

import java.util.ArrayList;
import java.util.List;

public class PawnMovementStrategy implements MovementStrategy {
    Move lastMove;
    Move tempMove;
    Square currentSquare;
    Square targetSquare;
    ChessPiece capturedPiece;


    @Override
    public List<Move> calculateLegalMoves(ChessBoard board, ChessPiece piece, MoveHistory move) {
        List<Move> legalMoves = new ArrayList<>();
        int row = piece.getCurrentSquare().getRow(), col = piece.getCurrentSquare().getCol();

        /*
         * White Pieces
         */

        if (piece.getPlayer().isWhite()) {
            if (row > 0 && board.isEmpty(row - 1, col)) {
                currentSquare = new Square(row, col);
                targetSquare = new Square(row - 1, col);
                tempMove = new Move(piece, currentSquare, targetSquare, null, board);
                move.makeMove(tempMove);
                if (!board.isKingInCheck(piece.getPlayer(), move)) {
                    legalMoves.add(tempMove);
                }
                move.undoMove();
                // First Move double move
                if (row == 6 && board.isEmpty(row - 2, col)) {
                    // May use piece.getCurrentSquare()
                    currentSquare = new Square(row, col);
                    targetSquare = new Square(row - 2, col);
                    tempMove = new Move(piece, currentSquare, targetSquare, null, board);
                    move.makeMove(tempMove);
                    if (!board.isKingInCheck(piece.getPlayer(), move)) {
                        legalMoves.add(tempMove);
                    }
                    move.undoMove();
                }
            }
            // Normal Captures
            if (row > 0 && col < 7 && board.isOccupiedByOpponent(row - 1, col + 1, piece.getPlayer())) {
                currentSquare = new Square(row, col);
                targetSquare = new Square(row - 1, col + 1);
                capturedPiece = board.getPieceAt(row - 1, col + 1);
                tempMove = new Move(piece, currentSquare, targetSquare, capturedPiece, board);
                move.makeMove(tempMove);
                if (!board.isKingInCheck(piece.getPlayer(), move)) {
                    legalMoves.add(tempMove);
                }
                move.undoMove();
            }
            if (row > 0 && col > 0 && board.isOccupiedByOpponent(row - 1, col - 1, piece.getPlayer())) {
                currentSquare = new Square(row, col);
                targetSquare = new Square(row - 1, col - 1);
                capturedPiece = board.getPieceAt(row - 1, col - 1);
                tempMove = new Move(piece, currentSquare, targetSquare, capturedPiece, board);
                move.makeMove(tempMove);
                if (!board.isKingInCheck(piece.getPlayer(), move)) {
                    legalMoves.add(tempMove);
                }
                move.undoMove();
            }
            /*
             En Passant Captures
             */
            // EP Capture Left
            lastMove = move.getLastMove();
            if (lastMove != null && lastMove.getPiece().getType().equals(PieceType.PAWN) &&
                    !lastMove.getPiece().getPlayer().isWhite() &&
                    Math.abs(lastMove.getStartSquare().getRow() - lastMove.getEndSquare().getRow()) == 2 &&
                    lastMove.getEndSquare().getCol() == col - 1) {
                currentSquare = new Square(row, col);
                targetSquare = new Square(row - 1, col - 1);
                capturedPiece = lastMove.getPiece();
                tempMove = new Move(piece, currentSquare, targetSquare, capturedPiece, board);
                move.makeMove(tempMove);
                if (!board.isKingInCheck(piece.getPlayer(), move)) {
                    legalMoves.add(tempMove);
                }
                move.undoMove();
            }
            // EP Capture Right
            if (lastMove != null && lastMove.getPiece().getType().equals(PieceType.PAWN) &&
                    !lastMove.getPiece().getPlayer().isWhite() &&
                    Math.abs(lastMove.getStartSquare().getRow() - lastMove.getEndSquare().getRow()) == 2 &&
                    lastMove.getEndSquare().getCol() == col + 1) {
                currentSquare = new Square(row, col);
                targetSquare = new Square(row - 1, col + 1);
                capturedPiece = lastMove.getPiece();
                tempMove = new Move(piece, currentSquare, targetSquare, capturedPiece, board);
                move.makeMove(tempMove);
                if (!board.isKingInCheck(piece.getPlayer(), move)) {
                    legalMoves.add(tempMove);
                }
                move.undoMove();
            }
        } else {

            /*
            Black Pieces
            */

            if (!piece.getPlayer().isWhite()) {
                if (row < 7 && board.isEmpty(row + 1, col)) {
                    currentSquare = new Square(row, col);
                    targetSquare = new Square(row + 1, col);
                    tempMove = new Move(piece, currentSquare, targetSquare, null, board);
                    move.makeMove(tempMove);
                    if (!board.isKingInCheck(piece.getPlayer(), move)) {
                        legalMoves.add(tempMove);
                    }
                    move.undoMove();
                    // First Move double move
                    if (row == 1 && board.isEmpty(row + 2, col)) {
                        currentSquare = new Square(row, col);
                        targetSquare = new Square(row + 2, col);
                        tempMove = new Move(piece, currentSquare, targetSquare, null, board);
                        move.makeMove(tempMove);
                        if (!board.isKingInCheck(piece.getPlayer(), move)) {
                            legalMoves.add(tempMove);
                        }
                        move.undoMove();
                    }
                }
                if (row < 7 && col < 7 && board.isOccupiedByOpponent(row + 1, col + 1, piece.getPlayer())) {
                    currentSquare = new Square(row, col);
                    targetSquare = new Square(row + 1, col + 1);
                    capturedPiece = board.getPieceAt(row + 1, col + 1);
                    tempMove = new Move(piece, currentSquare, targetSquare, capturedPiece, board);
                    move.makeMove(tempMove);
                    if (!board.isKingInCheck(piece.getPlayer(), move)) {
                        legalMoves.add(tempMove);
                    }
                    move.undoMove();
                }
                if (row < 7 && col > 0 && board.isOccupiedByOpponent(row + 1, col - 1, piece.getPlayer())) {
                    currentSquare = new Square(row, col);
                    targetSquare = new Square(row + 1, col - 1);
                    capturedPiece = board.getPieceAt(row + 1, col - 1);
                    tempMove = new Move(piece, currentSquare, targetSquare, capturedPiece, board);
                    move.makeMove(tempMove);
                    if (!board.isKingInCheck(piece.getPlayer(), move)) {
                        legalMoves.add(tempMove);
                    }
                    move.undoMove();
                }
            /*
             En Passant Captures
             */
                // EP Capture Left
                lastMove = move.getLastMove();
                if (lastMove != null && lastMove.getPiece().getType().equals(PieceType.PAWN) &&
                        lastMove.getPiece().getPlayer().isWhite() &&
                        Math.abs(lastMove.getStartSquare().getRow() - lastMove.getEndSquare().getRow()) == 2 &&
                        lastMove.getEndSquare().getCol() == col - 1) {
                    currentSquare = new Square(row, col);
                    targetSquare = new Square(row + 1, col - 1);
                    capturedPiece = lastMove.getPiece();
                    tempMove = new Move(piece, currentSquare, targetSquare, capturedPiece, board);
                    move.makeMove(tempMove);
                    if (!board.isKingInCheck(piece.getPlayer(), move)) {
                        legalMoves.add(tempMove);
                    }
                    move.undoMove();
                }
                // EP Capture Right
                if (lastMove != null && lastMove.getPiece().getType().equals(PieceType.PAWN) &&
                        lastMove.getPiece().getPlayer().isWhite() &&
                        Math.abs(lastMove.getStartSquare().getRow() - lastMove.getEndSquare().getRow()) == 2 &&
                        lastMove.getEndSquare().getCol() == col + 1) {
                    currentSquare = new Square(row, col);
                    targetSquare = new Square(row + 1, col + 1);
                    capturedPiece = lastMove.getPiece();
                    tempMove = new Move(piece, currentSquare, targetSquare, capturedPiece, board);
                    move.makeMove(tempMove);
                    if (!board.isKingInCheck(piece.getPlayer(), move)) {
                        legalMoves.add(tempMove);
                    }
                    move.undoMove();
                }
            }
        }
        return legalMoves;
    }

    public List<Move> calculateRawLegalMoves(ChessBoard board, ChessPiece piece, MoveHistory move) {
        List<Move> legalMoves = new ArrayList<>();
        int row = piece.getCurrentSquare().getRow(), col = piece.getCurrentSquare().getCol();

        /*
         * White Pieces
         */

        if (piece.getPlayer().isWhite()) {
            // Add forward moves
            if (row > 0 && board.isEmpty(row - 1, col)) {
                legalMoves.add(new Move(piece, new Square(row, col), new Square(row - 1, col), null, board));
            }
            if (row == 6 && board.isEmpty(row - 2, col)) {
                legalMoves.add(new Move(piece, new Square(row, col), new Square(row - 2, col), null, board));
            }

            // Add normal captures
            if (row > 0 && col < 7 && board.isOccupiedByOpponent(row - 1, col + 1, piece.getPlayer())) {
                legalMoves.add(new Move(piece, new Square(row, col), new Square(row - 1, col + 1), board.getPieceAt(row - 1, col + 1), board));
            }
            if (row > 0 && col > 0 && board.isOccupiedByOpponent(row - 1, col - 1, piece.getPlayer())) {
                legalMoves.add(new Move(piece, new Square(row, col), new Square(row - 1, col - 1), board.getPieceAt(row - 1, col - 1), board));
            }
            Move lastMove = move.getLastMove();
            if (lastMove != null && lastMove.getPiece().getType().equals(PieceType.PAWN) &&
                    !lastMove.getPiece().getPlayer().isWhite() &&
                    Math.abs(lastMove.getStartSquare().getRow() - lastMove.getEndSquare().getRow()) == 2 &&
                    lastMove.getEndSquare().getCol() == col - 1) {
                currentSquare = new Square(row, col);
                targetSquare = new Square(row - 1, col - 1);
                capturedPiece = lastMove.getPiece();
                tempMove = new Move(piece, currentSquare, targetSquare, capturedPiece, board);
                legalMoves.add(tempMove);
            }
            if (lastMove != null && lastMove.getPiece().getType().equals(PieceType.PAWN) &&
                    !lastMove.getPiece().getPlayer().isWhite() &&
                    Math.abs(lastMove.getStartSquare().getRow() - lastMove.getEndSquare().getRow()) == 2 &&
                    lastMove.getEndSquare().getCol() == col + 1) {
                currentSquare = new Square(row, col);
                targetSquare = new Square(row - 1, col + 1);
                capturedPiece = lastMove.getPiece();
                tempMove = new Move(piece, currentSquare, targetSquare, capturedPiece, board);
                legalMoves.add(tempMove);
            }
        }else {

            /*
            Black Pieces
            */

            // Add forward moves
            if (row < 7 && board.isEmpty(row + 1, col)) {
                legalMoves.add(new Move(piece, new Square(row, col), new Square(row + 1, col), null, board));
            }
            if (row == 1 && board.isEmpty(row + 2, col)) {
                legalMoves.add(new Move(piece, new Square(row, col), new Square(row + 2, col), null, board));
            }

            // Add normal captures
            if (row < 7 && col < 7 && board.isOccupiedByOpponent(row + 1, col + 1, piece.getPlayer())) {
                legalMoves.add(new Move(piece, new Square(row, col), new Square(row + 1, col + 1), board.getPieceAt(row + 1, col + 1), board));
            }
            if (row < 7 && col > 0 && board.isOccupiedByOpponent(row + 1, col - 1, piece.getPlayer())) {
                legalMoves.add(new Move(piece, new Square(row, col), new Square(row + 1, col - 1), board.getPieceAt(row + 1, col - 1), board));
            }
            Move lastMove = move.getLastMove();
            if (lastMove != null && lastMove.getPiece().getType().equals(PieceType.PAWN) &&
                    lastMove.getPiece().getPlayer().isWhite() &&
                    Math.abs(lastMove.getStartSquare().getRow() - lastMove.getEndSquare().getRow()) == 2 &&
                    lastMove.getEndSquare().getCol() == col - 1) {
                currentSquare = new Square(row, col);
                targetSquare = new Square(row + 1, col - 1);
                capturedPiece = lastMove.getPiece();
                tempMove = new Move(piece, currentSquare, targetSquare, capturedPiece, board);
                legalMoves.add(tempMove);
            }
            if (lastMove != null && lastMove.getPiece().getType().equals(PieceType.PAWN) &&
                    lastMove.getPiece().getPlayer().isWhite() &&
                    Math.abs(lastMove.getStartSquare().getRow() - lastMove.getEndSquare().getRow()) == 2 &&
                    lastMove.getEndSquare().getCol() == col + 1) {
                currentSquare = new Square(row, col);
                targetSquare = new Square(row + 1, col + 1);
                capturedPiece = lastMove.getPiece();
                tempMove = new Move(piece, currentSquare, targetSquare, capturedPiece, board);
                legalMoves.add(tempMove);
            }
        }
        return legalMoves;
    }
}
