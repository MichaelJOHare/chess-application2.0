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

import static org.michaeljohare.model.board.ChessBoard.BLACK_MAJOR_PIECE_ROW;
import static org.michaeljohare.model.board.ChessBoard.WHITE_MAJOR_PIECE_ROW;

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
            engineProcess.destroy();
            processReader.close();
            processWriter.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void sendCommand(String command) {
        processWriter.println(command);
        processWriter.flush();
    }

    public Move getMove() {
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

    public String getResponse() throws IOException {
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
        String castlingRights = getCastlingRights();
        fen.append(castlingRights.isEmpty() ? "-" : castlingRights).append(" ");

        // 4. En passant target square
        Square enPassantTarget = move.getEnPassantTarget();
        if (enPassantTarget != null) {
            fen.append(enPassantTarget).append(" ");
        } else {
            fen.append("- ");
        }

        // 5. Halfmove clock
        fen.append(move.getHalfMoveClock()).append(" ");

        // 6. Fullmove number
        fen.append(move.getFullMoveNumber());

        return fen.toString();
    }

    public String getCastlingRights() {
        StringBuilder castling = new StringBuilder();

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = board.getBoard()[row][col];
                if (piece instanceof King) {
                    if (piece.getPlayer().getColor() == PlayerColor.WHITE && !((PieceWithMoveStatus) piece).hasMoved()) {
                        // Check rooks for white's castling rights
                        for (int rcol = 0; rcol < 8; rcol++) {
                            ChessPiece rookPiece = board.getPieceAt(WHITE_MAJOR_PIECE_ROW, rcol);
                            if (rookPiece instanceof Rook && !((PieceWithMoveStatus) rookPiece).hasMoved()) {
                                if (rcol < col) {
                                    castling.append("Q");
                                } else {
                                    castling.append("K");
                                }
                            }
                        }
                    } else if (piece.getPlayer().getColor() == PlayerColor.BLACK && !((PieceWithMoveStatus) piece).hasMoved()) {
                        // Check rooks for black's castling rights
                        for (int rcol = 0; rcol < 8; rcol++) {
                            ChessPiece rookPiece = board.getPieceAt(BLACK_MAJOR_PIECE_ROW, rcol);
                            if (rookPiece instanceof Rook && !((PieceWithMoveStatus) rookPiece).hasMoved()) {
                                if (rcol < col) {
                                    castling.append("q");
                                } else {
                                    castling.append("k");
                                }
                            }
                        }
                    }
                }
            }
        }

        return castling.toString();
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
}
