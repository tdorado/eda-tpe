package TPE.Timbiriche.view;

import TPE.Timbiriche.model.Game;
import TPE.Timbiriche.model.GameBoard;
import TPE.Timbiriche.model.Move;
import TPE.Timbiriche.model.MoveDone;
import javafx.application.Application;

import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import static javafx.application.Application.launch;

public class App extends Application {
        public static void main(String[] args) {

            launch(args);
        }

        @Override
        public void start(Stage primaryStage) throws Exception {

            GameBoard gb = new GameBoard(10);

            Board b = new Board(gb);

            Scene scene = new Scene(b, 800, 600);
            primaryStage.setScene(scene);
            primaryStage.show();


        }
    }


