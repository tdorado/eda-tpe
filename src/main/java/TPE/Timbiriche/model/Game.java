package TPE.Timbiriche.model;

import TPE.Timbiriche.model.exceptions.WrongParametersException;

import java.io.*;
import java.util.Random;
import java.util.Stack;

public class Game implements Serializable {

    private static final long serialVersionUID = 1L;

    private GameBoard gameBoard;
    private Stack<MoveDone> undoStack;
    private int aiType;
    private Player player1;
    private Player player2;
    private int currentPlayerTurn;

    public Game(int size, int aiType, int aiMode, int aiModeParam, boolean prune){
        this.aiType = aiType;
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

    public boolean undoLastMove(){
        if(undoStack.isEmpty())
            return false;

        MoveDone moveDone = undoStack.pop();
        int pointsToRemove = gameBoard.undoMove(moveDone.getMove());
        moveDone.getPlayer().setPoints(moveDone.getPlayer().getPoints() - pointsToRemove);
        return true;
    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    Stack<MoveDone> getUndoStack() {
        return undoStack;
    }

    public Player getCurrentPlayer() {
        if(currentPlayerTurn == 1){
            return player1;
        }
        return player2;
    }

    void changeCurrentPlayerTurn() {
        if(currentPlayerTurn == 1){
            currentPlayerTurn = 2;
        }
        else{
            currentPlayerTurn = 1;
        }
    }

    public void saveGame(String fileName) throws IOException, ClassNotFoundException {
        FileManager.writeToFile(this,fileName);
    }

    public static Game loadGameFromFile(int size, int aiType, int aiMode, int aiModeParam, boolean prune, String fileName) throws IOException, ClassNotFoundException, WrongParametersException {
        Game game;
        game = (Game)FileManager.readFromFile(fileName);
        if (game != null){
            if(size != game.gameBoard.getSize()){
                throw new WrongParametersException();
            }
            if(game.aiType == 0){
                if(aiType == 1){
                    game.player1 = new AIPlayer(aiMode, aiModeParam, prune, game.player1.getPoints(), game);
                }
                else if(aiType == 2){
                    game.player2 = new AIPlayer(aiMode, aiModeParam, prune, game.player2.getPoints(), game);
                }
                else if(aiType == 3){
                    game.player1 = new AIPlayer(aiMode, aiModeParam, prune, game.player1.getPoints(), game);
                    game.player2 = new AIPlayer(aiMode, aiModeParam, prune, game.player2.getPoints(), game);
                }
            }
            else if(game.aiType == 1){
                if(aiType == 0){
                    game.player1 = new Player(game.player1.getPoints(), game);
                }
                else if(aiType == 1){
                    ((AIPlayer)game.player1).setAiMode(aiMode);
                    ((AIPlayer)game.player1).setAiModeParam(aiModeParam);
                    ((AIPlayer)game.player1).setPrune(prune);
                }
                else if(aiType == 2){
                    game.player1 = new Player(game.player1.getPoints(), game);
                    game.player2 = new AIPlayer(aiMode, aiModeParam, prune, game.player2.getPoints(), game);
                }
                else if(aiType == 3){
                    game.player2 = new AIPlayer(aiMode, aiModeParam, prune, game.player2.getPoints(), game);
                }
            }
            else if(game.aiType == 2){
                if(aiType == 0){
                    game.player2 = new Player(game.player2.getPoints(), game);
                }
                else if(aiType == 1){
                    game.player1 = new AIPlayer(aiMode, aiModeParam, prune, game.player1.getPoints(), game);
                    game.player2 = new Player(game.player2.getPoints(), game);
                }
                else if(aiType == 2){
                    ((AIPlayer)game.player2).setAiMode(aiMode);
                    ((AIPlayer)game.player2).setAiModeParam(aiModeParam);
                    ((AIPlayer)game.player2).setPrune(prune);
                }
                else if(aiType == 3){
                    game.player1 = new AIPlayer(aiMode, aiModeParam, prune, game.player1.getPoints(), game);
                }
            }
            else if(game.aiType == 3){
                if(aiType == 0){
                    game.player1 = new Player(game.player1.getPoints(), game);
                    game.player2 = new Player(game.player2.getPoints(), game);
                }
                else if(aiType == 1){
                    game.player2 = new Player(game.player2.getPoints(), game);
                }
                else if(aiType == 2){
                    game.player1 = new Player(game.player1.getPoints(), game);
                }
                else if(aiType == 3){
                    ((AIPlayer)game.player1).setAiMode(aiMode);
                    ((AIPlayer)game.player1).setAiModeParam(aiModeParam);
                    ((AIPlayer)game.player1).setPrune(prune);
                    ((AIPlayer)game.player2).setAiMode(aiMode);
                    ((AIPlayer)game.player2).setAiModeParam(aiModeParam);
                    ((AIPlayer)game.player2).setPrune(prune);
                }
            }
            game.aiType = aiType;
        }
        return game;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(gameBoard);
        out.writeObject(undoStack);
        out.writeInt(aiType);
        out.writeObject(player1);
        out.writeObject(player2);
        out.writeInt(currentPlayerTurn);
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        gameBoard = (GameBoard)ois.readObject();
        undoStack = (Stack<MoveDone>)ois.readObject();
        aiType = ois.readInt();
        player1 = (Player)ois.readObject();
        player2 = (Player)ois.readObject();
        currentPlayerTurn = ois.readInt();
    }
}
