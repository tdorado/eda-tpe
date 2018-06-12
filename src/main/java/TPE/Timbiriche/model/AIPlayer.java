package TPE.Timbiriche.model;

public class AIPlayer extends Player {
    private int aiMode;
    private int aiModeParam;
    private boolean prune;

    public AIPlayer(int aiMode, int aiModeParam, boolean prune, Game game) {
        super(game);
        this.aiMode = aiMode;
        this.aiModeParam = aiModeParam;
        this.prune = prune;
    }

    public void calculateAndMakeMove(){
       //MiniMax
    }

    @Override
    public boolean isAI(){
        return true;
    }
}