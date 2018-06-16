package TPE.Timbiriche.model;

import java.util.LinkedList;

public class AIPlayer extends Player {
    private int aiMode;
    private int aiModeParam;
    private boolean prune;

    public AIPlayer(int aiMode, int aiModeParam, boolean prune, Game game) {
        super(game);
        this.aiMode = aiMode;
        this.aiModeParam = aiModeParam;
        this.prune = prune;
    }

    public void calculateAndMakeMove(){
        // A este metodo llamamos para que la ai elija y haga su movimiento
        int result;
        Move move = new Move(0, 0, 0, 0, this);
        result = game.getGameBoard().makeMove(move);
        points += result;
        game.getUndoStack().push(move);
        game.changeCurrentPlayerTurn();
    }

    public boolean makeDotFile(){
        return false;
    }

    @Override
    public boolean isAI(){
        return true;
    }

    private class GameBoardState{
        private GameBoard gameboard;
        private LinkedList<GameBoardState> children;

        public GameBoardState(GameBoard gameboard) {
            this.gameboard = gameboard;
            this.children = new LinkedList<>();
        }
    }
}