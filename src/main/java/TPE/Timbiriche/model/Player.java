package TPE.Timbiriche.model;

import TPE.Timbiriche.model.exceptions.InvalidMoveException;

public class Player {
    int points;
    Game game;

    public Player(Game game) {
        this.points = 0;
        this.game = game;
    }

    public int getPoints() {
        return points;
    }


    /**
     *
     * Method that makes the player movement.
     *
     * @param iFrom
     * @param jFrom
     * @param iTo
     * @param jTo
     * @throws InvalidMoveException
     */
    public void makeMove(int iFrom, int jFrom, int iTo, int jTo) throws InvalidMoveException {
        int result;
        Move move = new Move(iFrom, jFrom, iTo, jTo, this);
        result = game.getGameBoard().makeMove(move);
        if(result == -1){
            throw new InvalidMoveException();
        }
        else{
            points += result;
            game.getUndoStack().push(move);
        }
        if(result == 0) //Unicamente cambia de turno si la jugada no gano puntos
            game.changeCurrentPlayerTurn();
    }

    public boolean isAI(){
        return false;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}