package TPE.Timbiriche.view;

import TPE.Timbiriche.model.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;


public class Board extends Pane {

    private static final int DISTANCE = 30;
    private static Game g;

    public Board(Game g){

        this.g = g;
        refreshBoard();
    }

    public void refreshBoard(){
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
            arc.setX(d.getMove().getRowFrom()+ 2.5);
            arc.setY(d.getMove().getColFrom()+2.5);

            if(d.getMove().isHorizontal()) {

                arc.setWidth(20);
                arc.setHeight(3);
                arc.setArcWidth(1);
                arc.setArcHeight(1);
            }

            if(d.getMove().isVertical()) {

                arc.setWidth(3);
                arc.setHeight(20);
                arc.setArcWidth(1);
                arc.setArcHeight(1);
            }

            if(d.getPlayer() == g.getPlayer1()){
                arc.setFill(Color.RED);
            }
            else {
                arc.setFill(Color.BLUE);
            }
            getChildren().add(arc);
        }
    }

    public boolean validateCoordinates(int x, int y){
        for(int i = 0;i < g.getGameBoard().getSize();i++){
            for(int j = 0;j < g.getGameBoard().getSize();j++) {
                if(x == i * DISTANCE && y == j * DISTANCE){
                    return true;
                }
            }
        }
        return false;
    }
}
