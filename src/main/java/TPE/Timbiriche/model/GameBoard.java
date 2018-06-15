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
        if(flag) {
            movesLeft++;
            int squareIndex;
            boolean flagNotMid = false;
            if(move.getiFrom() == move.getiTo()){ //Arista horizontal
                int jMin = Math.min(move.getjFrom(), move.getjTo());
                if(move.getiFrom() == size -1) {
                    flagNotMid = true;
                    squareIndex = (move.getiFrom() - 1)*(size - 1) + jMin;
                }
                else if(move.getiFrom() == 0){
                    flagNotMid = true;
                    squareIndex = jMin;
                }
                else
                    squareIndex = move.getiFrom()*(size - 1) + jMin;

                squares[squareIndex]++;

                if(!flagNotMid) {
                    squareIndex -= (size - 1);
                    squares[squareIndex]++;
                }
            }
            else{  //Arista vertical
                int iMin = Math.min(move.getiFrom(), move.getiTo());
                if(move.getjFrom() == size -1) {
                    flagNotMid = true;
                    squareIndex = move.getjFrom() - 1 + iMin*(size - 1);
                }
                else if(move.getjFrom() == 0){
                    flagNotMid = true;
                    squareIndex = iMin*(size - 1);
                }
                else
                    squareIndex = move.getjFrom() + iMin*(size - 1);

                squares[squareIndex]++;
                if(!flagNotMid) {
                    squareIndex --;
                    squares[squareIndex]++;
                }
            }
        }
        return flag;
    }

    public boolean validMove(Move move){
        if(!isOver())
            if(move.getiFrom() >= 0 && move.getiFrom() < size && move.getjFrom() >= 0 && move.getjFrom() < size)
                if(move.getiTo() >= 0 && move.getiTo() < size && move.getjTo() >= 0 && move.getjTo() < size)
                    if( ( (move.getiTo() == move.getiFrom()) && (move.getjTo() != move.getjFrom()) ) || ( (move.getiTo() != move.getiFrom()) && (move.getjTo() == move.getjFrom()) ) )
                        if( Math.abs(move.getiFrom() - move.getiTo()) <= 1 && Math.abs(move.getjFrom() - move.getjTo()) <= 1 )
                            return !arcs.contains(new Arc(new Node(move.getiFrom(), move.getjFrom()), new Node(move.getiTo(), move.getjTo())));
        return false;
    }

    public int makeMove(Move move){
        if(validMove(move)){
            if(arcs.add(new Arc(new Node(move.getiFrom(), move.getjFrom()), new Node(move.getiTo(), move.getjTo())))){
                movesLeft--;
                int squareIndex;
                int result = 0;
                boolean flagNotMid = false;
                if(move.getiFrom() == move.getiTo()){  //Arista horizontal
                    int jMin = Math.min(move.getjFrom(), move.getjTo());
                    if(move.getiFrom() == size -1) {
                        flagNotMid = true;
                        squareIndex = (move.getiFrom() - 1)*(size - 1) + jMin;
                    }
                    else if(move.getiFrom() == 0){
                        flagNotMid = true;
                        squareIndex = jMin;
                    }
                    else{
                        squareIndex = move.getiFrom()*(size - 1) + jMin;
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
                else{  //Arista vertical
                    int iMin = Math.min(move.getiFrom(), move.getiTo());
                    if(move.getjFrom() == size -1) {
                        flagNotMid = true;
                        squareIndex = move.getjFrom() - 1 + iMin*(size - 1);
                    }
                    else if(move.getjFrom() == 0){
                        flagNotMid = true;
                        squareIndex = iMin*(size - 1);
                    }
                    else{
                        squareIndex = move.getjFrom() + iMin*(size - 1);
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
