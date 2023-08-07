package org.michaeljohare.model.player;

import org.michaeljohare.model.board.ChessBoard;
import org.michaeljohare.model.board.Square;
import org.michaeljohare.model.pieces.ChessPiece;
import org.michaeljohare.model.pieces.PieceType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PieceManager {
    private Map<Player, List<ChessPiece>> piecesByPlayer;

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

    public List<ChessPiece> getAllOpposingPieces(Player player) {
        return piecesByPlayer.entrySet().stream()
                .filter(entry -> !entry.getKey().equals(player))
                .flatMap(entry -> entry.getValue().stream())
                .collect(Collectors.toList());
    }
}
