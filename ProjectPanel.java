import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;

public class ProjectsPanel extends JPanel implements ActionListener {
	
	private ArrayList<File> projects;
	private File directorySerialized;
	private JPanel projectsPanel;
	private JButton newButton;
	
	public ProjectsPanel() {
		setLayout(new BorderLayout());
		projects = new ArrayList<File>();
		directorySerialized = new File(Constants.projectDirectorySerialized);
		
		projectsPanel = new JPanel();
		
		if(directorySerialized.listFiles() == null) {
			System.out.println(Constants.projectDirectorySerialized);
			directorySerialized.mkdir();
			
		} else {
			projectsPanel.setLayout(new GridLayout(directorySerialized.listFiles().length/2, 2));
			for(File current: directorySerialized.listFiles()){
				addElement(current);
			}
			System.out.println(projects);
		}
		
		newButton = new JButton("Create new");
		newButton.addActionListener(this);
		newButton.setActionCommand(newButton.getText());
		
		add(projectsPanel, BorderLayout.CENTER);
		add(newButton, BorderLayout.SOUTH);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if(command.equals(newButton.getActionCommand())) {
			Photoshop.getCardLayout().show(Photoshop.getMasterPanel(), "boardPanel");
		}
	}
	
	public void addElement(File current){
		projects.add(current);
		JButton button = new JButton(current.getName());
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Photoshop.getCardLayout().show(Photoshop.getMasterPanel(), "boardPanel");
				Photoshop.getBoardPanel().setPointsList(loadPoints(current.getName()));
				Photoshop.getBoardPanel().getTextField().setText(current.getName().substring(0, current.getName().length()-4));
			}
		});
		projectsPanel.add(button);
	}
	
	public ArrayList<Points> loadPoints(String fileName) {
		File file = new File(Constants.projectDirectorySerialized+fileName);
		ArrayList<Points> deserializedPoints = new ArrayList<Points>();
		
		FileInputStream fileInputStream = null;
		ObjectInputStream objectInputStream = null;
		try{
			fileInputStream = new FileInputStream(file);
			objectInputStream = new ObjectInputStream(fileInputStream);
			
			try{
				while(true){
					Points current = (Points) objectInputStream.readObject();
					deserializedPoints.add(current);
				}
			}
			catch(ClassNotFoundException e){
				System.out.print("Points class not found\n");
			}
			catch(EOFException e){
				System.out.print("All data read\n");
			}
			
			fileInputStream.close();
			objectInputStream.close();
		}
		catch(FileNotFoundException e){
			System.out.print(fileName+" not found\n");
		}
		catch(IOException e){
			System.out.print("Error making ObjectOutputStream\n");
		}
		return deserializedPoints;
	}

	public ArrayList<File> getProjects(){
		return projects;
	}
	
}
