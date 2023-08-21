package org.michaeljohare.controller;

import org.michaeljohare.model.board.ChessBoard;
import org.michaeljohare.model.game.GameState;
import org.michaeljohare.model.game.GameStateMemento;
import org.michaeljohare.model.moves.MoveHandler;
import org.michaeljohare.model.moves.MoveHistory;
import org.michaeljohare.model.player.PieceManager;

import java.util.Stack;

public class GameController {

    private GUIController guiController;
    private StockfishController sfController;
    private ChessBoard board;
    private GameState gs;
    private MoveHandler mh;
    private MoveHistory move;
    private PieceManager pm;
    private Stack<GameStateMemento> mementos = new Stack<>();

    public GameController() {
        this.board = new ChessBoard();
        this.gs = new GameState(board);
        this.pm = board.getPieceManager();

        move = new MoveHistory();

        this.guiController = new GUIController(board, this);
        this.mh = new MoveHandler(board, move, gs, guiController, mementos, pm);
        sfController = new StockfishController(board, move, gs, guiController, mh);
        guiController.showGUI();

        initiateGame();
    }

    private void initiateGame() {
        if (gs.getCurrentPlayer().isStockfish()) {
            makeStockfishMove();
        }
    }

    public void handleSquareClick(int row, int col) {

        mh.handleSquareClick(row, col);

        if (gs.getCurrentPlayer().isStockfish()) {
            makeStockfishMove();
        }
    }

    public void handleUndoButtonClick() {
        mh.handleUndoMove();
    }

    public void askStockFish() {
        sfController.getBestMove();
    }

    private void makeStockfishMove() {
        sfController.makeMove();
    }
    public void handlePlayAgainButtonClick() {
        guiController.clearPreviousMoveHighlightedSquares();
        gs.init();
        board.init(gs.getPlayer1(), gs.getPlayer2());
        this.pm = board.getPieceManager();
        mh.setFirstClick(true);
        gs.setGameOver(false);
        move.resetMoveHistory();
        mementos.clear();
        guiController.updateGUI();
        initiateGame();
    }

    public void cleanup() {
        sfController.cleanup();
    }
}
