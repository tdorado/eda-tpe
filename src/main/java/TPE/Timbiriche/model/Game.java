package TPE.Timbiriche.model;

import java.io.File;
import java.util.Random;
import java.util.Stack;

public class Game {
    private GameBoard gameBoard;
    private Stack<Move> undoStack;
    private Player player1;
    private Player player2;
    private int currentPlayerTurn;

    public Game(int size, int aiType, int aiMode, int aiModeParam, boolean prune){
        this.undoStack = new Stack<>();
        this.gameBoard = new GameBoard(size);

        Random random = new Random();
        this.currentPlayerTurn = random.nextInt(3-1) + 1;

        if(aiType == 0){
            this.player1 = new Player(this);
            this.player2 = new Player(this);
        }
        else if(aiType == 1){
            this.currentPlayerTurn = 1;
            this.player1 = new AIPlayer(aiMode, aiModeParam, prune, this);
            this.player2 = new Player(this);
        }
        else if(aiType == 2){
            this.currentPlayerTurn = 1;
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
        if(undoStack.isEmpty())
            return false;
        Move move = undoStack.pop();
        move.getPlayer().setPoints(move.getPlayer().getPoints() -1);
        return gameBoard.undoMove(move);
    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    public Stack<Move> getUndoStack() {
        return undoStack;
    }

    public Player getCurrentPlayer() {
        if(currentPlayerTurn == 1){
            return player1;
        }
        return player2;
    }

    public void changeCurrentPlayerTurn() {
        if(currentPlayerTurn == 1){
            currentPlayerTurn = 2;
        }
        else{
            currentPlayerTurn = 1;
        }
    }


}
