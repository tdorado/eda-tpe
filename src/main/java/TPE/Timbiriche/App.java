package TPE.Timbiriche;

import TPE.Timbiriche.model.*;
import TPE.Timbiriche.model.exceptions.MinimaxException;
import TPE.Timbiriche.view.Board;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import javax.xml.soap.Node;

public class App extends Application {
    public static Game game;
    private int cont = 0;
    private Move[] n =new Move[2];

    public static void main( String[] args )
    {
        game = null;

        int size = 0, aiType = 0, aiMode = 0, aiModeParam = 0;
        boolean prune = false, argumentFail = false, loadFromFile = false;
        String fileName = null;

        if(args.length == 4 || args.length == 6 || args.length == 10 || args.length == 12) {
            if (args[0].equals("-size")) {
                size = Integer.parseInt(args[1]);
                if (size < 1 && size > 30) {
                    argumentFail = true;
                }
            }
            else{
                argumentFail = true;
            }
            if (args[2].equals("-ai")) {
                aiType = Integer.parseInt(args[3]);
                if (aiType < 0 && aiType > 3) {
                    argumentFail = true;
                }
            }
            else{
                argumentFail = true;
            }
            if(args.length > 4) {
                if(args.length == 6){
                    if (args[4].equals("-load")) {
                        loadFromFile = true;
                        fileName = args[5];
                    }
                    else{
                        argumentFail = true;
                    }
                }
                else {
                    if (args[4].equals("-mode")) {
                        if (args[5].equals("time")) {
                            aiMode = 0;
                        } else if (args[5].equals("depth")) {
                            aiMode = 1;
                        } else {
                            argumentFail = true;
                        }
                    } else {
                        argumentFail = true;
                    }
                    if (args[6].equals("-param")) {
                        aiModeParam = Integer.parseInt(args[7]);
                        if (aiType < 0) {
                            argumentFail = true;
                        }
                    } else {
                        argumentFail = true;
                    }
                    if (args[8].equals("-prune")) {
                        if (args[9].equals("on")) {
                            prune = true;
                        } else if (args[9].equals("off")) {
                            prune = false;
                        } else {
                            argumentFail = true;
                        }
                    } else {
                        argumentFail = true;
                    }
                }
            }
            if(args.length > 10) {
                if (args[10].equals("-load")) {
                    loadFromFile = true;
                    fileName = args[11];
                }
                else{
                    argumentFail = true;
                }
            }
        }
        else{
            argumentFail = true;
        }

        if(!argumentFail){
            boolean error = false;
            if(!loadFromFile) {
                game = new Game(size, aiType, aiMode, aiModeParam, prune);
            }
            else{
                try{
                    game = Game.loadGameFromFile(size, aiType, aiMode, aiModeParam, prune, fileName);
                }
                catch (Exception e){
                    error = true;
                    System.out.println(e);
                }
            }
            if(game == null){
                error = true;
            }
            if(!error) {
                testDeAIPlayer();
                //launch(args);
            }
            else{
                System.out.println("\nError: Wrong input parameters, please try again.");
            }
        }
        else{
            System.out.println("\nError: Wrong input parameters, please try again.");
        }
    }

    private static void testDeAIPlayer(){
        int i = 0;
//        while(!game.getGameBoard().isOver()) {
//            try {
//                ((AIPlayer)game.getCurrentPlayer()).calculateAndMakeMove();
//                System.out.println(((AIPlayer)game.getNotCurrentPlayer()).makeDotFile("player1" + i++));
//            } catch (MinimaxException e) {
//                e.printStackTrace();
//            }
//        }
        for(int j = 0; j<3; j++){
            try {
                ((AIPlayer)game.getCurrentPlayer()).calculateAndMakeMove();
                System.out.println(((AIPlayer)game.getNotCurrentPlayer()).makeDotFile("player1" + i++));
            } catch (MinimaxException e) {
                e.printStackTrace();
            }
        }
        try {
            ((AIPlayer)game.getCurrentPlayer()).calculateAndMakeMove();
            System.out.println(((AIPlayer)game.getNotCurrentPlayer()).makeDotFile("player1" + i++));
        } catch (MinimaxException e) {
            e.printStackTrace();
        }
        System.out.println(game.getPlayer1().getPoints());
        System.out.println(game.getPlayer2().getPoints());

        System.out.println("Termino");
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Board board = new Board(game);

        Scene scene = new Scene(board, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();



        do{

            scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {

                    if(existMove((int)mouseEvent.getX(),(int)mouseEvent.getY()){

                        n[cont] = new Move((int)mouseEvent.getX(),(int)mouseEvent.getY(),0,0);
                        cont++;

                    }

                }
            });
                    if(cont ==2 && validMove(n[0],n[1])){
                        Move m = new Move(n[0].getRowFrom(),n[0].getColFrom(),n[1].getRowTo(),n[1].getColTo());
                        
                    }

                    else
                        cont=0;

        }

        while(cont <2);



        while(!game.getGameBoard().isOver()){
            Player actualPlayer = game.getCurrentPlayer();
            if(actualPlayer.isAI()){
                ((AIPlayer)actualPlayer).calculateAndMakeMove();
            }
            else{
                Move move = board.getMove();
                actualPlayer.makeMovePlayer(move);
            }
            board.refreshBoard();
        }

    }

}
