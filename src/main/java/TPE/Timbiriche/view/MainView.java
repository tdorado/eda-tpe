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

public class MainView extends Application {
        public static void main(String args) {

            launch(args);
        }

        public Game g;

        public MainView(Game g)
        {
            this.g = g;
        }

        @Override
        public void start(Stage primaryStage) throws Exception {

            Board b = new Board(g);

            Scene scene = new Scene(b, 800, 600);
            primaryStage.setScene(scene);
            primaryStage.show();


        }
    }


