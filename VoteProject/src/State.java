import java.awt.Component;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class State {
	private File voterData;
	private File verifyData;
	private String currentCounty;
	private County newCounty;
	private ArrayList counties;
	
	public State(File voterFile, File verifyFile) {
		this.voterData = voterFile;
		this.verifyData = verifyFile;
	}
	
	public static File getFile() {
		
		JFrame fileFrame = new JFrame("Choose CSV File");
		fileFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new File(System.getProperty("user.home")));
		int result = fc.showOpenDialog(fileFrame);
		
		if (result == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			return file;
		}
		return null;
	}
	
	public String toString() {
		return this.voterData.toString();
	}
}
