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
import javafx.scene.text.Text;

import java.util.LinkedList;
import java.util.List;


public class Board extends Pane {

    private static final int DISTANCE = 30;
    private static final int RADIUS = 5;
    private static final int LINE_EXTEND = 4;
    private static final int LINE_DISTANCE = 20;
    private static Game g;

    private static Text points1 = new Text(600, 200, "0");
    private static Text points2 = new Text(600, 250, "0");
    private static Text firstClick = new Text(600, 300, "First click detected.");
    private static Text secondClick = new Text(600, 350, "Second click detected");
    private static Text invalidMove = new Text(600, 400, "Invalid Move");

    public Board(Game g) {
        this.g = g;
        initializeBoard();
    }

    public void setTexts() {
        points1.setText("Player 1 point: " + g.getPlayer1().getPoints());
        points2.setText("Player 2 point: " + g.getPlayer2().getPoints());
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
        getChildren().add(points1);
        getChildren().add(points2);
        firstClick.setVisible(false);
        secondClick.setVisible(false);
        invalidMove.setVisible(false);
        getChildren().add(firstClick);
        getChildren().add(secondClick);
        getChildren().add(invalidMove);
    }

    public Text getFirstClick(){
        return firstClick;
    }

    public Text getSecondClick(){
        return secondClick;
    }

    public Text getInvalidMoveText(){
        return invalidMove;
    }

    public void refreshBoard() {
        setTexts();

        for (MoveDone eachMoveDone : g.getLastMovesDone()) {
            Rectangle arc = createLine(eachMoveDone);

            if (eachMoveDone.getPlayer() == g.getPlayer1()) {
                arc.setFill(Color.RED);
            } else {
                arc.setFill(Color.BLUE);
            }

            getChildren().add(arc);
        }
    }

    public void undoLastMove(MoveDone lastMoveDone) {
        Rectangle arc = createLine(lastMoveDone);

        arc.setFill(Color.WHITE);
        getChildren().add(arc);
        setTexts();
    }

    private Rectangle createLine(MoveDone moveDone) {
        Rectangle arc = new Rectangle();
        arc.setArcWidth(1);
        arc.setArcHeight(1);

        if (moveDone.getMove().isHorizontal()) {
            arc.setWidth(LINE_DISTANCE);
            arc.setHeight(LINE_EXTEND);
            arc.setX((moveDone.getMove().getColFrom() * DISTANCE) + RADIUS * 2);
            arc.setY((moveDone.getMove().getRowFrom() * DISTANCE) + (RADIUS * 2 - LINE_EXTEND) / 2);

        } else {
            arc.setWidth(LINE_EXTEND);
            arc.setHeight(LINE_DISTANCE);
            arc.setX((moveDone.getMove().getColFrom() * DISTANCE) + (RADIUS * 2 - LINE_EXTEND) / 2);
            arc.setY((moveDone.getMove().getRowFrom() * DISTANCE) + RADIUS * 2);

        }

        return arc;
    }

    public boolean isCircle(int x, int y) {
        if (x >= 0 && x <= DISTANCE * g.getGameBoard().getSize() && y >= 0 && y <= DISTANCE * g.getGameBoard().getSize()) {
            if (x % DISTANCE <= RADIUS && y % DISTANCE <= RADIUS) {
                return true;
            }
        }
        return false;
    }

}
