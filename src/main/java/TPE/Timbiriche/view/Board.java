package TPE.Timbiriche.view;

import TPE.Timbiriche.model.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import static TPE.Timbiriche.App.game;


public class Board extends Pane {

    private static int DISTANCE = 30;


    // Board recibe el GameBord actual.
    public Board(Game g){

        int i;
        int j;

        for(i=0;i<g.getGameBoard().getSize();i++){
            for(j=0;j<g.getGameBoard().getSize();j++){
                Circle c = new Circle(5,Color.BLACK);
                c.relocate(i*DISTANCE,j*DISTANCE);
                getChildren().add(c);
            }
        }


        for(MoveDone d : g.getGameBoard().getMovesDone()){
            Rectangle arc = new Rectangle();
            arc.setX(d.getMove().getRowFrom());
            arc.setY(d.getMove().getColFrom());
            arc.setWidth(20);
            arc.setHeight(3);
            arc.setArcWidth(1);
            arc.setArcHeight(1);

            if(d.getPlayer() == g.getPlayer1()){
                arc.setFill(Color.RED);
                System.out.println("hola");
            }
            else {
                System.out.println("chau");
                arc.setFill(Color.BLUE);
            }
            getChildren().add(arc);
        }




      //  Circle c1 = new Circle(10, Color.BLACK);
      //  c1.relocate(0, 0);
       // Circle c2 = new Circle(10, Color.BLACK);
      //  c2.relocate(30, 30);

       // c.setFill(Color.BLACK);
       // getChildren().addAll(c1,c2);
      //  getChildren().add(c);
    }


}
