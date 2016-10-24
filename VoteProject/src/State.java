import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class State {
	final int COUNTY_NAME = 0;
	final int DISTRICT_NAME = 1;
	final int REP_VOTES = 2;
	final int DEM_VOTES = 3;
	final int IND_VOTES = 4;
	
	
	private File voterData;
	private File verifyData;
	private String currentCounty = "";
	private County newCounty;
	private ArrayList<County> counties;
	
	public State() {
		this.voterData = getFile();
		//this.verifyData = getFile();
		getCountiesAndDistricts();
	}
	
	/**
	 * @param countyName, the name of the county you want to select
	 * @return County, the county with the name you wanted
	 */
	public County selectCounty(String countyName) {
		for (int i = 0; i < this.counties.size(); i++) {
			if (countyName.equals(this.counties.get(i).getName())) {
				return this.counties.get(i);
			}
		}
		return null;
	}
	
	/**
	 * @return ArrayList, the counties contained within a state
	 */
	public ArrayList<County> getCounties() {
		return this.counties;
	}
	
	/**
	 * Initializes counties and districts under a state automatically
	 */
	private void getCountiesAndDistricts() {
		this.counties = new ArrayList<County>();
		this.currentCounty = "";
		this.newCounty = new County("");
		
		try (Scanner fileIn = new Scanner(this.voterData);) {
			String currentCountyName = "";
			while (fileIn.hasNextLine()) {
				String line = fileIn.nextLine();
				String[] data = line.split(",");
				currentCountyName = data[COUNTY_NAME];
				
				if (!this.currentCounty.equals(currentCountyName)) {
					this.currentCounty = currentCountyName;
					this.newCounty = new County(currentCountyName);
					this.counties.add(this.newCounty);
				}
				
				District newDistrict = new District(data[DISTRICT_NAME], Integer.parseInt(data[REP_VOTES]),
						Integer.parseInt(data[DEM_VOTES]), Integer.parseInt(data[IND_VOTES]));
				this.newCounty.addDistrict(newDistrict);
			}
		}
		catch (FileNotFoundException e) {
			System.out.println("FileNotFound");
		}
	}
	
	private boolean verifyRecords(){
		return true;
	}
	
	/**
	 * @return a File chosen by the user
	 */
	private static File getFile() {
		
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
		return this.counties.toString();
	}
}
