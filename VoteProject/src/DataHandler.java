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
	private final static int EXTRACTION = -1;
	private final static int FILE_NOT_FOUND = -2;
	private final static int DATA_FORMAT = -3;
	private final static int DUPLICATE = -4;
	private final static int TOO_MANY_VOTES = -5;
	private final static int NEGATIVE_VOTES = -6;
	private final static int MISSING_VALUE = -7;
	private final static int EXTRA_SYMBOL = -8;
	private final static int CORRUPT = -9;
	private final static int TAMPER = -10;
	
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
			dataArray = getDistrictData(path);
		}
		
		else if (dataType == REGISTERED_DATA) {
			registeredCountyVotes = getRegisteredVotes();
		}
		
		else {
			errors.add(EXTRACTION);
			err(EXTRACTION, "getMessage, inccorect data type given " + Integer.toString(dataType));
		}
	}
	
	private static ArrayList getDistrictData(String path) {
		try {
			//get the file and scanner
			File file = new File(path);
			Scanner fileIn = new Scanner(file);
			
			//create two temporary arrays to hold county data for checking
			ArrayList<String> tempArray = new ArrayList<String>();
			String[] tempData = new String[5];
			
			//loop through the lines
			while (fileIn.hasNextLine()) {
				String currentLine = fileIn.nextLine();
				checkFormat(currentLine, VOTER_DATA);
				tempArray.add(currentLine);
			}
		}
		
		catch (FileNotFoundException e) {
			errors.add(FILE_NOT_FOUND);
			err(FILE_NOT_FOUND, "getDistrictData, no file found at " + path);
			return null;
		}
		return null;
	}
	
	/**
	 * @param line
	 * @param dataType
	 * @return boolean, whether or not a format error was found
	 */
	private static boolean checkFormat(String line, int dataType) {
		if (dataType == VOTER_DATA) {
			String[] temp = line.split(",");
			if (temp.length != 5) {
				errors.add(DATA_FORMAT);
				err(DATA_FORMAT, "checkFormat, " + line);
			}
			
			try {
				Integer.parseInt(temp[2]);
			}
			catch (NumberFormatException e) {
				reformat(temp[2]);
			}
		}
	}
	
	
	
	//TODO: get the county votes data
	private static void extractData(int dataType) {
		if (dataType != COUNTY_VOTES) {
			errors.add(EXTRACTION);
			err(EXTRACTION, "extractData, incorrect dataType given: " + Integer.toString(dataType));
		}
	}
	
	private void getDataFromFile() {
		//create a scanner that reads in the voter data from the voter data file
		
	}
	
	public void addError(int error) {
		errors.add(error);
	}
	
	private static void err(int error, String reference) {
		String message;
		switch (error) {
			case EXTRACTION: message = "Extract error at " + reference ;
			break;
			case FILE_NOT_FOUND: message = "File not found at " + reference;
			break;
			case DATA_FORMAT: message = "data format error at " + reference;
			break;
			case DUPLICATE: message = "duplicate region found at " + reference;
			break;
			case TOO_MANY_VOTES: message = "too many voters found at " + reference;
			break;
			case NEGATIVE_VOTES: message = "negative voters found at " + reference;
			break;
			case MISSING_VALUE: message = "missing value at " + reference;
			break;
			case EXTRA_SYMBOL: message = "extra symbols found at " + reference;
			break;
			case CORRUPT: message = "data is corrupt at " + reference;
			break;
			case TAMPER: message = "data has been tampered with at " + reference;
			break;
			default: message = "error not recognized, error number " + Integer.toString(error);
		}
		JOptionPane.showMessageDialog(null, message);
	}
}
