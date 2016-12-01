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
	
	/**
	 * Creates a state, opens a file browser to get the voter data file and the voter verify file
	 * Verifies the data being given by voter data file, and creates counties and districts from the data
	 */
	
	public State(String fileName) {
		this(new File(fileName));
	}
	
	public State(File data) {
		this.voterData = data;
		this.repVotes = 0;
		this.demVotes = 0;
		this.indVotes = 0;
		this.totalVotes = 0;
		getCountiesAndDistricts(); //get all the data
	}
	
	public State(int repVotes, int demVotes, int indVotes) {
		this.repVotes = repVotes;
		this.demVotes = demVotes;
		this.indVotes = indVotes;
		this.totalVotes = repVotes+demVotes+indVotes;
		
	}
	public State() {
		this(getFile("Select the file with voter data"));
	}
	
	private void getCountiesAndDistricts() {
		//initialize needed variables
		this.counties = new ArrayList<County>();
		this.currentCounty = "";
		this.newCounty = new County("");
		int lineCounter = 1;
		
		//create a scanner that reads in the voter data from the voter data file
		try{
			verifyRecords();
			out.close();
			Scanner fileIn = null;
			try {
				fileIn = new Scanner(filterFile);
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(null, "File Not Found");
			}
			String currentCountyName = "";
			if (this.voterData.length() == 0) {
				JOptionPane.showMessageDialog(Gui.getFrame(), "File contains no data.");
				return;
			}
			while (fileIn.hasNextLine()) {
				String line = fileIn.nextLine(); //read next line
				String[] data = line.split(","); //split the line into an array containing each comma separated value
				currentCountyName = data[COUNTY_NAME]; // set the current county name as the name of the county in the line
				
				// TODO: this needs to handle duplicate items that aren't right next to each other
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
				lineCounter++;
			}
		}
		catch(NullPointerException n){
			JOptionPane.showMessageDialog(Gui.getFrame(), "No file provided.");
		}
		catch(ArrayIndexOutOfBoundsException a){
			JOptionPane.showMessageDialog(Gui.getFrame(), "File contains missing or invalid data. (Line: " + lineCounter + ")");
			this.counties.clear();
		}
		catch(NumberFormatException f){
			JOptionPane.showMessageDialog(Gui.getFrame(), "File contains missing or invalid data. (Line: " + lineCounter + ")");
			this.counties.clear();
		}
		getData();
	}
	
	/**
	 * verifies and filters the data by removing duplicates and counting votes
	 */
	private void verifyRecords(){
		logFile = new File("Error_Log.txt");
		
		try {
			out = new PrintWriter(logFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		File checkFile = getFile("Select the file to verify the number of votes");
		findAndRemoveDuplicates();
		System.out.println("duplicates removed");
		checkNumVotes(checkFile);
		System.out.println("Votes tallied, incorrect numbers have been corrected");
	}
	
	/**
	 * @return a file with the duplicate problem taken care of
	 */
	private void findAndRemoveDuplicates() {
		try {
			Scanner fileIn = new Scanner(voterData);
			filterFile = new File("Filtered_Data.csv");
			PrintWriter filterPrint = new PrintWriter(filterFile);
			String[] countyDistrict = {"","","","",""};
			ArrayList<String[]> countyDistrictList = new ArrayList<String[]>();
			
			while (fileIn.hasNextLine()) {
				String line = fileIn.nextLine();
				String[] data = line.split(",");
				
				countyDistrict = line.split(","); //adds the new line to an array for comparison
			
				if (!duplicate(countyDistrictList, countyDistrict)) { //checks if countyDistrict is not already in the countyDistrictList
					countyDistrictList.add(countyDistrict);
				}
				
				else {
					out.println("Duplicate Found: " + data[0] + ", " + data[1]);
					out.flush();
				}
			}
			for (int i = 0; i < countyDistrictList.size(); i++) {
				filterPrint.println(countyDistrictList.get(i)[0] + ", " + 
						countyDistrictList.get(i)[1] + "," +
						countyDistrictList.get(i)[2] + "," + 
						countyDistrictList.get(i)[3] + "," +
						countyDistrictList.get(i)[4]);
				filterPrint.flush();
			}
		}
		catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(Gui.getFrame(), "File not found.");
		}
		catch(NullPointerException n){
			JOptionPane.showMessageDialog(Gui.getFrame(), "No file provided.");
		}
		catch(ArrayIndexOutOfBoundsException a){
			JOptionPane.showMessageDialog(Gui.getFrame(), "Index out of bounds.");
			this.counties.clear();
		}
		catch(NumberFormatException f){
			JOptionPane.showMessageDialog(Gui.getFrame(), "Number is not formatted correctly.");
			this.counties.clear();
		}
	}
	
	/**
	 * @param countyDistrictList
	 * @param countyDistrict
	 * @return if the current line is a duplicate
	 */
	private boolean duplicate(ArrayList<String[]> countyDistrictList, String[] countyDistrict) {
		for (int i = 0; i < countyDistrictList.size(); i++) { //loops through countyDistrictList
			int duplicateCheck = 0;
			for (int k = 0; k < countyDistrict.length; k++) { //loops through the countyDistrict array
				if (countyDistrictList.get(i)[k].equals(countyDistrict[k])) { //if the countyDistrict == countyDistrictList
					duplicateCheck++;
				}
				if (duplicateCheck == 5) {
					return true;
				}
			}
		}
		return false;
	}
	
	public void checkNumVotes(File checkFile) {
		ArrayList<String[]> fixedData = new ArrayList<String[]>();
		try {
			Scanner checkIn = new Scanner(checkFile);
			Hashtable<String, Integer> countyAndVoteNum = new Hashtable<>();
			while (checkIn.hasNextLine()) {
				String[] line = checkIn.nextLine().split(",");
				String county = line[0];
				int regVoters = Integer.parseInt(line[1]);
				countyAndVoteNum.put(county, regVoters);
			}
			
			Scanner filterScanner = new Scanner(filterFile);
			
			while (filterScanner.hasNextLine()) {
				String[] line = filterScanner.nextLine().split(",");
				String county = line[0];
				int voters = Integer.parseInt(line[2]) + Integer.parseInt(line[3]) +
						Integer.parseInt(line[4]);
				
				if (voters > countyAndVoteNum.get(county)) {
					out.println("County " + county + " has too many votes!");
					out.flush();
				}
				
				else {
					fixedData.add(line);
					System.out.println(fixedData.get(0)[0] + fixedData.get(0)[1]);
				}
			}
			
			checkIn.close();
			filterScanner.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "File Not Found");
		}
		
		try {
			PrintWriter filterOut = new PrintWriter(filterFile);
			for (int i = 0; i < fixedData.size(); i++) {
				filterOut.println(fixedData.get(i)[0] + "," + 
						fixedData.get(i)[1] + "," +
						fixedData.get(i)[2] + "," + 
						fixedData.get(i)[3] + "," +
						fixedData.get(i)[4]);
			}
			filterOut.flush();
			filterOut.close();
		}
		
		catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "File Not Found");
		}
		
	}
	
	/**
	 * @return a File chosen by the user
	 */
	private static File getFile(String prompt) {
		
		JOptionPane.showMessageDialog(null, prompt);
		
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
