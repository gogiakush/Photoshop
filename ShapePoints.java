import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class ShapePoints extends Points {

	private int limit;
	
	public ShapePoints(int transparency, float brushThickness, Color color, int limit) {
		super(transparency, brushThickness, color);
		this.limit = limit;
	}
	
	public void addPoint(Integer x, Integer y) {
		if(!isFinished()) {
			super.addPoint(x, y);
		}
	}
	
	public void draw(Graphics g) {
		if(isFinished()) {
			super.draw(g);
		}
	}
	
	public boolean isFinished() {
		return getXs().size()>=limit;
	}
}
class RectanglePoints extends ShapePoints {

	public RectanglePoints(int transparency, float brushThickness, Color color) {
		super(transparency, brushThickness, color, 2);
	}
	
	public void draw(Graphics g) {
		if(isFinished()) {
			g.drawRect(getXs().get(0), getYs().get(0), getXs().get(1)-getXs().get(0), getYs().get(1)-getYs().get(0));
		}
	}
	
}
class CirclePoints extends ShapePoints {

	public CirclePoints(int transparency, float brushThickness, Color color) {
		super(transparency, brushThickness, color, 2);
	}
	
	public void draw(Graphics g) {
		if(isFinished()) {
			g.drawOval(getXs().get(0), getYs().get(0), getXs().get(1)-getXs().get(0), getYs().get(1)-getYs().get(0));
		}
	}
	
}
