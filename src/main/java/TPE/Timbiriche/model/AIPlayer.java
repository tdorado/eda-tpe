package TPE.Timbiriche.model;

import TPE.Timbiriche.model.exceptions.MinimaxException;

import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;

public class AIPlayer extends Player implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final int alpha = 10000;
    private static final int beta = -10000;

    private int aiMode;
    private int aiModeParam;
    private boolean prune;
    private LinkedList<MoveState> lastMoveStates;

    AIPlayer(int aiMode, int aiModeParam, boolean prune, int points, Game game) {
        super(points, game);
        this.aiMode = aiMode;
        this.aiModeParam = aiModeParam;
        this.prune = prune;
        this.lastMoveStates = null;
    }

    AIPlayer(int aiMode, int aiModeParam, boolean prune, Game game) {
        this(aiMode, aiModeParam, prune, 0, game);
    }

    @Override
    public boolean isAI(){
        return true;
    }

    void setAiMode(int aiMode) {
        this.aiMode = aiMode;
    }

    void setAiModeParam(int aiModeParam) {
        this.aiModeParam = aiModeParam;
    }

    void setPrune(boolean prune) {
        this.prune = prune;
    }

    public void calculateAndMakeMove() throws MinimaxException {
        Move move = minimax();
        this.makeMove(move);
    }

    private Move minimax() throws MinimaxException {
        MoveState root = new MoveState(new DataMove(false));
        minimaxRec(game.deepCopy(), root, 0);
        lastMoveStates = root.children;
        for(MoveState moveState : root.children){
            if(moveState.dataMove.chosen){
                return moveState.dataMove.moves.getFirst();
            }
        }
        return null;
    }

    private int minimaxRec(Game gamePhase, MoveState father, int depth) throws MinimaxException {
        if(gamePhase == null){
            throw new MinimaxException();
        }
        if(game.getCurrentPlayer() == this){
            DataMove maxMove = null;
            for(Move move : game.getGameBoard().getPossibleMoves()){
                MoveState currentMoveState = new MoveState(new DataMove(true));
                while(game.getCurrentPlayer() == this){
                    game.getCurrentPlayer().makeMove(move);
                    currentMoveState.dataMove.moves.add(move);
                }
                if(depth < aiModeParam){
                    currentMoveState.dataMove.value = minimaxRec(gamePhase.deepCopy(), currentMoveState, depth + 1);
                }
                else{
                    currentMoveState.dataMove.value = heuristicValue();
                }

                if(maxMove == null){
                    maxMove = currentMoveState.dataMove;
                }
                else{
                    if(maxMove.value < currentMoveState.dataMove.value){
                        maxMove = currentMoveState.dataMove;
                    }
                }
                father.children.add(currentMoveState);
            }
            maxMove.chosen = true;
            return maxMove.value;
        }
        else{
            DataMove minMove = null;
            for(Move move : game.getGameBoard().getPossibleMoves()){
                MoveState currentMoveState = new MoveState(new DataMove(false));
                while(game.getNotCurrentPlayer() == this){
                    game.getCurrentPlayer().makeMove(move);
                    currentMoveState.dataMove.moves.add(move);
                }
                if(prune){
                    if(depth < aiModeParam){
                        if(minMove == null){
                            minMove = currentMoveState.dataMove;
                        }
                        else{
                            currentMoveState.dataMove.value = heuristicValue();
                            if(minMove.value < currentMoveState.dataMove.value){
                                currentMoveState.dataMove.pruned = true;
                            }
                        }
                        if(!currentMoveState.dataMove.pruned) {
                            currentMoveState.dataMove.value = minimaxRec(gamePhase.deepCopy(), currentMoveState, depth + 1);
                        }
                        if(minMove.value > currentMoveState.dataMove.value){
                            minMove = currentMoveState.dataMove;
                        }
                    }
                }
                else{
                    if(depth < aiModeParam){
                        currentMoveState.dataMove.value = minimaxRec(gamePhase.deepCopy(), currentMoveState, depth + 1);
                    }
                    else{
                        currentMoveState.dataMove.value = heuristicValue();
                    }

                    if(minMove == null){
                        minMove = currentMoveState.dataMove;
                    }
                    else{
                        if(minMove.value > currentMoveState.dataMove.value){
                            minMove = currentMoveState.dataMove;
                        }
                    }
                }
                father.children.add(currentMoveState);
            }
            minMove.chosen = true;
            return minMove.value;
        }
    }

    private int heuristicValue(){
        if(game.getGameBoard().isOver()){
            if(game.getNotCurrentPlayer().getPoints() > points){
                return beta;
            }
            else if(game.getNotCurrentPlayer().getPoints() < points){
                return alpha;
            }
            else{
                return 0;
            }
        }
        return points - game.getNotCurrentPlayer().getPoints();
    }

    boolean makeDotFile(String fileName){
        if(lastMoveStates == null){
            return false;
        }
        PrintWriter writer = null;
        File file = new File(System.getProperty("user.dir") + "/target/" + fileName + ".dot");
        try {
            writer = new PrintWriter(file);
        } catch (FileNotFoundException e) {
            return false;
        }
        if(writer == null){
            return false;
        }

        writer.println("digraph {");
        writer.println();

        writer.println("START [shape=box, fillcolor=coral1]");
        for(MoveState moveState : lastMoveStates){
            String moveString = moveState.dataMove.toString();
            if(moveState.dataMove.chosen){
                writer.println( moveString + " [shape=oval, fillcolor=coral1]");
                writer.println( "START -> " + moveString);
            }
            else{
                writer.println(moveString + " [shape=oval, fillcolor=white]");
                writer.println( "START -> " + moveString);
            }
            makeDotFileRec(writer, moveState);
        }

        writer.println();
        writer.println("}");
        writer.close();
        return true;
    }

    private void makeDotFileRec(PrintWriter writer, MoveState moveState){
        String moveStringFrom = moveState.dataMove.toString();
        for(MoveState moveStateChild : moveState.children){
            String moveStringTo = moveStateChild.dataMove.toString();
            if(moveStateChild.dataMove.chosen){
                if(moveStateChild.dataMove.isMax) {
                    writer.println(moveStringTo + " [shape=oval, fillcolor=coral1]");
                    writer.println(moveStringFrom + " -> " + moveStringTo);
                }
                else{
                    writer.println(moveStringTo + " [shape=box, fillcolor=coral1]");
                    writer.println(moveStringFrom + " -> " + moveStringTo);
                }
            }
            else{
                if(moveStateChild.dataMove.isMax) {
                    writer.println(moveStringTo + " [shape=oval, fillcolor=white]");
                    writer.println(moveStringFrom + " -> " + moveStringTo);
                }
                else{
                    if(moveStateChild.dataMove.pruned){
                        writer.println(moveStringTo + " [shape=box, fillcolor=gray76]");
                        writer.println(moveStringFrom + " -> " + moveStringTo);
                    }
                    else{
                        writer.println(moveStringTo + " [shape=box, fillcolor=white]");
                        writer.println(moveStringFrom + " -> " + moveStringTo);
                    }
                }
            }
        }
    }

    private class MoveState{
        private DataMove dataMove;
        private LinkedList<MoveState> children;

        public MoveState(DataMove dataMove) {
            this.dataMove = dataMove;
            this.children = new LinkedList<>();
        }
    }

    private class DataMove{
        private LinkedList<Move> moves;
        private Integer value;
        private boolean pruned;
        private boolean chosen;
        private boolean isMax;

        public DataMove(boolean isMax) {
            this.moves = new LinkedList<>();
            this.value = null;
            this.pruned = false;
            this.chosen = false;
            this.isMax = isMax;
        }

        @Override
        public String toString() {
            StringBuilder result = new StringBuilder();
            result.append("{");
            for(Move move : moves){
                result.append(move.toString() + " ");
            }
            result.append(value + "}");
            return result.toString();
        }
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeInt(points);
        out.writeObject(game);
        out.writeInt(aiMode);
        out.writeInt(aiModeParam);
        out.writeBoolean(prune);
    }

    private void readObject(ObjectInputStream ois) throws IOException,ClassNotFoundException{
        ois.defaultReadObject();
        points = ois.readInt();
        game = (Game)ois.readObject();
        aiMode = ois.readInt();
        aiModeParam = ois.readInt();
        prune = ois.readBoolean();
    }
}