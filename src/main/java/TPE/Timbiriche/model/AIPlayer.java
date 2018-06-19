package TPE.Timbiriche.model;

import TPE.Timbiriche.model.exceptions.MinimaxException;

import java.io.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

public class AIPlayer extends Player implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final int alpha = 10000;
    private static final int beta = -10000;

    private int aiMode;
    private int aiModeParam;
    private boolean prune;
    private transient MoveState lastMoveState;

    AIPlayer(int aiMode, int aiModeParam, boolean prune, int points, Game game) {
        super(points, game);
        this.aiMode = aiMode;
        this.aiModeParam = aiModeParam;
        this.prune = prune;
        this.lastMoveState = null;
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
        LinkedList<Move> moves = minimax();
        for(Move move : moves) {
            this.makeMove(move);
        }
    }

    private LinkedList<Move> minimax() throws MinimaxException {
        MoveState root = new MoveState(false);
        minimaxRec(game.deepCopy(), root, 0, false, true, game.getCurrentPlayerTurn());
        lastMoveState = root;
        System.out.println(lastMoveState.chosen);
        if(lastMoveState.chosen != null) {
            return lastMoveState.chosen.moves;
        }
        return null;
    }

    private int minimaxRec(Game gamePhase, MoveState previousMoveState, int depth, boolean sameLevel, boolean maxOrMin, int currentTurn) throws MinimaxException {
        if(gamePhase == null){
            throw new MinimaxException();
        }
        if(maxOrMin){
            if(sameLevel){
                boolean firstEntry = true;
                boolean notMoreMoves = false;
                if(gamePhase.getGameBoard().getPossibleMoves().isEmpty()){
                    notMoreMoves = true;
                }
                for (Move move : gamePhase.getGameBoard().getPossibleMoves()) {
                    Game gamePhaseCopy = gamePhase.deepCopy();
                    gamePhaseCopy.getCurrentPlayer().makeMove(move);
                    MoveState currentMoveState;
                    if(firstEntry){
                        currentMoveState = previousMoveState.children.getLast();
                        firstEntry = false;
                    }
                    else{
                        currentMoveState = new MoveState(true);
                        LinkedList<Move> moves = previousMoveState.children.getLast().moves;
                        for(int i = 0; i < moves.size() - 1; i++){
                            currentMoveState.moves.add(moves.get(i));
                        }
                        previousMoveState.children.add(currentMoveState);
                    }
                    currentMoveState.moves.add(move);
                    if (currentTurn == gamePhaseCopy.getCurrentPlayerTurn()) {
                        minimaxRec(gamePhaseCopy, previousMoveState, depth, true, true, currentTurn);
                    }
                    else{
                        if(previousMoveState.chosen == null){
                            previousMoveState.chosen = currentMoveState;
                        }
                        if(depth < aiModeParam){
                            currentMoveState.value = minimaxRec(gamePhaseCopy, currentMoveState, depth + 1, false, false, currentTurn);
                        }
                        else{
                            currentMoveState.value = ((AIPlayer)gamePhaseCopy.getNotCurrentPlayer()).heuristicValue();
                        }
                        if(previousMoveState.chosen.value < currentMoveState.value){
                            previousMoveState.chosen = currentMoveState;
                        }
                    }
                }
                if(notMoreMoves){
                    MoveState currentMoveState = previousMoveState.children.getLast();
                    if(previousMoveState.chosen == null){
                        previousMoveState.chosen = currentMoveState;
                    }
                    currentMoveState.value = ((AIPlayer)gamePhase.getNotCurrentPlayer()).heuristicValue();
                    if(previousMoveState.chosen.value < currentMoveState.value){
                        previousMoveState.chosen = currentMoveState;
                    }
                }
                return previousMoveState.chosen.value;
            }
            else {
                boolean notMoreMoves = false;
                if(gamePhase.getGameBoard().getPossibleMoves().isEmpty()){
                    notMoreMoves = true;
                }
                for (Move move : gamePhase.getGameBoard().getPossibleMoves()) {
                    Game gamePhaseCopy = gamePhase.deepCopy();
                    gamePhaseCopy.getCurrentPlayer().makeMove(move);
                    MoveState currentMoveState = new MoveState(true);
                    currentMoveState.moves.add(move);
                    previousMoveState.children.add(currentMoveState);
                    if (currentTurn == gamePhaseCopy.getCurrentPlayerTurn()) {
                        minimaxRec(gamePhaseCopy, previousMoveState, depth, true, true, currentTurn);
                    }
                    else{
                        if(previousMoveState.chosen == null){
                            previousMoveState.chosen = currentMoveState;
                        }
                        if(depth < aiModeParam){
                            currentMoveState.value = minimaxRec(gamePhaseCopy, currentMoveState, depth + 1, false, false, currentTurn);
                        }
                        else
                            currentMoveState.value = ((AIPlayer)gamePhaseCopy.getNotCurrentPlayer()).heuristicValue();
                        if(previousMoveState.chosen.value < currentMoveState.value){
                            previousMoveState.chosen = currentMoveState;
                        }
                    }
                }
                if(notMoreMoves){
                    MoveState currentMoveState = previousMoveState.children.getLast();
                    if(previousMoveState.chosen == null)
                        previousMoveState.chosen = currentMoveState;
                    currentMoveState.value = ((AIPlayer)gamePhase.getNotCurrentPlayer()).heuristicValue();
                    if(previousMoveState.chosen.value < currentMoveState.value){
                        previousMoveState.chosen = currentMoveState;
                    }
                }
                return previousMoveState.chosen.value;
            }
        }
        else{
            if(sameLevel){
                boolean firstEntry = true;
                boolean notMoreMoves = false;
                if(gamePhase.getGameBoard().getPossibleMoves().isEmpty()){
                    notMoreMoves = true;
                }
                for (Move move : gamePhase.getGameBoard().getPossibleMoves()) {
                    Game gamePhaseCopy = gamePhase.deepCopy();
                    gamePhaseCopy.getCurrentPlayer().makeMove(move);
                    MoveState currentMoveState;
                    if(firstEntry){
                        currentMoveState = previousMoveState.children.getLast();
                        firstEntry = false;
                    }
                    else{
                        currentMoveState = new MoveState(false);
                        LinkedList<Move> moves = previousMoveState.children.getLast().moves;
                        for(int i = 0; i < moves.size() - 1; i++)
                            currentMoveState.moves.add(moves.get(i));
                        previousMoveState.children.add(currentMoveState);
                    }
                    currentMoveState.moves.add(move);
                    if (currentTurn != gamePhaseCopy.getCurrentPlayerTurn()) {
                        minimaxRec(gamePhaseCopy, previousMoveState, depth, true, false, currentTurn);
                    }
                    else{
                        if(previousMoveState.chosen == null){
                            previousMoveState.chosen = currentMoveState;
                        }
                        if(prune) {
                            currentMoveState.value = ((AIPlayer) gamePhaseCopy.getCurrentPlayer()).heuristicValue();
                            if (previousMoveState.chosen.value <= currentMoveState.value) {
                                currentMoveState.pruned = true;
                            }
                            if(!currentMoveState.pruned) {
                                if (depth < aiModeParam) {
                                    currentMoveState.value = minimaxRec(gamePhaseCopy, currentMoveState, depth + 1, false, true, currentTurn);
                                }
                                else {
                                    currentMoveState.value = ((AIPlayer) gamePhaseCopy.getCurrentPlayer()).heuristicValue();
                                }
                                if (previousMoveState.chosen.value > currentMoveState.value) {
                                    previousMoveState.chosen = currentMoveState;
                                }
                            }
                        }
                        else{
                            if (depth < aiModeParam) {
                                currentMoveState.value = minimaxRec(gamePhaseCopy, currentMoveState, depth + 1, false, true, currentTurn);
                            } else {
                                currentMoveState.value = ((AIPlayer) gamePhaseCopy.getCurrentPlayer()).heuristicValue();
                            }
                            if (previousMoveState.chosen.value > currentMoveState.value) {
                                previousMoveState.chosen = currentMoveState;
                            }
                        }
                    }
                }
                if(notMoreMoves){
                    MoveState currentMoveState = previousMoveState.children.getLast();
                    if(previousMoveState.chosen == null){
                        previousMoveState.chosen = currentMoveState;
                    }
                    currentMoveState.value = ((AIPlayer)gamePhase.getCurrentPlayer()).heuristicValue();
                    if(previousMoveState.chosen.value > currentMoveState.value){
                        previousMoveState.chosen = currentMoveState;
                    }
                }
                return previousMoveState.chosen.value;
            }
            else {
                boolean notMoreMoves = false;
                if(gamePhase.getGameBoard().getPossibleMoves().isEmpty()){
                    notMoreMoves = true;
                }
                for (Move move : gamePhase.getGameBoard().getPossibleMoves()) {
                    Game gamePhaseCopy = gamePhase.deepCopy();
                    gamePhaseCopy.getCurrentPlayer().makeMove(move);
                    MoveState currentMoveState = new MoveState(false);
                    currentMoveState.moves.add(move);
                    previousMoveState.children.add(currentMoveState);
                    if (currentTurn != gamePhaseCopy.getCurrentPlayerTurn()) {
                        minimaxRec(gamePhaseCopy, previousMoveState, depth, true, false, currentTurn);
                    }
                    else{
                        if(previousMoveState.chosen == null){
                            previousMoveState.chosen = currentMoveState;
                        }
                        if(prune) {
                            currentMoveState.value = ((AIPlayer) gamePhaseCopy.getCurrentPlayer()).heuristicValue();
                            if (previousMoveState.chosen.value <= currentMoveState.value) {
                                currentMoveState.pruned = true;
                            }
                            if(!currentMoveState.pruned) {
                                if (depth < aiModeParam) {
                                    currentMoveState.value = minimaxRec(gamePhaseCopy, currentMoveState, depth + 1, false, true, currentTurn);
                                }
                                else {
                                    currentMoveState.value = ((AIPlayer) gamePhaseCopy.getCurrentPlayer()).heuristicValue();
                                }
                                if (previousMoveState.chosen.value > currentMoveState.value)
                                    previousMoveState.chosen = currentMoveState;
                            }
                        }
                        else{
                            if (depth < aiModeParam) {
                                currentMoveState.value = minimaxRec(gamePhaseCopy, currentMoveState, depth + 1, false, true, currentTurn);
                            } else {
                                currentMoveState.value = ((AIPlayer) gamePhaseCopy.getCurrentPlayer()).heuristicValue();
                            }
                            if (previousMoveState.chosen.value > currentMoveState.value) {
                                previousMoveState.chosen = currentMoveState;
                            }
                        }
                    }
                }
                if(notMoreMoves){
                    MoveState currentMoveState = previousMoveState.children.getLast();
                    if(previousMoveState.chosen == null)
                        previousMoveState.chosen = currentMoveState;
                    currentMoveState.value = ((AIPlayer)gamePhase.getCurrentPlayer()).heuristicValue();
                    if(previousMoveState.chosen.value > currentMoveState.value)
                        previousMoveState.chosen = currentMoveState;
                }
                return previousMoveState.chosen.value;
            }
        }
    }

    private int heuristicValue(){
        if(game.getGameBoard().isOver()){
            if(getOtherPlayer().getPoints() > points){
                return beta;
            }
            else if(getOtherPlayer().getPoints() < points){
                return alpha;
            }
            else{
                return 0;
            }
        }
        return points - getOtherPlayer().getPoints();
    }

    private Player getOtherPlayer() {
        if(game.getCurrentPlayer() == this){
            return game.getNotCurrentPlayer();
        }
        return game.getCurrentPlayer();
    }


    public boolean makeDotFile(String fileName){
        if(lastMoveState == null){
            return false;
        }
        PrintWriter writer;
        File file = new File(System.getProperty("user.dir") + "/target/" + fileName + ".dot");
        try {
            writer = new PrintWriter(file);
        } catch (FileNotFoundException e) {
            return false;
        }
        if(writer == null){
            return false;
        }

        int nodeNumber = 1;

        writer.println("digraph {");
        writer.println();

        writer.println("START [shape=box, style=filled, fillcolor=coral1]");
        for(MoveState moveState : lastMoveState.children){
            String moveString = moveState.toString();
            if(moveState == lastMoveState.chosen){
                writer.println( nodeNumber + "[label=\"" + moveString + "\" , shape=oval, style=filled, fillcolor=coral1]");
                writer.println( "START -> " + nodeNumber);
            }
            else{
                writer.println( nodeNumber + "[label=\"" + moveString + "\" , shape=oval, style=filled, fillcolor=white]");
                writer.println( "START -> " + nodeNumber);
            }
            nodeNumber = makeDotFileRec(writer, moveState, nodeNumber);
        }

        writer.println();
        writer.println("}");
        writer.close();
        return true;
    }

    private int makeDotFileRec(PrintWriter writer, MoveState moveState, int nodeNumber){
        String moveStringFrom = moveState.toString();
        int nodeNumberFrom = nodeNumber++;
        for(MoveState moveStateChild : moveState.children){
            String moveStringTo = moveStateChild.toString();
            if(moveStateChild == moveState.chosen){
                if(moveStateChild.isMax) {
                    writer.println( nodeNumber + "[label=\"" + moveStringTo + "\" ,shape=oval, style=filled, fillcolor=coral1]");
                    writer.println(nodeNumberFrom + " -> " + nodeNumber++);
                }
                else{
                    writer.println( nodeNumber + "[label=\"" + moveStringTo + "\" ,shape=box, style=filled, fillcolor=coral1]");
                    writer.println(nodeNumberFrom + " -> " + nodeNumber++);
                }
            }
            else{
                if(moveStateChild.isMax) {
                    writer.println( nodeNumber + "[label=\"" + moveStringTo + "\" , shape=oval, style=filled, fillcolor=white]");
                    writer.println(nodeNumberFrom + " -> " + nodeNumber++);
                }
                else{
                    if(moveStateChild.pruned){
                        writer.println( nodeNumber + "[label=\"" + moveStringTo + "\" , shape=box, style=filled, fillcolor=gray76]");
                        writer.println(nodeNumberFrom + " -> " + nodeNumber++);
                    }
                    else{
                        writer.println( nodeNumber + "[label=\"" + moveStringTo + "\" , shape=box, style=filled, fillcolor=white]");
                        writer.println(nodeNumberFrom + " -> " + nodeNumber++);
                    }
                }
            }
        }
        return nodeNumber;
    }

    private class MoveState{
        private LinkedList<Move> moves;
        private Integer value;
        private boolean pruned;
        private boolean isMax;
        private MoveState chosen;
        private LinkedList<MoveState> children;

        public MoveState(boolean isMax) {
            this.moves = new LinkedList<>();
            this.value = null;
            this.pruned = false;
            this.chosen = null;
            this.isMax = isMax;
            this.children = new LinkedList<>();
        }
        @Override
        public String toString() {
            StringBuilder result = new StringBuilder();
            result.append("{");
            for(Move move : moves){
                result.append("[" + move.toString() + "] ");
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