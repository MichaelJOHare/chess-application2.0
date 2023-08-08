package org.michaeljohare.controller;

import org.michaeljohare.model.board.ChessBoard;
import org.michaeljohare.model.board.Square;
import org.michaeljohare.model.game.GameState;
import org.michaeljohare.model.moves.Move;
import org.michaeljohare.model.moves.MoveHistory;
import org.michaeljohare.model.moves.PromotionMove;
import org.michaeljohare.model.pieces.*;
import org.michaeljohare.model.player.PlayerColor;

import java.io.*;
import java.util.concurrent.CompletableFuture;

import static org.michaeljohare.model.board.ChessBoard.*;

public class StockfishController {
    private Process engineProcess;
    private BufferedReader processReader;
    private PrintWriter processWriter;
    private ChessBoard board;
    private MoveHistory move;
    private GameState gs;

    private static final String PATH = "src/main/resources/stockfish/stockfish-windows-x86-64-avx2.exe";

    public StockfishController(ChessBoard board, MoveHistory move, GameState gs) {
        this. board = board;
        this.move = move;
        this.gs = gs;
        if (startEngine()) {
            sendCommand("uci");
        } else {
            System.err.println("Failed to start Stockfish engine.");
        }
    }

    public boolean startEngine() {

        try {
            engineProcess = new ProcessBuilder(PATH).start();
            processReader = new BufferedReader(new InputStreamReader(engineProcess.getInputStream()));
            processWriter = new PrintWriter(engineProcess.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void stopEngine() {
        try {
            sendCommand("quit");
            if (processReader != null) {
                processReader.close();
            }
            if (processWriter != null) {
                processWriter.close();
            }
            if (engineProcess != null) {
                engineProcess.destroy();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public synchronized void sendCommand(String command) {
        processWriter.println(command);
        processWriter.flush();
    }

    public synchronized Move getMove() {
        String fen = toFEN();

        sendCommand("position fen " + fen);

        sendCommand("go movetime 1000");
        String bestMove = "";
        try {
            bestMove = getResponse();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return parseMove(bestMove);
    }

    public synchronized String getResponse() throws IOException {
        String line;
        StringBuilder response = new StringBuilder();
        String bestMove = "";
        while ((line = processReader.readLine()) != null) {
            response.append(line).append("\n");
            if (line.contains("bestmove")) {
                bestMove = line.split(" ")[1];
                break;
            }
        }
        return bestMove;
    }

    public Move parseMove(String algebraicMove) {

        String start = algebraicMove.substring(0, 2);
        String end = algebraicMove.substring(2, 4);

        Square startSquare = algebraicToSquare(start);
        Square endSquare = algebraicToSquare(end);

        ChessPiece movingPiece = board.getPieceAt(startSquare.getRow(), startSquare.getCol());

        ChessPiece capturedPiece = board.getPieceAt(endSquare.getRow(), endSquare.getCol());

        Move move;

        if (algebraicMove.length() > 4) {
            char promotionPieceChar = algebraicMove.charAt(4);
            PieceType promotionType = charToPieceType(promotionPieceChar);

            move = new PromotionMove(movingPiece, startSquare, endSquare, capturedPiece, promotionType, board);
        } else {
            move = new Move(movingPiece, startSquare, endSquare, capturedPiece, board);
        }

        return move;
    }

    public Square algebraicToSquare(String algebraic) {
        char col = algebraic.charAt(0);
        int row = Integer.parseInt(String.valueOf(algebraic.charAt(1)));

        int colIndex = col - 'a';
        int rowIndex = 8 - row;

        return new Square(rowIndex, colIndex);
    }

    public String toFEN() {
        StringBuilder fen = new StringBuilder();

        // 1. Piece placement
        for (int row = 0; row < 8; row++) {
            int emptySquares = 0;
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = board.getPieceAt(row, col);
                if (piece == null) {
                    emptySquares++;
                } else {
                    if (emptySquares != 0) {
                        fen.append(emptySquares);
                        emptySquares = 0;
                    }
                    fen.append(piece.pieceToFEN());
                }
            }
            if (emptySquares != 0) {
                fen.append(emptySquares);
            }
            if (row < 7) {
                fen.append("/");
            }
        }

        // 2. The side to move
        fen.append(gs.getCurrentPlayer().isWhite() ? " w " : " b ");

        // 3. Castling availability
        ChessPiece[][] chessBoard = board.getBoard();

        boolean anyCastlingAvailable = false;
        ChessPiece whiteKing = chessBoard[WHITE_MAJOR_PIECE_ROW][KING_COLUMN];
        if (whiteKing instanceof King && !((King) whiteKing).hasMoved()) {
            ChessPiece whiteKingRook = chessBoard[WHITE_MAJOR_PIECE_ROW][ROOK_COLUMN_1];
            ChessPiece whiteQueenRook = chessBoard[WHITE_MAJOR_PIECE_ROW][ROOK_COLUMN_2];
            if (whiteKingRook instanceof Rook && !((Rook) whiteKingRook).hasMoved()) {
                fen.append("K");
                anyCastlingAvailable = true;
            }
            if (whiteQueenRook instanceof Rook && !((Rook) whiteQueenRook).hasMoved()) {
                fen.append("Q");
                anyCastlingAvailable = true;
            }
        }

        ChessPiece blackKing = chessBoard[BLACK_MAJOR_PIECE_ROW][KING_COLUMN];
        if (blackKing instanceof King && !((King) blackKing).hasMoved()) {
            ChessPiece blackKingRook = chessBoard[BLACK_MAJOR_PIECE_ROW][ROOK_COLUMN_1];
            ChessPiece blackQueenRook = chessBoard[BLACK_MAJOR_PIECE_ROW][ROOK_COLUMN_2];
            if (blackKingRook instanceof Rook && !((Rook) blackKingRook).hasMoved()) {
                fen.append("k");
                anyCastlingAvailable = true;
            }
            if (blackQueenRook instanceof Rook && !((Rook) blackQueenRook).hasMoved()) {
                fen.append("q");
                anyCastlingAvailable = true;
            }
        }

        if (!anyCastlingAvailable) {
            fen.append(" - ");
        }

        // 4. En passant target square
        Square enPassantTarget = move.getEnPassantTarget();
        if (enPassantTarget != null) {
            fen.append(enPassantTarget).append(" ");
        } else {
            fen.append(" - ");
        }

        // 5. Halfmove clock
        fen.append(move.getHalfMoveClock()).append(" ");

        // 6. Fullmove number
        fen.append(move.getFullMoveNumber());

        return fen.toString();
    }

    private PieceType charToPieceType(char pieceChar) {
        switch (pieceChar) {
            case 'q': return PieceType.QUEEN;
            case 'r': return PieceType.ROOK;
            case 'b': return PieceType.BISHOP;
            case 'n': return PieceType.KNIGHT;
            default: throw new IllegalArgumentException("Invalid promotion piece char: " + pieceChar);
        }
    }

    public void cleanup() {
        stopEngine();
    }
}
