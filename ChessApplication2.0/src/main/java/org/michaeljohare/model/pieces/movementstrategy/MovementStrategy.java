package org.michaeljohare.model.pieces.movementstrategy;

import org.michaeljohare.model.board.ChessBoard;
import org.michaeljohare.model.moves.Move;
import org.michaeljohare.model.moves.MoveHistory;
import org.michaeljohare.model.pieces.ChessPiece;
import org.michaeljohare.model.player.PieceManager;

import java.util.List;

public interface MovementStrategy {
    List<Move> calculateLegalMoves(ChessBoard board, ChessPiece piece, MoveHistory move);
    List<Move> calculateRawLegalMoves(ChessBoard board, ChessPiece piece, MoveHistory move);
}