package TPE.Timbiriche.model;

public class Move {

    private int iFrom;
    private int jFrom;
    private int iTo;
    private int jTo;

    public Move(int iFrom, int jFrom, int iTo, int jTo) {
        this.iFrom = iFrom;
        this.jFrom = jFrom;
        this.iTo = iTo;
        this.jTo = jTo;
    }

    public int getiFrom() {
        return iFrom;
    }

    public int getjFrom() {
        return jFrom;
    }

    public int getiTo() {
        return iTo;
    }

    public int getjTo() {
        return jTo;
    }
}