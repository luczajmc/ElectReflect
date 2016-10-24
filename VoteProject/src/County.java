import java.util.ArrayList;

public class County {
	private String name;
	private ArrayList<District> districts = new ArrayList<District>();;
	private int demVotes;
	private int repVotes;
	private int indVotes;
	private int totalVotes;
	
	/**
	 * @param name, the name of the county
	 */
	public County(String name) {
		this.name = name;
		demVotes = 0;
		repVotes = 0;
		indVotes = 0;
		totalVotes = 0;
		getData();
	}
	
	/**
	 * calculates the voting data for a county
	 */
	private void getData() {
		calcDemVotes();
		calcRepVotes();
		calcIndVotes();
		calcTotalVotes();
	}
	
	/**
	 * gets the total number of democratic votes for a county
	 */
	private void calcDemVotes() {
		for (int i = 0; i < this.districts.size(); i++) {
			this.demVotes = this.demVotes + this.districts.get(i).getDemVotes();
		}
	}
	
	/**
	 * gets the total number of republican votes for a county
	 */
	private void calcRepVotes() {
		for (int i = 0; i < this.districts.size(); i++) {
			this.repVotes = this.repVotes + this.districts.get(i).getRepVotes();
		}
	}
	
	/**
	 * gets the number of independent votes for a county
	 */
	private void calcIndVotes() {
		for (int i = 0; i < this.districts.size(); i++) {
			this.indVotes = this.indVotes + this.districts.get(i).getIndVotes();
		}
	}
	
	/**
	 * gets the total number of votes for a county
	 */
	private void calcTotalVotes() {
		this.totalVotes = this.demVotes + this.repVotes + this.indVotes;
	}
	
	/**
	 * @param newDistrict, a District you want added to the County
	 */
	public void addDistrict(District newDistrict) {
		this.districts.add(newDistrict);
		getData();
	}
	
	/**
	 * @return ArrayList, the districts in a county
	 */
	public ArrayList getDistricts() {
		return this.districts;
	}

	/**
	 * @return String, the name of the county
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * @return int, the number of democratic votes
	 */
	public int getDemVotes() {
		return this.demVotes;
	}
	
	/**
	 * @return int, the number of republican votes
	 */
	public int getRepVotes() {
		return this.repVotes;
	}
	
	/**
	 * @return int, the number of independent votes
	 */
	public int getIndVotes() {
		return this.indVotes;
	}
	
	/**
	 * @return double, the percent of votes that were democratic
	 */
	public double getDemPercent() {
		return this.demVotes/this.totalVotes;
	}
	
	/**
	 * @return double, the percent of votes that were republican
	 */
	public double getRepPercent() {
		return this.repVotes/this.totalVotes;
	}
	
	/**
	 * @return double, the percent of votes that were independent
	 */
	public double getIndPercent() {
		return this.indVotes/this.totalVotes;
	}
	
}
