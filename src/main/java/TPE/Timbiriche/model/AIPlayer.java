package TPE.Timbiriche.model;

import java.io.*;
import java.util.LinkedList;

public class AIPlayer extends Player implements Serializable {

    private static final long serialVersionUID = 1L;

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

    public void calculateAndMakeMove(){
        //Minimax
    }

    public boolean makeDotFile(String fileName){
        if(lastMoveStates == null){
            return false;
        }
        PrintWriter writer = null;
        File file = new File(System.getProperty("user.dir") + "/target/dot-files" + fileName + ".dot");
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
        private int value;
        private boolean pruned;
        private boolean chosen;
        private boolean isMax;

        public DataMove(LinkedList<Move> moves, int value, boolean pruned, boolean chosen, boolean isMax) {
            this.moves = moves;
            this.value = value;
            this.pruned = pruned;
            this.chosen = chosen;
            this.isMax = isMax;
        }

        @Override
        public String toString() {
            StringBuilder result = new StringBuilder();
            result.append("{ ");
            for(Move move : moves){
                result.append(move.toString() + value + " ");
            }
            result.append("}");
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