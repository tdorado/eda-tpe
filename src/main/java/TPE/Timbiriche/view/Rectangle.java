public class Rectangle{
	private Arc topVericalArc;
	private Arc bottomVerticalArc;
	private Arc leftHorizontalArc;
	private Arc rightHorizontalArc;
	
	public Rectangle(Arc topVerticalArc,Arc bottomVerticalArc,Arc leftHorizontalArc,Arc rightHorizontalArc) {
		this.topVerticalArc = topVerticalArc;
		this.bottomVerticalArc = bottomVerticalArc;
		this.leftHorizontalArc = leftHorizontalArc;
		this.rightHorizontalArc = rightHorizontalArc;
	}
	public Arc getLeftHorizontalArc() {
		return leftHorizontalArc;
	}
	public Arc getRightHorizontalArc() {
		return rightHorizontalArc;
	}
	public Arc getTopVerticalArc() {
		return topVericalArc;
	}
	public Arc getBottomVerticalArc() {
		return bottomVerticalArc;
	}
}