package TPE.Timbiriche;


import TPE.Timbiriche.model.Game;
import TPE.Timbiriche.model.exceptions.InvalidMoveException;

public class App
{
    public static void main( String[] args )
    {
        System.out.print("Args= " );
        for(String arg : args){
            System.out.print(arg + " ");
        }
        System.out.println();

        //Prueba que hice con el debugger de intellij y salio bien
        Game game = new Game(4, 0, 1, 10, true);
        try {
            game.getCurrentPlayer().makeMove(0,0,0,1);
            game.getCurrentPlayer().makeMove(0,1,1,1);
            game.getCurrentPlayer().makeMove(1,0,1,1);
            game.getCurrentPlayer().makeMove(0,0,1,0);
            game.getCurrentPlayer().makeMove(1,1,2,1);
            //game.undoLastMove();
        } catch (InvalidMoveException e) {
            System.out.println(e);
        }
    }
}
