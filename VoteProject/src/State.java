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
	private String currentCounty;
	private County newCounty;
	private ArrayList<County> counties;
	
	/**
	 * Creates a state, opens a file browser to get the voter data file and the voter verify file
	 * Verifies the data being given by voter data file, and creates counties and districts from the data
	 */
	public State() {
		this.voterData = getFile();
		this.verifyData = getFile();
		getCountiesAndDistricts();
	}
	
	private void getCountiesAndDistricts() {
		//initialize needed variables
		this.counties = new ArrayList<County>();
		this.currentCounty = "";
		this.newCounty = new County("");
		
		//create a scanner that reads in the voter data from the voter data file
		try (Scanner fileIn = new Scanner(this.voterData);) {
			String currentCountyName = "";
			while (fileIn.hasNextLine()) {
				String line = fileIn.nextLine(); //read next line
				String[] data = line.split(","); //split the line into an array containing each comma separated value
				currentCountyName = data[COUNTY_NAME]; // set the current county name as the name of the county in the line
				
				if (!this.currentCounty.equals(currentCountyName)) { //checks if the name of the current county == the name 
																	 //of the county in the previous line
					this.currentCounty = currentCountyName; //sets the current county name to the new county name
					this.newCounty = new County(currentCountyName); //creates a new county with name currentCountyName
					this.counties.add(this.newCounty); //adds the new county to the ArrayList of counties contained within the state
				}
				
				//every line is a new district, so we add a new district every line and pass its relevant information
				District newDistrict = new District(data[DISTRICT_NAME], Integer.parseInt(data[REP_VOTES]),
						Integer.parseInt(data[DEM_VOTES]), Integer.parseInt(data[IND_VOTES]));
				this.newCounty.addDistrict(newDistrict);//finally we add the new district to the county we are working in
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
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 * @return a String with the path of the directory selected for the state
	 */
	public String toString() {
		return this.counties.toString();
	}
}
