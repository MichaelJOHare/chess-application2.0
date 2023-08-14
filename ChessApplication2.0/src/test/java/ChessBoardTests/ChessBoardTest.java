package ChessBoardTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.michaeljohare.model.board.ChessBoard;
import org.michaeljohare.model.board.Square;
import org.michaeljohare.model.pieces.ChessPiece;
import org.michaeljohare.model.pieces.Queen;
import org.michaeljohare.model.player.Player;
import org.michaeljohare.model.player.PlayerColor;

import static org.junit.jupiter.api.Assertions.*;

public class ChessBoardTest {

    private ChessBoard board;

    @BeforeEach
    public void setup() {
        board = new ChessBoard();
    }

    @Test
    public void testGetPieceAt() {
        Player whitePlayer = new Player(PlayerColor.WHITE,"mike", true);
        ChessPiece piece = new Queen(new Square(4, 4), whitePlayer);
        board.addPiece(piece);

        ChessPiece retrievedPiece = board.getPieceAt(4, 4);
        assertEquals(piece, retrievedPiece);
    }

    @Test
    public void testGetPieceAtEmptySquare() {
        assertNull(board.getPieceAt(0, 0));
    }

    @Test
    public void testCopy() {
        Player whitePlayer = new Player(PlayerColor.WHITE, "mike", true);
        ChessPiece piece = new Queen(new Square(4, 4), whitePlayer);
        board.addPiece(piece);

        ChessBoard copiedBoard = board.copy();

        assertNotSame(board, copiedBoard);

        ChessPiece originalPiece = board.getPieceAt(4, 4);
        ChessPiece copiedPiece = copiedBoard.getPieceAt(4, 4);

        assertEquals(originalPiece, copiedPiece);
        assertNotSame(originalPiece, copiedPiece);
    }

    @Test
    public void testCopyEmptySquare() {
        ChessBoard copiedBoard = board.copy();

        assertNull(copiedBoard.getPieceAt(0, 0));
    }
}
