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
            Board b = new Board();
            Scene scene = new Scene(b, 800, 600);
            primaryStage.setScene(scene);
            primaryStage.show();
        }
    }


