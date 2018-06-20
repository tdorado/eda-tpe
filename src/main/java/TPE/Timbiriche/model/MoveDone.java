package TPE.Timbiriche.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Objects;

/**
 * Class for a move done
 */
public class MoveDone implements Serializable {

    private static final long serialVersionUID = 1L;

    private Move move;
    private Player player;

    public MoveDone(Move move, Player player) {
        this.move = move;
        this.player = player;
    }

    /**
     * Returns player that made this move
     *
     * @return Player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Returns move of this done move
     *
     * @return
     */
    public Move getMove() {
        return move;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MoveDone moveDone = (MoveDone) o;
        return Objects.equals(move, moveDone.move);
    }

    @Override
    public int hashCode() {
        return move.hashCode();
    }

    /**
     * Serialization method for saveGame and loadGame
     *
     * @param out
     * @throws IOException
     */
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(move);
        out.writeObject(player);
    }

    /**
     * Serialization method for saveGame and loadGame
     *
     * @param ois
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        move = (Move) ois.readObject();
        player = (Player) ois.readObject();
    }
}
