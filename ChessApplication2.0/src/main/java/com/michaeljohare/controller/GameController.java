package com.michaeljohare.controller;

import com.michaeljohare.model.moves.MoveHandler;
import com.michaeljohare.model.moves.MoveHistory;
import com.michaeljohare.model.player.PieceManager;
import com.michaeljohare.model.board.ChessBoard;
import com.michaeljohare.model.game.GameState;
import com.michaeljohare.model.game.GameStateMemento;
import com.michaeljohare.model.moves.MoveResult;

import java.util.Stack;

public class GameController {

    private final GUIController guiController;
    private final StockfishController sfController;
    private final ChessBoard board;
    private final GameState gs;
    private final MoveHandler mh;
    private final MoveHistory move;
    private final Stack<GameStateMemento> mementos = new Stack<>();
    private PieceManager pm;

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

    public void handleClickToMove(int row, int col) {

        if (mh.isFirstClick()) {
            mh.handleSelectPieceClick(row, col);
        } else {
            mh.handleMovePieceClick(row, col);
        }

        if (gs.getCurrentPlayer().isStockfish()) {
            makeStockfishMove();
        }
    }

    public boolean handleDragStart(int row, int col) {
        return mh.handleDragStart(row, col);
    }

    public MoveResult handleDragDrop(int endRow, int endCol) {
        MoveResult result = mh.handleDragDrop(endRow, endCol);

        if (gs.getCurrentPlayer().isStockfish()) {
            makeStockfishMove();
        }
        return result;
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
