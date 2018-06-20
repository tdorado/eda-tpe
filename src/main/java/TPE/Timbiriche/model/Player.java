package TPE.Timbiriche.model;

import TPE.Timbiriche.model.exceptions.InvalidMoveException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Class for the players of the game
 */
public class Player implements Serializable {

    private static final long serialVersionUID = 1L;

    int points;
    Game game;

    Player(int points, Game game) {
        this.points = points;
        this.game = game;
    }

    Player(Game game) {
        this(0, game);
    }

    /**
     * Method that makes a move of the player
     * @param move Move
     * @throws InvalidMoveException Invalid Move
     */
    public void makeMovePlayer(Move move) throws InvalidMoveException {
        if(makeMove(move) == -1){
            throw new InvalidMoveException();
        }
    }

    /**
     * Method that makes the move and saves it on the undoStack
     * @param move Move
     * @return -1 if invalid move or 0, 1 or 2 if the move made points
     */
    int makeMove(Move move){
        int result;
        result = game.getGameBoard().makeMove(new MoveDone(move, this));
        if(result == -1){
            return result;
        }
        points += result;
        game.getUndoStack().push(new MoveDone(move, this));
        if(result == 0) {
            game.changeCurrentPlayerTurn();
        }
        return result;
    }

    /**
     * Method that returns false in Player
     * @return boolean
     */
    public boolean isAI(){
        return false;
    }

    /**
     * Returns int of the points of the player
     * @return int
     */
    public int getPoints() {
        return points;
    }

    /**
     * Method that sets the points of the player
     * @param points int
     */
    void setPoints(int points) {
        this.points = points;
    }

    /**
     * Serialization method for saveGame and loadGame
     * @param out
     * @throws IOException
     */
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeInt(points);
        out.writeObject(game);
    }

    /**
     * Serialization method for saveGame and loadGame
     * @param ois
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException{
        ois.defaultReadObject();
        points = ois.readInt();
        game = (Game)ois.readObject();
    }
}