import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
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
	private final static int REG_ERROR = 2;
	
	//ints for extracting data
	private final static int VOTER_DATA = 1;
	private final static int COUNTY_VOTES = 2;
	private final static int REGISTERED_DATA =3;
	
	//ints for logging errors
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
	
	//file for logging errors
	private static File errorLog = new File( "console_log_" + LocalDate.now() + ".txt");
	private static PrintWriter out;
	
	public static void main(String[] args) {
		makeState();
	}
	
	public static State makeState(String directory) {
		return makeState(directory, directory);
	}
	
	public static State makeState(String resultsDirectory,
			String registeredVotersDirectory) {
		System.out.println("Preparing printwriter");
		try {
			out = new PrintWriter(errorLog);
		}
		catch (FileNotFoundException e) {
			System.out.println("Something went wrong");
		}
		out.print("-- PrintWriter created, begin logging. --\r\n\r\n");
		
		out.print("Prompting for voter files...");
		extractData(resultsDirectory, VOTER_DATA);
		out.print("done.\r\n\r\n");
		
		out.print("Sorting voter data...");
		sort(dataArray);
		out.print("done.\r\n\r\n");
		
		out.print("Prompting for registered voter data...");
		extractData(registeredVotersDirectory, REGISTERED_DATA);
		out.print("done.\r\n\r\n");
		
		out.print("Verifying data integrity...");
		extractData(COUNTY_VOTES);
		out.print("done.\r\n\r\n");
		
		out.print("Sorting county...");
		sort(registeredCountyArray);
		out.print("done.\r\n\r\n");
		
		out.print("Verifying number of voters...");
		verifyVoters();
		out.print("done.\r\n\r\n");
		
		out.println("Creating State object.");
		out.close();
		return new State(dataArray);
	}
	
	static String chooseDirectory(String message) {
        //show input dialog
        JOptionPane.showMessageDialog(null, message);

		//filechooser
        // FIXME: the title bar should say Choose a folder, but it says Open
		JFrame fileFrame = new JFrame("Choose a folder");
		fileFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// TODO: windo
		//show the filechooser
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.setCurrentDirectory(new File(System.getProperty("user.home")));
		int result = fc.showOpenDialog(fileFrame);
		
		//extract the data depending on the type
		if (result == JFileChooser.APPROVE_OPTION) {
			String path = fc.getSelectedFile().getAbsolutePath().toString(); //returns a string of the directory
			return path;
		}
		
		return null;

	}
	
	public static State makeState() {
		return makeState(chooseDirectory(getMessage(VOTER_DATA)),
				chooseDirectory(getMessage(REGISTERED_DATA)));
	}

//sorter (I'm sorry)
//==================================================================================
	private static void sort(ArrayList<String[]> sortThis) {
		for (int i = 1; i < sortThis.size(); i++) {
			for (int k = 0; k < sortThis.size()-i; k++) {
				if (sortThis.get(k)[COUNTY_NAME].compareTo(sortThis.get(k+1)[COUNTY_NAME]) > 0) {
					//System.out.println("Swapped " + sortThis.get(k)[COUNTY_NAME] + " with " + sortThis.get(k+1)[COUNTY_NAME]);
					swap(sortThis, k, k+1);
				}
			}
		}
	}
	
	private static void swap(ArrayList<String[]> sortThis, int pos1, int pos2) {
		String[] temp = sortThis.get(pos2);
		sortThis.set(pos2, sortThis.get(pos1));
		sortThis.set(pos1, temp);
	}
