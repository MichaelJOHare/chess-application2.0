package ChessBoardTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.michaeljohare.model.board.ChessBoard;
import com.michaeljohare.model.board.Square;
import com.michaeljohare.model.pieces.ChessPiece;
import com.michaeljohare.model.pieces.Queen;
import com.michaeljohare.model.player.Player;
import com.michaeljohare.model.player.PlayerColor;
import com.michaeljohare.model.player.PlayerType;

import static org.junit.jupiter.api.Assertions.*;

public class ChessBoardTest {

    private ChessBoard board;

    @BeforeEach
    public void setup() {
        board = new ChessBoard();
    }

    @Test
    public void testGetPieceAt() {
        Player whitePlayer = new Player(PlayerColor.WHITE, PlayerType.HUMAN, "Mike");
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
        Player whitePlayer = new Player(PlayerColor.WHITE, PlayerType.HUMAN, "Mike");
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
