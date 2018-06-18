package TPE.Timbiriche.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class MoveDone implements Serializable {

    private static final long serialVersionUID = 1L;

    private Move move;
    private Player player;

    public MoveDone(Move move, Player player) {
        this.move = move;
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public Move getMove() {
        return move;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(move);
        out.writeObject(player);
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        move = (Move)ois.readObject();
        player = (Player)ois.readObject();
    }
}
