package com.michaeljohare.model.pieces.movementstrategy;

import com.michaeljohare.model.moves.Move;
import com.michaeljohare.model.moves.MoveHistory;
import com.michaeljohare.model.pieces.ChessPiece;
import com.michaeljohare.model.board.ChessBoard;

import java.util.List;

public interface MovementStrategy {
    List<Move> calculateLegalMoves(ChessBoard board, ChessPiece piece, MoveHistory move);
    List<Move> calculateRawLegalMoves(ChessBoard board, ChessPiece piece, MoveHistory move);
}