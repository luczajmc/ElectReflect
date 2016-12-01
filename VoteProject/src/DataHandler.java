import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * @author Reese Wells
 *	1. Imports voter files
 *	2. Imports verification files
 *	3. Verifies the data
 *	4. Saves the data
 */
public class DataHandler {
	//the arrays for saving the data
	private static ArrayList<String[]> dataArray; //holds the districtData array
	private static ArrayList<String[]> countyArray; //holds countyVotes array
	private static ArrayList<String[]> registeredCountyArray; //holds the registeredCountyVotes array
	private static String[] districtData = new String[5]; //[county, district, repubVotes, demVotes, indVotes]
	private static String[] countyVotes = new String[2]; //[county, voters]
	private static String[] registeredCountyVotes = new String[2]; //[county, registeredVoters]
	
	//final ints for accessing data in the arrays
	private final static int COUNTY_NAME = 0;
	private final static int VOTE_COUNT = 1;
	private final static int DISTRICT_NAME = 1;
	private final static int REP_VOTES = 2;
	private final static int DEM_VOTES = 3;
	private final static int IND_VOTES = 4;
	
	//ints for extracting data
	private final static int VOTER_DATA = 1;
	private final static int COUNTY_VOTES = 2;
	private final static int REGISTERED_DATA =3;
	
	//ints for logging errors
	//TODO: add more errors
	private static ArrayList<Integer> errors = new ArrayList<Integer>();
	private final static int EXTRACT_ERROR = -1;
	private final static int FILE_NOT_FOUND = -2;
	
	public static void main(String[] args) {
		getFiles(VOTER_DATA);
		getFiles(REGISTERED_DATA);
		extractData(COUNTY_VOTES);
	}
	
	private static void getFiles(int dataType) {
		
		String message = getMessage(dataType);
		
		//show input dialog
		JOptionPane.showMessageDialog(null, message);
		
		//filechooser
		JFrame fileFrame = new JFrame("Choose a folder");
		fileFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//show the filechooser
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.setCurrentDirectory(new File(System.getProperty("user.home")));
		int result = fc.showOpenDialog(fileFrame);
		
		//extract the data depending on the type
		if (result == JFileChooser.APPROVE_OPTION) {
			String path = fc.getSelectedFile().getAbsolutePath().toString(); //returns a string of the directory
			extractData(path, dataType);
		}
	}
	
	private static String getMessage(int dataType) {
		if (dataType == VOTER_DATA) {
			return "Select the file with voting data";
		}
		
		else {
			return "Select the file with the registered voters";
		}
	}
	
	//TODO: extract the data to the array
	private static void extractData(String path, int dataType) {
		if (dataType == VOTER_DATA) {
			dataArray = getDistrictData();
		}
		
		else if (dataType == REGISTERED_DATA) {
			registeredCountyVotes = getRegisteredVotes();
		}
		
		else {errors.add(-1);}
	}
	
	//TODO: get the county votes data
	private static void extractData(int dataType) {
		if (dataType)
	}
	
	private void getDataFromFile() {
		//create a scanner that reads in the voter data from the voter data file
		
	}
	
	public void addError(int error) {
		errors.add(error);
	}
}
