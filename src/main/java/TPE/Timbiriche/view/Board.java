package TPE.Timbiriche.view;

import TPE.Timbiriche.App;
import TPE.Timbiriche.model.*;
import TPE.Timbiriche.model.exceptions.InvalidMoveException;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;


public class Board extends Pane {

    private static final int DISTANCE = 30;
    private static final int RADIUS = 5;
    private static Game g;

    public Board(Game g){

        this.g = g;
        refreshBoard();
    }



    public void refreshBoard(){
        int i;
        int j;

        App.getPoints1().setText("Player 1 point: " + g.getPlayer1().getPoints());
        App.getPoints2().setText("Player 2 point: " + g.getPlayer2().getPoints());

        for(i=0;i<g.getGameBoard().getSize();i++){
            for(j=0;j<g.getGameBoard().getSize();j++){
                Circle c = new Circle(RADIUS,Color.BLACK);
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
    public boolean existMove(int x,int y){
        if(x >= 0 && x <= DISTANCE * g.getGameBoard().getSize()  && y >= 0 && y <= DISTANCE * g.getGameBoard().getSize() ){ //esta dentro del tablero
            if(x % DISTANCE <= 5 && y % DISTANCE <= 5){ //esta dentro del radio de un punto
                return true;
            }
        }
        return false;
    }
    public boolean validMove(int x1,int y1,int x2, int y2){
        if(Math.sqrt(Math.pow(x2 - x1 ,2) + Math.pow(y2 - y1 ,2)) <= DISTANCE + 2 * RADIUS)
        {
            return true;
        }
        return false;
    }

}
