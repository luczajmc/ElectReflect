import java.awt.Component;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class State {
	final int COUNTY_NAME = 0;
	final int DISTRICT_NAME = 1;
	final int REP_VOTES = 2;
	final int DEM_VOTES = 3;
	final int IND_VOTES = 4;
	
	
	private File voterData;
	private File verifyData;
	private String currentCounty;
	private County newCounty;
	private ArrayList<County> counties;
	
	public State(File voterFile, File verifyFile) {
		this.voterData = voterFile;
		this.verifyData = verifyFile;
	}
	
	public void getCountiesAndDistricts() {
		try (Scanner fileIn = new Scanner(this.voterData);) {
			while (fileIn.hasNextLine()) {
				String line = fileIn.nextLine();
				String[] data = line.split(",");
				String name = data[COUNTY_NAME];
				
				if (!this.currentCounty.equals(name)) {
					this.newCounty = new County(name);
					this.counties.add(newCounty);
				}
				
				District newDistrict = new District(data[DISTRICT_NAME], Integer.parseInt(data[REP_VOTES]),
						Integer.parseInt(data[DEM_VOTES]), Integer.parseInt(data[IND_VOTES]));
				this.newCounty.addDistrict(newDistrict);
			}
		}
		catch (FileNotFoundException e) {
			
		}
	}
	
	/**
	 * @return a File chosen by the user
	 */
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
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 * @return a String with the path of the directory selected for the state
	 */
	public String toString() {
		return this.voterData.toString() + "\n" + this.verifyData.toString();
	}
}
