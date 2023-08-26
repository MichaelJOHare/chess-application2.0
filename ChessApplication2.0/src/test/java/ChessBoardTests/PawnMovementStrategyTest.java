package ChessBoardTests;

import static org.junit.jupiter.api.Assertions.*;
import static com.michaeljohare.model.player.PlayerType.HUMAN;

import org.junit.jupiter.api.Test;
import com.michaeljohare.model.board.ChessBoard;
import com.michaeljohare.model.board.Square;
import com.michaeljohare.model.moves.MoveHistory;
import com.michaeljohare.model.pieces.ChessPiece;
import com.michaeljohare.model.pieces.King;
import com.michaeljohare.model.pieces.Pawn;
import com.michaeljohare.model.player.Player;
import com.michaeljohare.model.player.PlayerColor;

public class PawnMovementStrategyTest {

    @Test
    public void testWouldResultInCheckWithBlockedPawn() {
        ChessBoard board = new ChessBoard();
        Player playerWhite = new Player(PlayerColor.WHITE, HUMAN, "Mike");
        Player playerBlack = new Player(PlayerColor.BLACK, HUMAN, "Bob");
        ChessPiece whitePawn = new Pawn(new Square(6, 1), playerWhite);
        ChessPiece whiteKing = new King(new Square(7, 0), playerWhite);
        ChessPiece blackPawn = new Pawn(new Square(5, 1), playerBlack);

        board.addPiece(whitePawn);
        board.addPiece(whiteKing);
        board.addPiece(blackPawn);

        MoveHistory moveHistory = new MoveHistory();

        try {
            blackPawn.calculateLegalMoves(board, moveHistory);
        } catch (NullPointerException e) {
            fail("NullPointerException thrown!");
        }
    }
}
