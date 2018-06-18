import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


public class Board extends Pane {
    public Board(){
        Circle c = new Circle(10);
        c.setFill(Color.BLACK);

        getChildren().add(c);
    }


}
