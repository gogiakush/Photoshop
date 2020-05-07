import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Photoshop {
	
	public static final int WIDTH = 1200, HEIGHT = 800;
	
	private static JFrame frame;
	private static JPanel masterPanel;
	private static CardLayout cardLayout;
	private static ProjectsPanel projectsPanel;
	private static BoardPanel boardPanel;
	
	public Photoshop(String name) {
		frame = new JFrame(name);
		frame.setSize(WIDTH, HEIGHT);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		masterPanel = new JPanel();
		cardLayout = new CardLayout();
		masterPanel.setLayout(cardLayout);
		
		projectsPanel = new ProjectsPanel();
		boardPanel = new BoardPanel();
		
		masterPanel.add(projectsPanel, "projectsPanel");
		masterPanel.add(boardPanel, "boardPanel");
		
		cardLayout.show(masterPanel, "projectsPanel");
		//cardLayout.show(masterPanel, "boardPanel");
		
		frame.add(masterPanel);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setFocusable(true);
		frame.requestFocusInWindow();
	}
	
	public static void main(String[] args) {
		new Photoshop("gavin");
	}
	
	public static JFrame getFrame() {
		return frame;
	}
	
	public static JPanel getMasterPanel() {
		return masterPanel;
	}
	
	public static CardLayout getCardLayout() {
		return cardLayout;
	}
	
	public static ProjectsPanel getProjectsPanel() {
		return projectsPanel;
	}
	
	public static BoardPanel getBoardPanel() {
		return boardPanel;
	}
	
	
}
