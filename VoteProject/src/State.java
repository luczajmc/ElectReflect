import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class State extends Region {
	final int COUNTY_NAME = 0;
	final int DISTRICT_NAME = 1;
	final int REP_VOTES = 2;
	final int DEM_VOTES = 3;
	final int IND_VOTES = 4;
	
	private File voterData;
	private File logFile;
	private File filterFile;
	private PrintWriter out;
	private String currentCounty;
	private County newCounty;
	private ArrayList<County> counties = new ArrayList<County>(); //holds all the counties
	
	private int repVotes;
	private int demVotes;
	private int indVotes;
	private int totalVotes;
	
	public State(ArrayList<String[]> data) {
		//initialize needed variables
		this.counties = new ArrayList<County>();
		this.currentCounty = "";
		this.newCounty = new County("");
		
		String currentCountyName = "";
		for (String[] currentDistrict : data) {
			currentCountyName = currentDistrict[COUNTY_NAME]; // set the current county name as the name of the county in the line
			
			// TODO: this needs to handle duplicate items that aren't right next to each other
			if (!this.currentCounty.equals(currentCountyName)) { //checks if the name of the current county == the name 
																 //of the county in the previous line
				System.out.println(this.newCounty.getName() + " " + this.newCounty.getTotalVotes());
				this.currentCounty = currentCountyName; //sets the current county name to the new county name
				this.newCounty = new County(currentCountyName); //creates a new county with name currentCountyName
				this.counties.add(this.newCounty); //adds the new county to the ArrayList of counties contained within the state
			}
			
			//every line is a new district, so we add a new district every line and pass its relevant information
			District newDistrict = new District(currentDistrict[DISTRICT_NAME], Integer.parseInt(currentDistrict[REP_VOTES]),
					Integer.parseInt(currentDistrict[DEM_VOTES]), Integer.parseInt(currentDistrict[IND_VOTES]));
			this.newCounty.addDistrict(newDistrict);//finally we add the new district to the county we are working in
			
			System.out.println(newDistrict.getName() + " " + newDistrict.getTotalVotes());
			System.out.println(newCounty.getName() + " " + newCounty.getTotalVotes());
		}
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
		return (double) checkNum(this.demVotes, this.totalVotes);
	}
	
	/**
	 * @return the percent of democratic votes
	 */
	public double getDemPercent() {
		return (double) checkNum(this.repVotes, this.totalVotes);
	}
	
	/**
	 * @return the percent of independent votes
	 */
	public double getIndPercent() {
		return (double) checkNum(this.indVotes, this.totalVotes);
	}
	
	public String getName() {
		return "Ohio";
	}
	
	@Override
	public ArrayList<Region> getSubregions() {
		return new ArrayList<Region>(getCounties());
	}
	
	/**
	 * @return the resulting percentage after checking for arithmetic errors
	 */
	private double checkNum(double doubleIn, double totalVotes) {
		try {
			double result = doubleIn / totalVotes;
			return result;
		} catch (ArithmeticException e) {
			return 0;
		}
	}
}
