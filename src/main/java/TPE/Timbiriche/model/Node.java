package TPE.Timbiriche.model;

import java.util.Objects;

public class Node {
    private int i;
    private int j;

    public Node(int i, int j) {
        this.i = i;
        this.j = j;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return i == node.i &&
                j == node.j;
    }

    @Override
    public int hashCode() {

        return Objects.hash(i, j);
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }
}
