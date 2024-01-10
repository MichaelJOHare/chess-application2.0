package com.michaeljohare.model.player;

import com.michaeljohare.model.moves.Move;
import com.michaeljohare.model.moves.PromotionMove;
import com.michaeljohare.model.board.ChessBoard;
import com.michaeljohare.model.board.Square;
import com.michaeljohare.model.pieces.ChessPiece;
import com.michaeljohare.model.pieces.PieceType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PieceManager {
    private final Map<Player, List<ChessPiece>> piecesByPlayer;

    public PieceManager(ChessBoard board) {
        piecesByPlayer = new HashMap<>();
        initPieces(board);
    }

    private void initPieces(ChessBoard board) {
        for (ChessPiece[] row : board.getBoard()) {
            for (ChessPiece piece : row) {
                if (piece != null) {
                    piecesByPlayer.computeIfAbsent(piece.getPlayer(), p -> new ArrayList<>()).add(piece);
                }
            }
        }
    }

    public List<ChessPiece> getPlayerPieces(Player player) {
        return piecesByPlayer.get(player);
    }

    public void removePiece(ChessPiece piece) {
        Player owner = piece.getPlayer();
        List<ChessPiece> playerPieces = piecesByPlayer.get(owner);
        if (playerPieces != null) {
            playerPieces.remove(piece);
        }
    }

    public void addPiece(ChessPiece piece) {
        Player owner = piece.getPlayer();
        List<ChessPiece> playerPieces = piecesByPlayer.computeIfAbsent(owner, p -> new ArrayList<>());
        playerPieces.add(piece);
    }

    public Square findKingSquare(Player player) {
        List<ChessPiece> pieces = piecesByPlayer.get(player);
        if (pieces != null) {
            for (ChessPiece piece : pieces) {
                if (piece.getType() == PieceType.KING) {
                    return piece.getCurrentSquare();
                }
            }
        }
        return null;
    }

    public void handlePromotion(Move move) {
        if (move.isPromotion()) {
            removePiece(((PromotionMove) move).getOriginalPiece());
            addPiece(((PromotionMove) move).getPromotedPiece());
        }
    }

    public void handleUndoPromotion(Move move) {
        if (move.isPromotion()) {
            removePiece(((PromotionMove) move).getPromotedPiece());
            addPiece(((PromotionMove) move).getOriginalPiece());
        }
    }

    public List<ChessPiece> getOpposingPieces(Player player) {
        return piecesByPlayer.entrySet().stream()
                .filter(entry -> !entry.getKey().equals(player))
                .flatMap(entry -> entry.getValue().stream())
                .collect(Collectors.toList());
    }
}
