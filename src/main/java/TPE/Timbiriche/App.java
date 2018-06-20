package TPE.Timbiriche;

import TPE.Timbiriche.model.*;
import TPE.Timbiriche.model.exceptions.InvalidMoveException;
import TPE.Timbiriche.model.exceptions.MinimaxException;
import TPE.Timbiriche.view.Board;
import javafx.application.Application;
import javafx.beans.binding.BooleanBinding;
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
    private Button nextTurn;
    private Coordinates cords[] = new Coordinates[2];
    private int cont;
    private static Text points1 = new Text(600, 150, "0");
    private static Text points2 = new Text(600, 175, "0");

    private Move lastMoveClicked = null;

    public static Text getPoints1() {
        return points1;
    }

    public static Text getPoints2() {
        return points2;
    }

    public static void main(String[] args) {
        game = null;

        int size = 0, aiType = 0, aiMode = 0, aiModeParam = 0;
        boolean prune = false, argumentFail = false, loadFromFile = false;
        String fileName = null;

        if (args.length == 4 || args.length == 6 || args.length == 10 || args.length == 12) {
            if (args[0].equals("-size")) {
                size = Integer.parseInt(args[1]);
                if (size < 1 && size > 30) {
                    argumentFail = true;
                }
            } else {
                argumentFail = true;
            }
            if (args[2].equals("-ai")) {
                aiType = Integer.parseInt(args[3]);
                if (aiType < 0 && aiType > 3) {
                    argumentFail = true;
                }
            } else {
                argumentFail = true;
            }
            if (args.length > 4) {
                if (args.length == 6) {
                    if (args[4].equals("-load")) {
                        loadFromFile = true;
                        fileName = args[5];
                    } else {
                        argumentFail = true;
                    }
                } else {
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
            if (args.length > 10) {
                if (args[10].equals("-load")) {
                    loadFromFile = true;
                    fileName = args[11];
                } else {
                    argumentFail = true;
                }
            }
        } else {
            argumentFail = true;
        }

        if (!argumentFail) {
            boolean error = false;
            if (!loadFromFile) {
                game = new Game(size, aiType, aiMode, aiModeParam, prune);
            } else {
                try {
                    game = Game.loadGameFromFile(size, aiType, aiMode, aiModeParam, prune, fileName);
                } catch (Exception e) {
                    error = true;
                    System.out.println(e);
                }
            }
            if (game == null) {
                error = true;
            }
            if (!error) {
                launch(args);
            } else {
                System.out.println("\nError: Wrong input parameters, please try again.");
            }
        } else {
            System.out.println("\nError: Wrong input parameters, please try again.");
        }
    }

    @Override
    public void start(Stage primaryStage) {
        board = new Board(game);

        scene = new Scene(board, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        nextTurn = new Button("NEXT TURN");
        nextTurn.setDefaultButton(true);

        nextTurn.setPrefSize(100, 25);

        nextTurn.setLayoutX(600);
        nextTurn.setLayoutY(125);

        board.getChildren().add(nextTurn);
        
        nextTurn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                playNextTurn();
            }
        });


        scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //ACA VOY A MODIFICAR lastMoveClicked cada vez que hace un clic en un arista
            }
        });

    }

    private void playNextTurn() {
        if (!game.getGameBoard().isOver()) {   //aca dentro va lo del text de turno
            Player actualPlayer = game.getCurrentPlayer();
            if (actualPlayer.isAI()) {
                try {
                    ((AIPlayer) actualPlayer).calculateAndMakeMove();

                } catch (MinimaxException ex) {
                    System.out.println(ex);
                }
            } else {
                try {
                    actualPlayer.makeMovePlayer(lastMoveClicked);
                } catch (InvalidMoveException ex) {
                    System.out.println(ex);
                }
                lastMoveClicked = null;
            }

            board.refreshBoard();
        }
    }

    private class Coordinates {
        private int x;
        private int y;

        public Coordinates(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

}
