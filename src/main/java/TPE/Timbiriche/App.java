package TPE.Timbiriche;

import TPE.Timbiriche.model.*;
import TPE.Timbiriche.model.exceptions.InvalidMoveException;
import TPE.Timbiriche.model.exceptions.MinimaxException;
import TPE.Timbiriche.view.Board;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Clase principal que parsea la entrada de argumentos y genera la partida
 */
public class App extends Application {
    private static Game game;
    private Scene scene;
    private Board board;
    private Button nextTurn;
    private Coordinates cord1 = new Coordinates(0, 0);
    private Coordinates cord2 = new Coordinates(0, 0);
    private int cont = 0;

    private Move lastMoveClicked = null;

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

        undo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                MoveDone lastMoveDone = game.undoLastMove();
                if (lastMoveDone != null) {
                    board.undoLastMove(lastMoveDone);
                }
            }
        });

        save.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {
                    game.saveGame("partida");
                } catch (IOException | ClassNotFoundException e1) {
                    e1.printStackTrace();
                }
            }
        });

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
                int x = (int) event.getX();
                int y = (int) event.getY();
                if (board.isCircle(x, y)) {
                    if (cont == 0) {
                        cord1.x = y / 30;
                        cord1.y = x / 30;
                        cont = 1;
                        board.getFirstClick().setVisible(true);
                        board.getInvalidMoveText().setVisible(false);
                    } else if (cont == 1) {
                        cord2.x = y / 30;
                        cord2.y = x / 30;
                        cont = 2;
                        board.getSecondClick().setVisible(true);
                        lastMoveClicked = new Move(cord1.x, cord1.y, cord2.x, cord2.y);
                        playNextTurn();
                    }
                }
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
                    board.getInvalidMoveText().setVisible(true);
                    System.out.println(ex);
                }
                board.getFirstClick().setVisible(false);
                board.getSecondClick().setVisible(false);
                lastMoveClicked = null;
                cont = 0;
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
