package TPE.Timbiriche.model;

public class Tester {
        public static void main(String[]args){
            Game g = new Game(10,0,0,0,false);

            GameBoard gb = new GameBoard(10);

            gb.getMovesDone().add(new MoveDone(new Move(5,0,10,0),g.getPlayer1()));
            gb.getMovesDone().add(new MoveDone(new Move(10,0,15,0),g.getPlayer2()));
        }

}
