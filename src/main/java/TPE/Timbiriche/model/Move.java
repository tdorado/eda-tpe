package TPE.Timbiriche.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Objects;

public class Move implements Serializable {

    private static final long serialVersionUID = 1L;

    private int rowFrom;
    private int colFrom;
    private int rowTo;
    private int colTo;

    Move(int rowFrom, int colFrom, int rowTo, int colTo) {
        /*The HashCode and equals implementation assumes that the From position is lower than the To position,
        so we have to check this here just in case. */
        if(rowFrom > rowTo){
            this.rowFrom = rowTo;
            this.colFrom = colFrom;
            this.rowTo = rowFrom;
            this.colTo = colTo;
        }
        else if(colFrom > colTo){
            this.rowFrom = rowFrom;
            this.colFrom = colTo;
            this.rowTo = rowTo;
            this.colTo = colFrom;
        }
        else{
            this.rowFrom = rowFrom;
            this.colFrom = colFrom;
            this.rowTo = rowTo;
            this.colTo = colTo;
        }
    }

    public int getRowFrom() {
        return rowFrom;
    }

    public int getColFrom() {
        return colFrom;
    }

    public int getRowTo() {
        return rowTo;
    }

    public int getColTo() {
        return colTo;
    }

    public boolean isVertical(){
        return colFrom == colTo;
    }

    public boolean isHorizontal(){
        return rowFrom == rowTo;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;

        if (object == null || getClass() != object.getClass())
            return false;

        Move move = (Move) object;
        return rowFrom == move.rowFrom && colFrom == move.colFrom && rowTo == move.rowTo && colTo == move.colTo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rowFrom, colFrom, rowTo, colTo);
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeInt(rowFrom);
        out.writeInt(colFrom);
        out.writeInt(rowTo);
        out.writeInt(colTo);
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        rowFrom = ois.readInt();
        colFrom = ois.readInt();
        rowTo = ois.readInt();
        colTo = ois.readInt();
    }
}