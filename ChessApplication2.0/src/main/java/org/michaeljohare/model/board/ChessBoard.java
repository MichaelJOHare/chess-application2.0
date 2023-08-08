package org.michaeljohare.model.board;

import org.michaeljohare.model.moves.Move;
import org.michaeljohare.model.moves.MoveHistory;
import org.michaeljohare.model.pieces.*;
import org.michaeljohare.model.player.PieceManager;
import org.michaeljohare.model.player.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class ChessBoard {
    public static final int ROW_LENGTH = 8;
    public static final int COLUMN_LENGTH = 8;
    public static final int WHITE_PAWN_ROW = 6;
    public static final int BLACK_PAWN_ROW = 1;
    public static final int WHITE_MAJOR_PIECE_ROW = 7;
    public static final int BLACK_MAJOR_PIECE_ROW = 0;
    public static final int ROOK_COLUMN_1 = 0;
    public static final int ROOK_COLUMN_2 = 7;
    public static final int KNIGHT_COLUMN_1 = 1;
    public static final int KNIGHT_COLUMN_2 = 6;
    public static final int BISHOP_COLUMN_1 = 2;
    public static final int BISHOP_COLUMN_2 = 5;
    public static final int QUEEN_COLUMN = 3;
    public static final int KING_COLUMN = 4;
    private ChessPiece[][] board;
    private PieceManager pieceManager;


    public ChessBoard() {
        board = new ChessPiece[ROW_LENGTH][COLUMN_LENGTH];
    }

    public void init(Player player1, Player player2) {
        clearBoard();
        initializeBoard(player1, player2);
        initializePieceManager();
    }

    private void clearBoard() {
        for (int row = 0; row < COLUMN_LENGTH; row++) {
            for (int col = 0; col < ROW_LENGTH; col++) {
                board[row][col] = null;
            }
        }
    }

    private void initializeBoard(Player player1, Player player2) {
        initializeMajorPieces(player1, player2);
        initializePawns(player1, player2);
    }

    private void initializeMajorPieces(Player player1, Player player2) {
        Map<Integer, BiFunction<Square, Player, ChessPiece>> pieceMap = new HashMap<>();
        pieceMap.put(ROOK_COLUMN_1, Rook::new);
        pieceMap.put(ROOK_COLUMN_2, Rook::new);
        pieceMap.put(KNIGHT_COLUMN_1, Knight::new);
        pieceMap.put(KNIGHT_COLUMN_2, Knight::new);
        pieceMap.put(BISHOP_COLUMN_1, Bishop::new);
        pieceMap.put(BISHOP_COLUMN_2, Bishop::new);
        pieceMap.put(QUEEN_COLUMN, Queen::new);
        pieceMap.put(KING_COLUMN, King::new);

        for (int row = 0; row < COLUMN_LENGTH; row++) {
            for (int col = 0; col < ROW_LENGTH; col++) {
                if (row == WHITE_MAJOR_PIECE_ROW || row == BLACK_MAJOR_PIECE_ROW) {
                    Player player = (row == WHITE_MAJOR_PIECE_ROW) ? player1 : player2;
                    if (pieceMap.containsKey(col)) {
                        placePiece(row, col, pieceMap.get(col).apply(new Square(row, col), player));
                    }
                }
            }
        }
    }

    private void initializePawns(Player player1, Player player2) {
        initializePawnRow(BLACK_PAWN_ROW, player2);
        initializePawnRow(WHITE_PAWN_ROW, player1);
    }

    private void initializePawnRow(int row, Player player) {
        for (int col = 0; col < ROW_LENGTH; col++) {
            placePiece(row, col, new Pawn(new Square(row, col), player));
        }
    }

    private void placePiece(int row, int col, ChessPiece piece) {
        board[row][col] = piece;
    }

    public ChessPiece[][] getBoard() {
        return board;
    }

    public boolean isEmpty(int row, int col) {
        return board[row][col] == null;
    }

    public ChessPiece getPieceAt(int row, int col) {
        return board[row][col];
    }

    public void addPiece(ChessPiece piece) {
        board[piece.getCurrentSquare().getRow()][piece.getCurrentSquare().getCol()] = piece;
    }

    public void removePiece(ChessPiece piece) {
        board[piece.getCurrentSquare().getRow()][piece.getCurrentSquare().getCol()] = null;
    }

    public boolean isKingInCheck(Player player, MoveHistory move) {
        Square kingSquare = pieceManager.findKingSquare(player);
        for (ChessPiece piece : pieceManager.getAllOpposingPieces(player)) {
            List<Move> pieceMoves = piece.calculateRawLegalMoves(this, move);
            for (Move m : pieceMoves) {
                if (m.getPiece().isAlive() && m.getEndSquare().equals(kingSquare)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isSquareAttackedByOpponent(int row, int col, Player player) {
        for (ChessPiece piece : pieceManager.getAllOpposingPieces(player)) {
            List<Move> pieceMoves = piece.calculateRawLegalMoves(this, new MoveHistory());
            for (Move m : pieceMoves) {
                if (m.getPiece().isAlive() && m.getEndSquare().getRow() == row && m.getEndSquare().getCol() == col) {
                    return true;
                }
            }
        }
        return false;
    }

    public void initializePieceManager() {
        this.pieceManager = new PieceManager(this);
    }

    public PieceManager getPieceManager() {
        return pieceManager;
    }

    public boolean isOccupied(int row, int col) {
        ChessPiece piece = getPieceAt(row, col);
        return piece != null;
    }

    public boolean isOccupiedByOpponent(int row, int col, Player player) {
        ChessPiece piece = getPieceAt(row, col);
        return piece != null && !piece.getPlayer().equals(player);
    }

    public ChessBoard copy() {
        ChessBoard copiedBoard = new ChessBoard();

        for (int row = 0; row < ROW_LENGTH; row++) {
            for (int col = 0; col < COLUMN_LENGTH; col++) {
                ChessPiece currentPiece = this.board[row][col];
                if (currentPiece != null) {
                    ChessPiece copiedPiece = currentPiece.copy();
                    copiedBoard.board[row][col] = copiedPiece;
                }
            }
        }

        copiedBoard.initializePieceManager();

        return copiedBoard;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int row = 0; row < ROW_LENGTH; row++) {
            for (int col = 0; col < COLUMN_LENGTH; col++) {
                ChessPiece piece = board[row][col];
                if (piece != null) {
                    builder.append(piece).append("@").append(new Square(row, col)).append("\n");
                }
            }
        }
        return builder.toString();
    }
}
