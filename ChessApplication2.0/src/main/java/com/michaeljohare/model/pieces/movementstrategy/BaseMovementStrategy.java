package com.michaeljohare.model.pieces.movementstrategy;

import com.michaeljohare.model.board.ChessBoard;
import com.michaeljohare.model.moves.Move;
import com.michaeljohare.model.moves.MoveHistory;
import com.michaeljohare.model.pieces.ChessPiece;
import com.michaeljohare.model.player.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseMovementStrategy implements MovementStrategy {

    @Override
    public List<Move> calculateLegalMoves(ChessBoard board, ChessPiece piece, MoveHistory move) {
        List<Move> rawLegalMoves = calculateRawLegalMoves(board, piece, move);
        List<Move> legalMoves = new ArrayList<>();

        for (Move m : rawLegalMoves) {
            if (!wouldResultInCheck(board, piece, move, m)) {
                legalMoves.add(m);
            }
        }
        return legalMoves;
    }

    public boolean wouldResultInCheck(ChessBoard board, ChessPiece piece, MoveHistory move, Move m) {
        ChessBoard copiedBoard = board.copy();
        MoveHistory copiedMoveHistory = move.copy();
        ChessPiece copiedPiece = piece.copy();
        Player copiedPlayer = copiedPiece.getPlayer().copy();

        Move copiedMove = new Move(copiedPiece, m.getStartSquare(), m.getEndSquare(), copiedBoard.getPieceAt(m.getEndSquare().getRow(), m.getEndSquare().getCol()), copiedBoard);

        copiedMoveHistory.makeMove(copiedMove);
        copiedBoard.initializePieceManager();

        return copiedBoard.isKingInCheck(copiedPlayer, copiedMoveHistory, copiedBoard);
    }

    @Override
    public abstract List<Move> calculateRawLegalMoves(ChessBoard board, ChessPiece piece, MoveHistory move);
}
