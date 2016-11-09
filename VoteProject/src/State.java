import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class State extends Region {
	final int COUNTY_NAME = 0;
	final int DISTRICT_NAME = 1;
	final int REP_VOTES = 2;
	final int DEM_VOTES = 3;
	final int IND_VOTES = 4;
	
	private File voterData;
	private File verifyData;
	private String currentCounty;
	private County newCounty;
	private ArrayList<County> counties; //holds all the counties
	
	private int repVotes;
	private int demVotes;
	private int indVotes;
	private int totalVotes;
	
	/**
	 * Creates a state, opens a file browser to get the voter data file and the voter verify file
	 * Verifies the data being given by voter data file, and creates counties and districts from the data
	 */
	
	public State(int repVotes, int demVotes, int indVotes) {
		this.repVotes = repVotes;
		this.demVotes = demVotes;
		this.indVotes = indVotes;
		this.totalVotes = repVotes+demVotes+indVotes;
		
	}
	public State() {
		this.voterData = getFile();
		this.verifyData = getFile();
		this.repVotes = 0;
		this.demVotes = 0;
		this.indVotes = 0;
		this.totalVotes = 0;
		getCountiesAndDistricts(); //get all the data
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
		getData();
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
	 * calculates the voting numbers for a state
	 */
	private void getData() {
		calcRepVotes();
		calcDemVotes();
		calcIndVotes();
		calcTotalVotes();
	}
	
	/**
	 * calculates the number of republican votes
	 */
	private void calcRepVotes() {
		for (int i = 0; i < this.counties.size(); i++) {
			this.repVotes = this.repVotes + this.counties.get(i).getRepVotes();
		}
	}
	
	/**
	 * calculates the number of democratic votes
	 */
	private void calcDemVotes() {
		for (int i = 0; i < this.counties.size(); i++) {
			this.demVotes = this.demVotes + this.counties.get(i).getDemVotes();
		}
	}
	
	/**
	 * calculates the number of independent votes
	 */
	private void calcIndVotes() {
		for (int i = 0; i < this.counties.size(); i++) {
			this.indVotes = this.indVotes + this.counties.get(i).getIndVotes();
		}
	}
	
	/**
	 * calculates the total number of votes
	 */
	private void calcTotalVotes() {
		this.totalVotes = this.repVotes + this.demVotes + this.indVotes;
	}
	
	/**
	 * @return the number of republican votes
	 */
	public int getRepVotes() {
		return this.repVotes;
	}
	
	/**
	 * @return the number of democratic votes
	 */
	public int getDemVotes() {
		return this.demVotes;
	}
	
	/**
	 * @return the number of independent votes
	 */
	public int getIndVotes() {
		return this.indVotes;
	}
	
	/**
	 * @return the total number of votes
	 */
	public int getTotalVotes() {
		return this.totalVotes;
	}
	
	/**
	 * @return the percent of republican votes
	 */
	public double getRepPercent() {
		return (double)this.repVotes / this.totalVotes;
	}
	
	/**
	 * @return the percent of democratic votes
	 */
	public double getDemPercent() {
		return (double)this.demVotes / this.totalVotes;
	}
	
	/**
	 * @return the percent of independent votes
	 */
	public double getIndPercent() {
		return (double)this.indVotes / this.totalVotes;
	}
	
	public String getName() {
		return "Ohio";
	}
}
