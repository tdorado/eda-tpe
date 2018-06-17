package TPE.Timbiriche.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.LinkedList;

public class AIPlayer extends Player implements Serializable {

    private static final long serialVersionUID = 1L;

    private int aiMode;
    private int aiModeParam;
    private boolean prune;

    AIPlayer(int aiMode, int aiModeParam, boolean prune, int points, Game game) {
        super(points, game);
        this.aiMode = aiMode;
        this.aiModeParam = aiModeParam;
        this.prune = prune;
    }

    AIPlayer(int aiMode, int aiModeParam, boolean prune, Game game) {
        this(aiMode, aiModeParam, prune, 0, game);
    }

    public void setAiMode(int aiMode) {
        this.aiMode = aiMode;
    }

    public void setAiModeParam(int aiModeParam) {
        this.aiModeParam = aiModeParam;
    }

    public void setPrune(boolean prune) {
        this.prune = prune;
    }

    public void calculateAndMakeMove(){
        // A este metodo llamamos para que la ai elija y haga su movimiento
        /*int result;
        Move move = new Move(0, 0, 0, 0, this);
        result = game.getGameBoard().makeMove(move);
        points += result;
        game.getUndoStack().push(move);
        game.changeCurrentPlayerTurn();*/
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

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeInt(points);
        out.writeObject(game);
        out.writeInt(aiMode);
        out.writeInt(aiModeParam);
        out.writeBoolean(prune);
    }

    private void readObject(ObjectInputStream ois) throws IOException,ClassNotFoundException{
        ois.defaultReadObject();
        points = ois.readInt();
        game = (Game)ois.readObject();
        aiMode = ois.readInt();
        aiModeParam = ois.readInt();
        prune = ois.readBoolean();
    }
}