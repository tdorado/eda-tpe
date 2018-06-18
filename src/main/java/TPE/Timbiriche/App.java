package TPE.Timbiriche;

import TPE.Timbiriche.model.Game;

public class App
{
    public static Game game;

    public static void main( String[] args )
    {
        game = null;

        int size = 0, aiType = 0, aiMode = 0, aiModeParam = 0;
        boolean prune = false, argumentFail = false, loadFromFile = false;
        String fileName = null;

        if(args.length == 4 || args.length == 6 || args.length == 10 || args.length == 12) {
            if (args[0].equals("-size")) {
                size = Integer.parseInt(args[1]);
                if (size < 2 && size > 30) {
                    argumentFail = true;
                }
            }
            else{
                argumentFail = true;
            }
            if (args[2].equals("-ai")) {
                aiType = Integer.parseInt(args[3]);
                if (aiType < 0 && aiType > 3) {
                    argumentFail = true;
                }
            }
            else{
                argumentFail = true;
            }
            if(args.length > 4) {
                if(args.length == 6){
                    if (args[4].equals("-load")) {
                        loadFromFile = true;
                        fileName = args[5];
                    }
                    else{
                        argumentFail = true;
                    }
                }
                else {
                    if (args[4].equals("-mode")) {
                        if (args[5].equals("time")) {
                            aiMode = 0;
                        } else if (args[5].equals("depth")) {
                            aiMode = 1;
                        } else {
                            argumentFail = true;
                        }
                    } else {
                        argumentFail = true;
                    }
                    if (args[6].equals("-param")) {
                        aiModeParam = Integer.parseInt(args[7]);
                        if (aiType < 0) {
                            argumentFail = true;
                        }
                    } else {
                        argumentFail = true;
                    }
                    if (args[8].equals("-prune")) {
                        if (args[9].equals("on")) {
                            prune = true;
                        } else if (args[9].equals("off")) {
                            prune = false;
                        } else {
                            argumentFail = true;
                        }
                    } else {
                        argumentFail = true;
                    }
                }
            }
            if(args.length > 10) {
                if (args[10].equals("-load")) {
                    loadFromFile = true;
                    fileName = args[11];
                }
                else{
                    argumentFail = true;
                }
            }
        }
        else{
            argumentFail = true;
        }

        if(!argumentFail){
            boolean error = false;
            if(!loadFromFile) {
                game = new Game(size, aiType, aiMode, aiModeParam, prune);
            }
            else{
                try{
                    game = Game.loadGameFromFile(size, aiType, aiMode, aiModeParam, prune, fileName);
                }
                catch (Exception e){
                    error = true;
                    System.out.println(e);
                }
            }
            if(!error) {
                startView();
            }
        }
        else{
            System.out.println("\nError: Wrong input parameters, please try again.");
        }
    }

    private static void startView() {
        //ARRANCA LA VIEW DESDE ACA TIENEN QUE EMPEZAR A HACER LO VISUAL; LO UNICO QUE NECESITAN ES game que ya aca lo tienen
    }
}
