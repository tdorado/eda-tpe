package TPE.Timbiriche.model;

import java.util.Objects;

public class Arc {
    private Node nodeFrom;
    private Node nodeTo;

    public Arc(Node nodeFrom, Node nodeTo) {
        this.nodeFrom = nodeFrom;
        this.nodeTo = nodeTo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Arc arc = (Arc) o;
        return nodeFrom.equals(arc.nodeFrom) &&
                nodeTo.equals(arc.nodeTo);
    }

    @Override
    public int hashCode() {

        return Objects.hash(nodeFrom, nodeTo);
    }

    public Node getNodeFrom() {
        return nodeFrom;
    }

    public Node getNodeTo() {
        return nodeTo;
    }
}
