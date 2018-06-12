package TPE.Timbiriche.model;

import java.io.File;
import java.util.Stack;

public class Game {
    protected GameBoard gameBoard;
    protected Stack<Move> undoStack;
    private Player player1;
    private Player player2;

    public Game(int size, int aiType, int aiMode, int aiModeParam, boolean prune){
        this.undoStack = new Stack<>();
        this.gameBoard = new GameBoard(size);
        if(aiType == 0){
            this.player1 = new Player(this);
            this.player2 = new Player(this);
        }
        else if(aiType == 1){
            this.player1 = new AIPlayer(aiMode, aiModeParam, prune, this);
            this.player2 = new Player(this);
        }
        else if(aiType == 2){
            this.player1 = new Player(this);
            this.player2 = new AIPlayer(aiMode, aiModeParam, prune, this);
        }
        else{
            this.player1 = new AIPlayer(aiMode, aiModeParam, prune, this);
            this.player2 = new AIPlayer(aiMode, aiModeParam, prune, this);
        }
    }

    public Game(int size, int aiType, int aiMode, int aiModeParam, boolean prune, File file){
        //Lo mismo pero con la carga desde un File
        //Hay que cargar primero del archivo y despues hacer this con eso por si cambia el tipo de ai
        //loadData(file)
        this(size, aiType, aiMode, aiModeParam, prune);
    }

    public boolean undoLastMove(){
        Move move = undoStack.pop();
        if(move == null)
            return false;
        return gameBoard.undoMove(move);
    }
}
