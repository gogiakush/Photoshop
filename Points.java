import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.ArrayList;

public class Points implements Comparable<Points>, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ArrayList<Integer> xs;
	private ArrayList<Integer> ys;
	private Color color;
	private int transparency;
	private float brushThickness;
	
	
	public Points(int transparency, float brushThickness, Color color) {
		xs = new ArrayList<Integer>();
		ys = new ArrayList<Integer>();
		this.color = color;
		this.transparency = transparency;
		this.brushThickness = brushThickness;
	}
	
	public void addPoint(Integer x, Integer y) {
		xs.add(x);
		ys.add(y);
	}
	
	public void setPoint(int index, Integer x, Integer y) {
		xs.add(index, x);
		ys.add(index, y);
	}
	
	public void draw(Graphics g) {
		 
		for(int i=0; i<xs.size()-1; i++) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setStroke(new BasicStroke(brushThickness));
			g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), transparency));
			g.drawLine(xs.get(i), ys.get(i), xs.get(i+1), ys.get(i+1));
		}
		
		/*
		for(int i=0; i<xs.size(); i++) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setStroke(new BasicStroke(brushThickness));
			g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), transparency));
			g.drawOval(xs.get(i), ys.get(i), (int)brushThickness, (int)brushThickness);
		}
		*/
	}
	
	public ArrayList<Integer> getXs(){
		return xs;
	}
	
	public ArrayList<Integer> getYs(){
		return ys;
	}
	
	public Color getColor() {
		return color;
	}
	
	public int getTransparency() {
		return transparency;
	}
	
	public float getBrushThickness() {
		return brushThickness;
	}

	@Override
	public int compareTo(Points o) {
		return 0;
	}
	
	public String toString() {
		return "x: "+xs+", y: "+ys;
	}
	
}
