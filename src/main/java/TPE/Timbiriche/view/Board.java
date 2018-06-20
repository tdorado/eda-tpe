package TPE.Timbiriche.view;

import TPE.Timbiriche.App;
import TPE.Timbiriche.model.*;
import TPE.Timbiriche.model.exceptions.InvalidMoveException;
import com.sun.org.apache.xpath.internal.SourceTree;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.util.LinkedList;
import java.util.List;


public class Board extends Pane {

    private static final int DISTANCE = 30;
    private static final int RADIUS = 5;
    private static final int LINE_EXTEND = 4;
    private static final int LINE_DISTANCE = 20;
    private static Game g;

    public Board(Game g) {
        this.g = g;
        initializeBoard();
    }

    public void setTexts() {
        App.getPoints1().setText("Player 1 point: " + g.getPlayer1().getPoints());
        App.getPoints2().setText("Player 2 point: " + g.getPlayer2().getPoints());
    }

    private void initializeBoard() {
        setTexts();

        for (int i = 0; i < g.getGameBoard().getSize(); i++) {
            for (int j = 0; j < g.getGameBoard().getSize(); j++) {
                Circle c = new Circle(RADIUS, Color.BLACK);
                c.relocate(i * DISTANCE, j * DISTANCE);
                getChildren().add(c);
            }
        }
    }

    public void refreshBoard() {
        setTexts();

        for (MoveDone eachMoveDone : g.getLastMovesDone()) {
            Rectangle arc = new Rectangle();
            arc.setArcWidth(1);
            arc.setArcHeight(1);

            if (eachMoveDone.getMove().isHorizontal()) {
                arc.setWidth(LINE_DISTANCE);
                arc.setHeight(LINE_EXTEND);
                arc.setX((eachMoveDone.getMove().getColFrom() * DISTANCE) + RADIUS * 2);
                arc.setY((eachMoveDone.getMove().getRowFrom() * DISTANCE) + (RADIUS * 2 - LINE_EXTEND) / 2);

            } else {
                arc.setWidth(LINE_EXTEND);
                arc.setHeight(LINE_DISTANCE);
                arc.setX((eachMoveDone.getMove().getColFrom() * DISTANCE) + (RADIUS * 2 - LINE_EXTEND) / 2);
                arc.setY((eachMoveDone.getMove().getRowFrom() * DISTANCE) + RADIUS * 2);

            }

            if (eachMoveDone.getPlayer() == g.getPlayer1()) {
                arc.setFill(Color.RED);
            } else {
                arc.setFill(Color.BLUE);
            }

            getChildren().add(arc);
        }
    }

    public boolean existMove(int x, int y) {
        if (x >= 0 && x <= DISTANCE * g.getGameBoard().getSize() && y >= 0 && y <= DISTANCE * g.getGameBoard().getSize()) {
            if (x % DISTANCE <= RADIUS && y % DISTANCE <= RADIUS) {
                return true;
            }
        }
        return false;
    }

    public boolean validMove(int x1, int y1, int x2, int y2) {
        if (Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2)) <= DISTANCE + 2 * RADIUS) {
            return true;
        }
        return false;
    }

}
