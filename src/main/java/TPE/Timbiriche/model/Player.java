package TPE.Timbiriche.model;

import TPE.Timbiriche.model.exceptions.InvalidMoveException;

public class Player {
    private int points;
    private Game game;

    public Player(Game game) {
        this.points = 0;
        this.game = game;
    }

    public int getPoints() {
        return points;
    }

    public void makeMove(int iFrom, int jFrom, int iTo, int jTo) throws InvalidMoveException {
        int result;
        Move move = new Move(iFrom, jFrom, iTo, jTo);
        result = game.gameBoard.makeMove(move);
        if(result == -1){
            throw new InvalidMoveException();
        }
        else{
            points += result;
            game.undoStack.push(move);
        }
    }

    public boolean isAI(){
        return false;
    }
}