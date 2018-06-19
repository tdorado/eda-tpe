package TPE.Timbiriche.model;

import TPE.Timbiriche.model.exceptions.InvalidMoveException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

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

    public void makeMovePlayer(Move move) throws InvalidMoveException {
        if(makeMove(move) == -1){
            throw new InvalidMoveException();
        }
    }

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

    public boolean isAI(){
        return false;
    }

    public int getPoints() {
        return points;
    }

    void setPoints(int points) {
        this.points = points;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeInt(points);
        out.writeObject(game);
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException{
        ois.defaultReadObject();
        points = ois.readInt();
        game = (Game)ois.readObject();
    }
}