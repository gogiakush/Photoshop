import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class BoardPanel extends JPanel implements ActionListener, MouseListener, MouseMotionListener {
	
	private JButton backButton, saveButton, undoButton, redoButton;
	private JButton paintBrushButton, lineBrushButton, rectBrushButton, circleBrushButton;
	private JTextField textField;
	private JPanel canvas;
	
	private ArrayList<Points> pointsList;
	private ArrayList<Points> removedPointsList;
	private Points currentPoint;
	
	private Color burshColor;
	private BrushState currentState;
	private int transparency;
	private float brushThickness;
	
	private enum BrushState {
		Paint, Lines, Rects, Circles
	}
	
	public BoardPanel() {
		setLayout(new BorderLayout());
		
		reset();
		
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		backButton = new JButton("Back");
		backButton.addActionListener(this);
		backButton.setActionCommand(backButton.getText());
		saveButton = new JButton("Save");
		saveButton.addActionListener(this);
		saveButton.setActionCommand(saveButton.getText());
		undoButton = new JButton("Undo");
		undoButton.addActionListener(this);
		undoButton.setActionCommand(undoButton.getText());
		redoButton = new JButton("Redo");
		redoButton.addActionListener(this);
		redoButton.setActionCommand(redoButton.getText());
		textField = new JTextField("name");
		textField.setColumns(40);
		topPanel.add(backButton);
		topPanel.add(saveButton);
		topPanel.add(textField);
		topPanel.add(undoButton);
		topPanel.add(redoButton);
		
		JPanel brushPanel = new JPanel();
		brushPanel.setLayout(new GridLayout(4, 1));
		paintBrushButton = new JButton("paintBrush");
		paintBrushButton.addActionListener(this);
		paintBrushButton.setActionCommand(paintBrushButton.getText()); 
		
		lineBrushButton = new JButton("lineBrush");
		lineBrushButton.addActionListener(this);
		lineBrushButton.setActionCommand(lineBrushButton.getText()); 
		
		rectBrushButton = new JButton("rectBrushButton");
		rectBrushButton.addActionListener(this);
		rectBrushButton.setActionCommand(rectBrushButton.getText()); 
		
		circleBrushButton = new JButton("circleBrushButton");
		circleBrushButton.addActionListener(this);
		circleBrushButton.setActionCommand(circleBrushButton.getText()); 
		
		brushPanel.add(paintBrushButton);
		brushPanel.add(lineBrushButton);
		brushPanel.add(rectBrushButton);
		brushPanel.add(circleBrushButton);
		
		JPanel optionsPanel = new JPanel();
		optionsPanel.setLayout(new GridLayout(2, 1));
		
		JPanel colorsPanel = new JPanel();
		colorsPanel.setLayout(new GridLayout(Constants.colors.length/2, 2));
		for(Color currentColor: Constants.colors) {
			JButton button = new JButton();
			button.setBackground(currentColor);
			button.setForeground(currentColor);
			button.setOpaque(true);
			button.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					burshColor = button.getBackground();
				}
				
			});
			colorsPanel.add(button);
		}
		JPanel sizePanel = new JPanel();
		sizePanel.setLayout(new GridLayout(4, 1));
		JSlider transparencySlider = new JSlider(1, 255, 255);
		transparencySlider.addChangeListener(new ChangeListener() {
	        @Override
	        public void stateChanged(ChangeEvent ce) {
	            transparency = ((JSlider) ce.getSource()).getValue();
	        }
	    });
		JSlider brushThicknessSlider = new JSlider(0, 10);
		brushThicknessSlider.addChangeListener(new ChangeListener() {
	        @Override
	        public void stateChanged(ChangeEvent ce) {
	            brushThickness = ((JSlider) ce.getSource()).getValue();
	        }
	    });
		sizePanel.add(new JLabel("transparency", JLabel.CENTER));
		sizePanel.add(transparencySlider); 
		sizePanel.add(new JLabel("brushThickness", JLabel.CENTER));
		sizePanel.add(brushThicknessSlider);
		
		optionsPanel.add(colorsPanel);
		optionsPanel.add(sizePanel);
		
		canvas = new JPanel() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.setColor(burshColor);
				for(Points current: pointsList) {
					current.draw(g);
				}
				
			}
		};
		canvas.setBackground(Color.white);
		canvas.setFocusable(true);
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
		
		add(topPanel, BorderLayout.NORTH);
		add(brushPanel, BorderLayout.EAST);
		add(optionsPanel, BorderLayout.WEST);
		add(canvas, BorderLayout.CENTER);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if(currentState==BrushState.Paint) {
			if(currentPoint!=null) {
				currentPoint.addPoint(e.getX(), e.getY());
				repaint();
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(currentState==BrushState.Paint) {
			currentPoint = new Points(transparency, brushThickness, burshColor);
			currentPoint.addPoint(e.getX(), e.getY());
			pointsList.add(currentPoint);
		} else if(currentState==BrushState.Lines) {
			if(!(currentPoint instanceof ShapePoints) || (currentPoint instanceof ShapePoints && ((ShapePoints)currentPoint).isFinished())) {
					currentPoint = new ShapePoints(transparency, brushThickness, burshColor, 2);
					currentPoint.addPoint(e.getX(), e.getY());
					pointsList.add(currentPoint);
			}
			else if ((currentPoint instanceof ShapePoints) && !((ShapePoints)currentPoint).isFinished()) {
				currentPoint.addPoint(e.getX(), e.getY());
			}
			repaint();
		} else if(currentState==BrushState.Rects) {
			if(!(currentPoint instanceof RectanglePoints) || (currentPoint instanceof RectanglePoints && ((RectanglePoints)currentPoint).isFinished())) {
				currentPoint = new RectanglePoints(transparency, brushThickness, burshColor);
				currentPoint.addPoint(e.getX(), e.getY());
				pointsList.add(currentPoint);
			}
			else if ((currentPoint instanceof RectanglePoints) && !((RectanglePoints)currentPoint).isFinished()) {
				currentPoint.addPoint(e.getX(), e.getY());
			}
			repaint();
		} else if(currentState==BrushState.Circles) {
			if(!(currentPoint instanceof CirclePoints) || (currentPoint instanceof CirclePoints && ((CirclePoints)currentPoint).isFinished())) {
				currentPoint = new CirclePoints(transparency, brushThickness, burshColor);
				currentPoint.addPoint(e.getX(), e.getY());
				pointsList.add(currentPoint);
			}
			else if ((currentPoint instanceof CirclePoints) && !((CirclePoints)currentPoint).isFinished()) {
				currentPoint.addPoint(e.getX(), e.getY());
			}
			repaint();
		} 
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		requestFocusInWindow();
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		
		if(command.equals(backButton.getActionCommand())) {
			reset();
			Photoshop.getCardLayout().show(Photoshop.getMasterPanel(), "projectsPanel");
		} else if(command.equals(saveButton.getActionCommand())) {
			saveImage();
			serialize();
			File file = new File(Constants.projectDirectorySerialized+textField.getText()+".txt");
			if(!Photoshop.getProjectsPanel().getProjects().contains(file)) {
				Photoshop.getProjectsPanel().getProjects().add(file);
				Photoshop.getProjectsPanel().addElement(file);
			}
		} else if(command.equals(undoButton.getActionCommand())) {
			if(pointsList.size()>0) {
				removedPointsList.add(pointsList.remove(pointsList.size()-1));
			}
			repaint();
		} else if(command.equals(redoButton.getActionCommand())) {
			if(removedPointsList.size()>0) {
				pointsList.add(removedPointsList.remove(removedPointsList.size()-1));
			}
			repaint();
		} else if(command.equals(paintBrushButton.getActionCommand())) {
			currentState = BrushState.Paint;
		} else if(command.equals(lineBrushButton.getActionCommand())) {
			currentState = BrushState.Lines;
		} else if(command.equals(rectBrushButton.getActionCommand())) {
			currentState = BrushState.Rects;
		} else if(command.equals(circleBrushButton.getActionCommand())) {
			currentState = BrushState.Circles;
		}
	}
	
	public void reset() {
		pointsList = new ArrayList<Points>();
		removedPointsList = new ArrayList<Points>();
		
		burshColor = Color.black;
		currentState = BrushState.Paint;
		transparency = 255;
		brushThickness = 5;
		currentPoint = new Points(transparency, brushThickness, burshColor);
		repaint();
	}
	
	public void saveImage() {
		BufferedImage img = new BufferedImage(canvas.getWidth(), canvas.getHeight(), BufferedImage.TYPE_INT_RGB);
		canvas.paint(img.getGraphics());
		File outputfile = new File(Constants.projectDirectory+textField.getText()+".png");
		try {
			ImageIO.write(img, "png", outputfile);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public void serialize() {
		if(pointsList.size()<1) {
			return;
		}
		File directorySerialized = new File(Constants.projectDirectorySerialized);
		if(!directorySerialized.exists()) {
			directorySerialized.mkdir();
		}
		File file = new File(Constants.projectDirectorySerialized+textField.getText()+".txt");
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
		FileOutputStream fileOutputStream = null;
		ObjectOutputStream objectOutputStream = null;
		
		try{
			fileOutputStream = new FileOutputStream(file);
			objectOutputStream = new ObjectOutputStream(fileOutputStream);
			
			for(Points current: pointsList){
				objectOutputStream.writeObject(current);
			}
			
			fileOutputStream.close();
			objectOutputStream.close();
			
			System.out.print(Constants.projectDirectorySerialized+textField.getText()+".txt"+" saved\n");
		}
		catch(FileNotFoundException err){
			System.out.print(Constants.projectDirectorySerialized+textField.getText()+".txt"+" not found\n");
		}
		catch(IOException err){
			System.out.print("Error making ObjectOutputStream\n");
		}
	}
	
	public ArrayList<Points> getPointsList() {
		return pointsList;
	}
	
	public void setPointsList(ArrayList<Points> pointsList) {
		this.pointsList = pointsList;
	}
	
	public ArrayList<Points> getRemovedPointsList() {
		return removedPointsList;
	}
	
	public void setRemovedPointsList(ArrayList<Points> removedPointsList) {
		this.removedPointsList = removedPointsList;
	}
	
	public JTextField getTextField() {
		return textField;
	}
	
}
