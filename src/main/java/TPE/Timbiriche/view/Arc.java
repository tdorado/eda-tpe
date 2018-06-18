package TPE.Timbiriche.view;

public class Arc{
	private Point from;
	private Point to;
	public Arc(Point from,Point to) {
		this.from = from;
		this.to = to;
	}
	public Point getFrom() {
		return from;
	}
	public Point getTo() {
		return to;
	}
}