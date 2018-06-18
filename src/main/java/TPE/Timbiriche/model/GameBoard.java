package TPE.Timbiriche.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashSet;

public class GameBoard implements Serializable {

    private static final long serialVersionUID = 1L;

    private int size;
    private int squares[];
    private HashSet<MoveDone> movesDone;
    private HashSet<Move> possibleMoves;

    GameBoard(int size) {
        this.size = size;
        this.squares = new int[(size - 1) * (size - 1)];
        this.movesDone = new HashSet<>();
        this.possibleMoves = new HashSet<>();
        initializeGameBoard();
    }

    /**
     * Returns HashSet containing all moves done at the point of request.
     * @return HashSet of Move
     */
    public HashSet<MoveDone> getMovesDone(){
        return movesDone;
    }

    /**
     * Returns HashSet containing all possible moves at the point of request, not including the moves already done.
     * @return HashSet of Move
     */
    public HashSet<Move> getPossibleMoves(){
        return possibleMoves;
    }

    /**
     * Returns true if the game is over, false if not.
     * @return boolean
     */
    public boolean isOver(){
        return possibleMoves.isEmpty();
    }

    /**
     * Returns the size of the game board
     * @return int
     */
    public int getSize(){
        return size;
    }

    private void initializeGameBoard() {
        for(int i = 0; i < (size - 1) * (size - 1); i++){
            squares[i] = 4;
        }
        for(int row = 0; row < size; row++){
            for(int col = 0; col < size; col++){
                if (row == size - 1 && col != size -1) {
                    possibleMoves.add(new Move(row, col, row, col + 1));
                } else if (col == size - 1 && row != size -1) {
                    possibleMoves.add(new Move(row, col, row + 1, col));
                } else if(row != size -1 && col != size -1) {
                    possibleMoves.add(new Move(row, col, row, col + 1));
                    possibleMoves.add(new Move(row, col, row + 1, col));
                }
            }
        }
    }

    private boolean validMove(Move move){
        if(!isOver())
            return possibleMoves.contains(move);
        return false;
    }

    int undoMove(MoveDone moveDone){
        Move move = moveDone.getMove();
        if(movesDone.remove(moveDone)){
            possibleMoves.add(move);

            SquareIndex squareIndex = locateSquareIndex(move);
            int result = 0;

            if(move.isHorizontal()){
                if(squares[squareIndex.index] == 0){
                    result++;
                }
                squares[squareIndex.index]++;

                if(!squareIndex.flagNotMid) {
                    squareIndex.index -= (size - 1);
                    if(squares[squareIndex.index] == 0){
                        result++;
                    }
                    squares[squareIndex.index]++;
                }
            }
            else{
                if(squares[squareIndex.index] == 0){
                    result++;
                }
                squares[squareIndex.index]++;

                if(!squareIndex.flagNotMid) {
                    squareIndex.index--;
                    if(squares[squareIndex.index] == 0){
                        result++;
                    }
                    squares[squareIndex.index]++;
                }
            }
            return result;
        }
        return -1;
    }

    int makeMove(MoveDone moveDone){
        Move move = moveDone.getMove();
        if(validMove(move)){
            movesDone.add(moveDone);
            possibleMoves.remove(move);

            SquareIndex squareIndex = locateSquareIndex(move);
            int result = 0;

            if(move.isHorizontal()){  //Arista horizontal
                squares[squareIndex.index]--;
                if(squares[squareIndex.index] == 0){
                    result++;
                }
                if(!squareIndex.flagNotMid) {
                    squareIndex.index -= (size - 1);
                    squares[squareIndex.index]--;
                    if (squares[squareIndex.index] == 0) {
                        result++;
                    }
                }
                return result;
            }
            else{  //Arista vertical
                squares[squareIndex.index]--;
                if(squares[squareIndex.index] == 0){
                    result++;
                }
                if(!squareIndex.flagNotMid) {
                    squareIndex.index--;
                    squares[squareIndex.index]--;
                    if (squares[squareIndex.index] == 0) {
                        result++;
                    }
                }
                return result;
            }
        }
        return -1;
    }

    private SquareIndex locateSquareIndex(Move move){
        SquareIndex squareIndex = new SquareIndex();

        if(move.isHorizontal()){  //Arista horizontal
            if(move.getRowFrom() == size -1) {
                squareIndex.flagNotMid = true;
                squareIndex.index = (move.getRowFrom() - 1)*(size - 1) + Math.min(move.getColFrom(), move.getColTo());
            }
            else if(move.getRowFrom() == 0){
                squareIndex.flagNotMid = true;
                squareIndex.index = Math.min(move.getColFrom(), move.getColTo());
            }
            else{
                squareIndex.flagNotMid = false;
                squareIndex.index = move.getRowFrom()*(size - 1) + Math.min(move.getColFrom(), move.getColTo());
            }
        }
        else {  //Arista vertical
            if (move.getColFrom() == size - 1) {
                squareIndex.flagNotMid = true;
                squareIndex.index = move.getColFrom() - 1 + Math.min(move.getRowFrom(), move.getRowTo()) * (size - 1);
            } else if (move.getColFrom() == 0) {
                squareIndex.flagNotMid = true;
                squareIndex.index = Math.min(move.getRowFrom(), move.getRowTo()) * (size - 1);
            } else {
                squareIndex.flagNotMid = false;
                squareIndex.index = move.getColFrom() + Math.min(move.getRowFrom(), move.getRowTo()) * (size - 1);
            }
        }
        return squareIndex;
    }

    private class SquareIndex{
        private int index;
        private boolean flagNotMid;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeInt(size);
        out.writeObject(squares);
        out.writeObject(movesDone);
        out.writeObject(possibleMoves);
    }

    private void readObject(ObjectInputStream ois) throws IOException,ClassNotFoundException{
        ois.defaultReadObject();
        size = ois.readInt();
        squares = (int[])ois.readObject();
        movesDone = (HashSet<MoveDone>)ois.readObject();
        possibleMoves = (HashSet<Move>)ois.readObject();
    }
}
