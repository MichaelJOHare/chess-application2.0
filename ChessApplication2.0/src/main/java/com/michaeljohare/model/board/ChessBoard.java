package com.michaeljohare.model.board;

import com.michaeljohare.model.moves.Move;
import com.michaeljohare.model.moves.MoveHistory;
import com.michaeljohare.model.pieces.*;
import com.michaeljohare.model.player.PieceManager;
import com.michaeljohare.model.player.Player;
import com.michaeljohare.model.player.PlayerColor;

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
    private final ChessPiece[][] board;
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
        if (player1.isWhite()) {
            initializeMajorPieces(player1, WHITE_MAJOR_PIECE_ROW, player2, BLACK_MAJOR_PIECE_ROW);
            initializePawnRows(player1, WHITE_PAWN_ROW, player2, BLACK_PAWN_ROW);
        } else {
            initializeMajorPieces(player1, BLACK_MAJOR_PIECE_ROW, player2, WHITE_MAJOR_PIECE_ROW);
            initializePawnRows(player1, BLACK_PAWN_ROW, player2, WHITE_PAWN_ROW);
        }
    }

    private void initializeMajorPieces(Player player1, int player1Row, Player player2, int player2Row) {
        Map<Integer, BiFunction<Square, Player, ChessPiece>> pieceMap = createPieceMap();

        placeMajorPiecesForRow(player1Row, player1, pieceMap);
        placeMajorPiecesForRow(player2Row, player2, pieceMap);
    }

    private Map<Integer, BiFunction<Square, Player, ChessPiece>> createPieceMap() {
        Map<Integer, BiFunction<Square, Player, ChessPiece>> pieceMap = new HashMap<>();
        pieceMap.put(ROOK_COLUMN_1, Rook::new);
        pieceMap.put(ROOK_COLUMN_2, Rook::new);
        pieceMap.put(KNIGHT_COLUMN_1, Knight::new);
        pieceMap.put(KNIGHT_COLUMN_2, Knight::new);
        pieceMap.put(BISHOP_COLUMN_1, Bishop::new);
        pieceMap.put(BISHOP_COLUMN_2, Bishop::new);
        pieceMap.put(QUEEN_COLUMN, Queen::new);
        pieceMap.put(KING_COLUMN, King::new);
        return pieceMap;
    }

    private void placeMajorPiecesForRow(int row, Player player, Map<Integer, BiFunction<Square, Player, ChessPiece>> pieceMap) {
        for (int col = 0; col < ROW_LENGTH; col++) {
            if (pieceMap.containsKey(col)) {
                placePiece(row, col, pieceMap.get(col).apply(new Square(row, col), player));
            }
        }
    }

    private void initializePawnRows(Player player1, int row1, Player player2, int row2) {
        initializePawnRow(row1, player1);
        initializePawnRow(row2, player2);
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

    public boolean isKingInCheck(Player player, MoveHistory move, ChessBoard board) {
        Square kingSquare = pieceManager.findKingSquare(player);
        for (ChessPiece piece : pieceManager.getAllOpposingPieces(player)) {
            List<Move> pieceMoves = piece.calculateRawLegalMoves(board, move);
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
            if (piece instanceof Pawn) {
                // Need to check for pawns specifically since their captures != their moves
                if (isSquareAttackedByPawn(piece.getCurrentSquare().getRow(),
                        piece.getCurrentSquare().getCol(), row, col, player)) {
                    return true;
                }
            } else {
                List<Move> pieceMoves = piece.calculateRawLegalMoves(this, new MoveHistory());
                for (Move m : pieceMoves) {
                    if (m.getPiece().isAlive() && m.getEndSquare().getRow() == row && m.getEndSquare().getCol() == col) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isSquareAttackedByPawn(int pawnRow, int pawnCol, int kingRow, int targetCol, Player player) {
        if (player.isWhite()) {
            return (pawnRow + 1 == kingRow && (pawnCol - 1 == targetCol || pawnCol + 1 == targetCol));
        } else {
            return (pawnRow - 1 == kingRow && (pawnCol - 1 == targetCol || pawnCol + 1 == targetCol));
        }
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
