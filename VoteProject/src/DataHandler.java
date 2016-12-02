import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
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
	private static ArrayList<String[]> dataArray = new ArrayList<String[]>(); //holds the districtData array
	private static ArrayList<String[]> countyArray = new ArrayList<String[]>(); //holds countyVotes array
	private static ArrayList<String[]> registeredCountyArray = new ArrayList<String[]>(); //holds the registeredCountyVotes array
	
	//final ints for accessing data in the arrays
	private final static int COUNTY_NAME = 0;
	private final static int DISTRICT_NAME = 1;
	private final static int REP_VOTES = 2;
	private final static int DEM_VOTES = 3;
	private final static int IND_VOTES = 4;
	private final static int VOTE_COUNT = 1;
	private final static int ERROR = 5;
	
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
		//extractData(COUNTY_VOTES);
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
	
	//extracts data to the appropriate array
	private static void extractData(String path, int dataType) {
		File folder = new File(path);
		File[] files = folder.listFiles();
		
		//extracts voter data to the array
		if (dataType == VOTER_DATA) {
			for (File file : files) {
				String filePath = file.getAbsolutePath();
				String[] splitPath = filePath.split("\\\\");
				String fileName = splitPath[splitPath.length-1];
				
				if (fileName.charAt(0) == '.') {
					System.out.println("Skipping file " + fileName);
				}
				
				else {
					System.out.println("Getting file from " + filePath);
					dataArray.addAll(getDistrictData(filePath));
				}
			}
			removeDistrictDuplicates();
		}
		
		else if (dataType == REGISTERED_DATA) {
			for (File file : files) {
				String filePath = file.getAbsolutePath();
				String[] splitPath = filePath.split("\\\\");
				String fileName = splitPath[splitPath.length-1];
				
				if (fileName.charAt(0) == '.') {
					System.out.println("Skipping file " + fileName);
				}
				
				else {
					System.out.println("Getting file from " + filePath);
					registeredCountyArray.addAll(getRegisterData(filePath));
				}
			}
			removeRegisterDuplicates();
		}
		
		
		
		else {
			errors.add(EXTRACTION);
			err(EXTRACTION, "getMessage, incorrect data type given " + Integer.toString(dataType));
		}
		
	}
	
	private static void removeRegisterDuplicates() {
		// TODO Auto-generated method stub
		
	}

	private static Collection<? extends String[]> getRegisterData(String filePath) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param path
	 * @return the data in arraylist form from the file
	 */
	private static ArrayList<String[]> getDistrictData(String path) {
		try {
			//get the file and scanner
			File file = new File(path);
			Scanner fileIn = new Scanner(file);
			
			//make a temporary array
			ArrayList<String[]> tempList = new ArrayList<String[]>();
			
			//loop through the lines
			while (fileIn.hasNextLine()) {
				String currentLine = fileIn.nextLine();
				String[] fixedLine = formatError(currentLine, VOTER_DATA);
				
				if (fixedLine[ERROR].equals("true")) {
					break;
				}
				
				tempList.add(fixedLine);
			}
			fileIn.close();
			return tempList;
		}
		
		catch (FileNotFoundException e) {
			errors.add(FILE_NOT_FOUND);
			err(FILE_NOT_FOUND, "getDistrictData, no file found at " + path);
			ArrayList<String[]> result = new ArrayList<String[]>();
			return result;
		}
	}
	
	/**
	 * @param line
	 * @param dataType
	 * @return boolean, whether or not a format error was found
	 */
	private static String[] formatError(String line, int dataType) {
		String[] result;
		
		//voter data format checker
		result = new String[6];
		result[ERROR] = "false";
		
		if (dataType == VOTER_DATA) {
			String[] temp = line.split(",");
			
			//check if the data is the proper length for the data type
			if (temp.length != 5) {
				errors.add(DATA_FORMAT);
				err(DATA_FORMAT, "checkFormat, " + line);
				result[ERROR] = "true";
				return result;
			}
			
			//check if any of the data is blank
			for (int i = 0; i < 5; i++) {
				if (temp[i].length() == 0) {
					errors.add(MISSING_VALUE);
					err(MISSING_VALUE, "formatError, missing value at " + line);
					result[5] = "true";
					return result;
				}
			}
			
			//add the county and district name to data
			result[COUNTY_NAME] = temp[COUNTY_NAME];
			result[DISTRICT_NAME] = temp[DISTRICT_NAME];
			
			//check if the numbers have any symbols mixed in with them
			for (int i = REP_VOTES; i < ERROR; i++) {
				try {
					int check = Integer.parseInt(temp[i]);
					//check if there is a positive number of votes
					if (check >= 0 ) {
						result[i] = temp[i];
					}
					
					else {
						errors.add(NEGATIVE_VOTES);
						err(NEGATIVE_VOTES, "formatError, negative number of votes in " + temp[COUNTY_NAME] + ", " + temp[DISTRICT_NAME]);
						result[ERROR] = "true";
						return result;
					}
					
				}
				catch (NumberFormatException e) {
					String fixed = reformat(temp[i]);
					if (fixed.equals("-1")) {
						result[ERROR] = "true";
						return result;
					}
					else result[i] = fixed;
				}
			}
			return result;
		}
		
		//registered data format checker
		else if (dataType == REGISTERED_DATA) {
			String[] temp = line.split(",");
			if (temp.length != 2) {
				errors.add(DATA_FORMAT);
				err(DATA_FORMAT, "checkFormat" + line);
				return result;
			}
			
			for (int i = 0; i < 2; i++) {
				if (temp[i].length() == 0) {
					errors.add(MISSING_VALUE);
					err(MISSING_VALUE, "formatError, missing value at " + line);
					return result;
				}
			}
			
			try {
				Integer.parseInt(temp[1]);
			}
			catch (NumberFormatException e) {
				String fixed = reformat(temp[1]);
				if (fixed.equals("-1")) {
					return result;
				}
			}
			return result;
		}
		
		else {
			errors.add(EXTRACTION);
			err(EXTRACTION, "checkFormat, incorrect data type: " + Integer.toString(dataType));
			return result;
		}
	}
	
	/**
	 * @param num
	 * @return int
	 * removes letters mixed in with numbers
	 */
	private static String reformat(String num) {
		String fixed = "";
		
		//finds any letters mixed in with the number
		for (int i = 0; i < num.length(); i++) {
			if (num.charAt(i) > '0' && num.charAt(i) < '9') {
				fixed += num.charAt(i);
			}
		}
		
		//checks if the number exists
		if (fixed.length() == 0) {
			errors.add(DATA_FORMAT);
			return "-1";
		}
		else {return fixed;}
	}
	
	/**
	 * removes any duplicates the dataArray list
	 */
	private static void removeDistrictDuplicates() {
		for (int i = 0; i < dataArray.size()-1; i++) {
			for (int k = 0; k < dataArray.size(); k++) {
				if (k == i) {k++;}
				if (isDistrictEqual(dataArray.get(i), (dataArray.get(k)))) {
					errors.add(DUPLICATE);
					//err(DUPLICATE, "removeDuplicates, duplicate found at " + dataArray.get(k)[COUNTY_NAME]);
					dataArray.remove(k);
				}
			}
		}
	}
	
	/**
	 * takes 2 string arrays and returns whether they are equal or not
	 * @return boolean
	 */
	private static boolean isDistrictEqual(String[] s1, String[] s2) {
		int matchCount = 0;
		for (int i = 0; i < 2; i++) {
			//System.out.println("Comparing " + s1[i] + " to " + s2[i]);
			if (s1[i].equals(s2[i])) {matchCount++;}
		}
		if (matchCount == 2) {
			return true;
		}
		return false;
	}
	
	private static void getRegisteredVotes
	
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
			case EXTRACTION: message = "Extract error at " + reference;
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
	
	private static void printer() {
		for (String[] array : dataArray) {
			for (String word : array) {
				System.out.println(word);
			}
		}
	}
}