//==================================================================================
	
	private static void verifyVoters() {
		String[] countyVotes = new String[2];
		
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
					out.println("Skipping file " + fileName);
				}
				
				else {
					out.println("Getting file from " + filePath);
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
					out.println("Skipping file " + fileName);
				}
				
				else {
					out.println("Getting file from " + filePath);
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
	
	/**
	 * removes any duplicates the dataArray list
	 */
	private static void removeRegisterDuplicates() {
		for (int i = 0; i < registeredCountyArray.size()-1; i++) {
			for (int k = 0; k < registeredCountyArray.size(); k++) {
				if (k == i) {k++;}
				if (isRegEqual(registeredCountyArray.get(i), registeredCountyArray.get(k))) {
					errors.add(DUPLICATE);
					err(DUPLICATE, "removeDuplicates, duplicate found at " + dataArray.get(k)[COUNTY_NAME]);
					registeredCountyArray.remove(k);
				}
			}
		}
	}
	
	/**
	 * takes 2 registered data arrays and returns whether they are equal or not
	 * @return boolean
	 */
	private static boolean isRegEqual(String[] s1, String[] s2) {
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

	private static ArrayList<String[]> getRegisterData(String filePath) {
		try {
			//get the file and scanner
			File file = new File(filePath);
			Scanner fileIn = new Scanner(file);
			
			//make a temporary array
			ArrayList<String[]> tempList = new ArrayList<String[]>();
			
			//loop through the lines
			while (fileIn.hasNextLine()) {
				String currentLine = fileIn.nextLine();
				String[] fixedLine = formatError(currentLine, REGISTERED_DATA);
				
				if (fixedLine[REG_ERROR].equals("true")) {
					break;
				}
				
				tempList.add(fixedLine);
			}
			fileIn.close();
			return tempList;
		}
		
		catch (FileNotFoundException e) {
			errors.add(FILE_NOT_FOUND);
			err(FILE_NOT_FOUND, "getRegisterData, no file found at " + filePath);
			ArrayList<String[]> result = new ArrayList<String[]>();
			return result;
		}
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
					result[ERROR] = "true";
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
			result[REG_ERROR] = "false";
			
			if (temp.length != 2) {
				errors.add(DATA_FORMAT);
				err(DATA_FORMAT, "checkFormat" + line);
				result[REG_ERROR] = "true";
				return result;
			}
			
			for (int i = 0; i < 2; i++) {
				if (temp[i].length() == 0) {
					errors.add(MISSING_VALUE);
					err(MISSING_VALUE, "formatError, missing value at " + line);
					result[REG_ERROR] = "true";
					return result;
				}
			}
			
			//add county name to array
			result[COUNTY_NAME] = temp[COUNTY_NAME];
			
			try {
				int check = Integer.parseInt(temp[VOTE_COUNT]);
				//check if there is a positive number of votes
				if (check >= 0 ) {
					result[VOTE_COUNT] = temp[VOTE_COUNT];
				}
				
				else {
					errors.add(NEGATIVE_VOTES);
					err(NEGATIVE_VOTES, "formatError, negative number of votes in " + temp[COUNTY_NAME]);
					result[REG_ERROR] = "true";
					return result;
				}
			}
				
			catch (NumberFormatException e) {
				String fixed = reformat(temp[VOTE_COUNT]);
				if (fixed.equals("-1")) {
					result[REG_ERROR] = "true";
					return result;
				}
				else result[VOTE_COUNT] = fixed;
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
					err(DUPLICATE, "removeDuplicates, duplicate found at " + dataArray.get(k)[COUNTY_NAME] + ": " + dataArray.get(k)[DISTRICT_NAME]);
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
	
	//TODO: get the county votes data
	private static void extractData(int dataType) {
		if (dataType != COUNTY_VOTES) {
			errors.add(EXTRACTION);
			err(EXTRACTION, "extractData, incorrect dataType given: " + Integer.toString(dataType));
		}
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
		out.write(message);
		out.println();
		out.flush();
	}
	
	private static void printer() {
		try {
			File file = new File("dataArray");
			PrintWriter out = new PrintWriter(file);
			for (String[] array : dataArray) {
				for (String word : array) {
					out.print(word + ", ");
				}
				out.println();
			}
			out.close();
		}
		catch (FileNotFoundException e) {
			System.out.println("OMFG");
		}
	}
}