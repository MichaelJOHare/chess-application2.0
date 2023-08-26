package com.michaeljohare.controller;

import com.michaeljohare.model.board.Square;
import com.michaeljohare.model.game.GameState;
import com.michaeljohare.model.moves.*;
import com.michaeljohare.model.pieces.ChessPiece;
import com.michaeljohare.model.pieces.King;
import com.michaeljohare.model.pieces.PieceType;
import com.michaeljohare.model.pieces.Rook;
import com.michaeljohare.model.board.ChessBoard;

import javax.swing.*;
import java.io.*;
import java.util.concurrent.CompletableFuture;

import static com.michaeljohare.model.board.ChessBoard.*;

public class StockfishController {
    private final ChessBoard board;
    private final MoveHistory move;
    private final GameState gs;
    private final MoveHandler mh;
    private final GUIController guiController;
    private Process engineProcess;
    private BufferedReader processReader;
    private PrintWriter processWriter;

    public StockfishController(ChessBoard board, MoveHistory move, GameState gs, GUIController guiController, MoveHandler mh) {
        this. board = board;
        this.move = move;
        this.gs = gs;
        this.guiController = guiController;
        this.mh = mh;

        if (startEngine()) {
            sendCommand("uci");
            setSkillLevel();
        } else {
            System.err.println("Failed to start Stockfish engine.");
        }
    }

    private String getStockfishPath() {
        String os = System.getProperty("os.name").toLowerCase();
        String stockfishPath;

        if (os.contains("win")) {
            stockfishPath = "src/main/resources/stockfish/stockfish-windows-x86-64-avx2.exe";
        } else if (os.contains("mac")) {
            stockfishPath = "src/main/resources/stockfish/stockfish";
        } else {
            throw new RuntimeException("Unsupported operating system");
        }
        return stockfishPath;
    }

    private boolean startEngine() {

        try {
            engineProcess = new ProcessBuilder(getStockfishPath()).start();
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

    private void setSkillLevel() {
        int elo = gs.getStockfishElo();
        int skillLevel = (elo - 800) / 100;
        skillLevel = Math.max(0, Math.min(skillLevel, 20));
        sendCommand("setoption name Skill Level value " + skillLevel);
    }

    public synchronized Move getMove() {
        String fen = toFEN();

        sendCommand("position fen " + fen);

        sendCommand("go movetime 2000");
        String bestMove = "";
        try {
            bestMove = getResponse();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return parseMove(bestMove);
    }

    public void makeMove() {
        if (gs.isGameOver()) {
            return;
        }

        guiController.stockfishThinkingButtonText();
        gs.lockBoard();
        CompletableFuture.supplyAsync(this::getMove)
                .thenAccept(stockfishMove -> {
                    if (stockfishMove != null) {
                        SwingUtilities.invokeLater(() -> {
                            guiController.resetStockfishButtonText();

                            mh.finalizeMove(stockfishMove);

                            mh.handleCheckAndCheckmate();
                        });
                    } else {
                        guiController.stockfishGameOverButtonText();
                    }
                    gs.unlockBoard();
                })
                .exceptionally(ex -> {
                    System.err.println("Error executing move for Stockfish: " + ex.getMessage());
                    ex.printStackTrace();
                    gs.unlockBoard();
                    return null;
                });
    }

    public void getBestMove() {
        if (gs.isGameOver()) {
            return;
        }

        guiController.stockfishWaitingButtonText();
        CompletableFuture.supplyAsync(this::getMove)
                .thenAccept(move -> {
                    if (move != null) {
                        SwingUtilities.invokeLater(() -> {
                            guiController.resetStockfishButtonText();
                            guiController.clearHighlightedSquares();
                            guiController.setHighlightedSquaresStockfish(move);
                        });
                    } else {
                        guiController.stockfishGameOverButtonText();
                    }
                })
                .exceptionally(ex -> {
                    System.err.println("Error fetching move from Stockfish: " + ex.getMessage());
                    ex.printStackTrace();
                    return null;
                });
    }

    private synchronized String getResponse() throws IOException {
        String line;
        String bestMove = "";
        while ((line = processReader.readLine()) != null) {
            if (line.contains("bestmove")) {
                bestMove = line.split(" ")[1];
                break;
            }
        }
        return bestMove;
    }

    private Move parseMove(String algebraicMove) {

        //                   AKA Game is over
        if (algebraicMove.equals("(none)")) {
            return null;
        }

        String start = algebraicMove.substring(0, 2);
        String end = algebraicMove.substring(2, 4);

        Square startSquare = algebraicToSquare(start);
        Square endSquare = algebraicToSquare(end);

        ChessPiece movingPiece = board.getPieceAt(startSquare.getRow(), startSquare.getCol());

        // If stockfish is suggesting a 2 square king move it must be a castling move
        if (movingPiece instanceof King && Math.abs(startSquare.getCol() - endSquare.getCol()) == 2) {
            Square rookStartSquare;
            Square rookEndSquare;

            // King side castles
            if (end.equals("g1") || end.equals("g8")) {
                rookStartSquare = new Square(startSquare.getRow(), 7);
                rookEndSquare = new Square(startSquare.getRow(), 5);
            } else { // Queen side castles
                rookStartSquare = new Square(startSquare.getRow(), 0);
                rookEndSquare = new Square(startSquare.getRow(), 3);
            }

            Rook rook = (Rook) board.getPieceAt(rookStartSquare.getRow(), rookStartSquare.getCol());
            return new CastlingMove(movingPiece, rook, startSquare, endSquare, rookStartSquare, rookEndSquare, board);
        }

        ChessPiece capturedPiece = board.getPieceAt(endSquare.getRow(), endSquare.getCol());

        Move move;

        // The only algebraic move with length over 4 that stockfish suggests is a pawn promotion
        //    e.g. normally it's e2e4 etc.
        if (algebraicMove.length() > 4) {
            char promotionPieceChar = algebraicMove.charAt(4);
            PieceType promotionType = charToPieceType(promotionPieceChar);

            move = new PromotionMove(movingPiece, startSquare, endSquare, capturedPiece, promotionType, board);
            move.setPromotion(true);
        } else {
            move = new Move(movingPiece, startSquare, endSquare, capturedPiece, board);
        }

        return move;
    }

    private Square algebraicToSquare(String algebraic) {
        char col = algebraic.charAt(0);
        int row = Integer.parseInt(String.valueOf(algebraic.charAt(1)));

        int colIndex = col - 'a';
        int rowIndex = 8 - row;

        return new Square(rowIndex, colIndex);
    }

    private String toFEN() {
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
            fen.append(" ").append(enPassantTarget).append(" ");
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
