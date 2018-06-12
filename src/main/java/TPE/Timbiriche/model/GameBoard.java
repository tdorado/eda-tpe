package TPE.Timbiriche.model;

import java.util.HashSet;

public class GameBoard {
    private int squares[];
    private HashSet<Arc> arcs;
    private int movesLeft;
    private int size;

    public GameBoard(int size) {
        this.size = size;
        this.squares = new int[(size - 1) * (size - 1)];
        this.arcs = new HashSet<>();
        this.movesLeft = 2 * size * (size - 1);
        initializeGameBoard();
    }

    private void initializeGameBoard() {
        for(int i = 0; i < (size - 1) * (size - 1); i++){
            squares[i] = 4;
        }
    }

    public boolean undoMove(Move move){
        boolean flag = arcs.remove(new Arc(new Node(move.getiFrom(), move.getjFrom()), new Node(move.getiTo(), move.getjTo())));
        if(flag)
            movesLeft++;
        return flag;
    }

    public boolean validMove(Move move){
        if(!isOver())
            if(move.getiFrom() > 0 && move.getiFrom() < size && move.getjFrom() > 0 && move.getjFrom() < size)
                if(move.getiTo() > 0 && move.getiTo() < size && move.getjTo() > 0 && move.getjTo() < size)
                    if( ( (move.getiTo() == move.getiFrom()) && (move.getjTo() != move.getjFrom()) ) || ( (move.getiTo() != move.getiFrom()) && (move.getjTo() == move.getjFrom()) ) )
                        return arcs.contains(new Arc(new Node(move.getiFrom(), move.getjFrom()), new Node(move.getiTo(), move.getjTo())));
        return false;
    }

    public int makeMove(Move move){
        if(validMove(move)){
            if(arcs.add(new Arc(new Node(move.getiFrom(), move.getjFrom()), new Node(move.getiTo(), move.getjTo())))){
                int squareIndex;
                int result = 0;
                boolean flagNotMid = false;
                if(move.getiFrom() == move.getiTo()){
                    if(move.getiFrom() == size -1) {
                        flagNotMid = true;
                        squareIndex = (move.getiFrom() - 1)*(size - 1) + Math.min(move.getjFrom(), move.getjTo());
                    }
                    else if(move.getiFrom() == 0){
                        flagNotMid = true;
                        squareIndex = move.getiFrom()*(size - 1) + Math.min(move.getjFrom(), move.getjTo());
                    }
                    else{
                        squareIndex = move.getiFrom()*(size - 1) + Math.min(move.getjFrom(), move.getjTo());
                    }
                    squares[squareIndex]--;
                    if(squares[squareIndex] == 0){
                        result++;
                    }
                    if(!flagNotMid) {
                        squareIndex -= (size - 1);
                        squares[squareIndex]--;
                        if (squares[squareIndex] == 0) {
                            result++;
                        }
                    }
                    return result;
                }
                else{
                    if(move.getjFrom() == size -1) {
                        flagNotMid = true;
                        squareIndex = (move.getjFrom() - 1)*(size - 1) + Math.min(move.getiFrom(), move.getiTo());
                    }
                    else if(move.getjFrom() == 0){
                        flagNotMid = true;
                        squareIndex = move.getjFrom()*(size - 1) + Math.min(move.getiFrom(), move.getiTo());
                    }
                    else{
                        squareIndex = move.getjFrom()*(size - 1) + Math.min(move.getiFrom(), move.getiTo());
                    }
                    squares[squareIndex]--;
                    if(squares[squareIndex] == 0){
                        result++;
                    }
                    if(!flagNotMid) {
                        squareIndex --;
                        squares[squareIndex]--;
                        if (squares[squareIndex] == 0) {
                            result++;
                        }
                    }
                    return result;
                }
            }
        }
        return -1;
    }

    public boolean isOver(){
        return movesLeft == 0;
    }

}
