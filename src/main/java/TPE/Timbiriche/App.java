package TPE.Timbiriche;

import TPE.Timbiriche.model.Game;
import java.io.File;

public class App
{
    public static void main( String[] args )
    {
        Game game = null;

        int size = 0, aiType = 0, aiMode = 0, aiModeParam = 0;
        boolean prune = false, argumentFail = false, loadFromFile = false;
        File file = null;

        if(args.length == 12 || args.length == 10) {
            if (args[0].equals("-size")) {
                size = Integer.parseInt(args[1]);
                if (size < 2 && size > 30) {
                    argumentFail = true;
                }
            }
            if (args[2].equals("-ai")) {
                aiType = Integer.parseInt(args[3]);
                if (size < 0 && size > 3) {
                    argumentFail = true;
                }
            }
            if (args[4].equals("-mode")) {
                if (args[5].equals("time")) {
                    aiMode = 0;
                } else if (args[5].equals("depth")) {
                    aiMode = 1;
                } else {
                    argumentFail = true;
                }
            }
            if (args[6].equals("-param")) {
                aiModeParam = Integer.parseInt(args[7]);
            }
            if (args[8].equals("-prune")) {
                if (args[9].equals("on")) {
                    prune = true;
                } else if (args[9].equals("off")) {
                    prune = false;
                } else {
                    argumentFail = true;
                }
            }
            if(args.length == 11) {
                if (args[10].equals("-load")) {
                    loadFromFile = true;
                    file = new File(args[11]);
                }
            }
        }
        else{
            argumentFail = true;
        }

        if(!argumentFail){
            if(!loadFromFile) {
                game = new Game(size, aiType, aiMode, aiModeParam, prune);
            }
            else{
                game = new Game(size, aiType, aiMode, aiModeParam, prune, file);
            }
            //LOAD INTERFAZ VISUAL
        }
        else{
            System.out.println("\nError: Wrong input parameters, please try again.");
        }
    }
}
