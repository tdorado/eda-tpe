package TPE.Timbiriche;

import TPE.Timbiriche.model.*;
import TPE.Timbiriche.model.exceptions.MinimaxException;
import TPE.Timbiriche.view.Board;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import javax.xml.soap.Node;
import java.awt.*;
import java.io.IOException;

import static javafx.scene.paint.Color.BLUE;

public class App extends Application {
    private static Game game;
    private Scene scene;
    private Board board;
    private Coordinates cords[] = new Coordinates[2];
    private int cont;
    private static Text points1 = new Text(600,150,"0");
    private static Text points2 = new Text(600,175,"0");

    public static Text getPoints1() {
        return points1;
    }
    public static Text getPoints2(){
        return points2;
    }

    public static void main(String[] args )
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
                //testDeAIPlayer();
                launch(args);
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

        board = new Board(game);

        scene = new Scene(board, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        Button undo = new Button("UNDO");
        undo.setDefaultButton(true);

        undo.setPrefSize(100, 25);

        undo.setLayoutX(600);
        undo.setLayoutY(25);

        board.getChildren().add(undo);

        Button save = new Button("SAVE");
        save.setDefaultButton(true);

        save.setPrefSize(100, 25);

        save.setLayoutX(600);
        save.setLayoutY(75);

        board.getChildren().add(save);


        Text turn = new Text(600,126,"");


        undo.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                game.undoLastMove();
            }
        });

        save.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                try {
                    game.saveGame("Partida");
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                }
            }
        });




        while(!game.getGameBoard().isOver()){   //aca dentro va lo del text de turno
            Player actualPlayer = game.getCurrentPlayer();
            if(actualPlayer == game.getPlayer1()){
                turn.setText("Player 1 turn");
            }
            else{
                turn.setText("Player 2 turn");
            }
            if(actualPlayer.isAI()){
                ((AIPlayer)actualPlayer).calculateAndMakeMove();
            }
            else{
                Move move = getMove();
               actualPlayer.makeMovePlayer(move);
            }
            board.refreshBoard();
      }

    }

    private Move getMove(){
        Move move = null;
        cont = 0;
        while(move == null){
            scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if(board.existMove((int)mouseEvent.getX(),(int)mouseEvent.getY())){
                        cords[cont] = new Coordinates((int)mouseEvent.getX(),(int)mouseEvent.getY());
                        cont++;
                    }
                }
            });
            if(cont == 2){ // ya tengo 2 puntos
                if(board.validMove(cords[0].x,cords[0].y,cords[1].x,cords[1].y)){ //si es un movimiento valido lo creo
                    move = new Move(cords[0].y / 30,cords[0].x / 30,cords[1].y / 30,cords[1].x / 30);
                }
                else{   //preparo espera de dos puntos nuevos
                    cont = 0;
                }
            }
        }
        System.out.println(move);
        return move;
    }


    private class Coordinates{
        private int x;
        private int y;

        public Coordinates(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

}
