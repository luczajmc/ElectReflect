import java.util.ArrayList;

public class County extends Region {
	private String name;
	private ArrayList<District> districts = new ArrayList<District>(); // holds
																		// all
																		// the
																		// districts
	private int demVotes;
	private int repVotes;
	private int indVotes;
	private int totalVotes;

	/**
	 * @param name,
	 *            the name of the county
	 */
	public County(String name) {
		this.name = name;
		demVotes = 0;
		repVotes = 0;
		indVotes = 0;
		totalVotes = 0;
		getData();
	}

	public County(String name, int repVotes, int demVotes, int indVotes) {
		this.name = name;
		this.repVotes = repVotes;
		this.demVotes = demVotes;
		this.indVotes = indVotes;
		this.totalVotes = repVotes + demVotes + indVotes;

	}

	public District selectDistrict(String districtName) {
		for (int i = 0; i < this.districts.size(); i++) {
			if (districtName.equals(this.districts.get(i).getName())) {
				return this.districts.get(i);
			}
		}
		return null;
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
		this.demVotes = 0;
		for (int i = 0; i < this.districts.size(); i++) {
			this.demVotes = this.demVotes + this.districts.get(i).getDemVotes();
		}
	}

	/**
	 * gets the total number of republican votes for a county
	 */
	private void calcRepVotes() {
		this.repVotes = 0;
		for (int i = 0; i < this.districts.size(); i++) {
			this.repVotes = this.repVotes + this.districts.get(i).getRepVotes();
		}
	}

	/**
	 * gets the number of independent votes for a county
	 */
	private void calcIndVotes() {
		this.indVotes = 0;
		for (int i = 0; i < this.districts.size(); i++) {
			this.indVotes = this.indVotes + this.districts.get(i).getIndVotes();
		}
	}

	/**
	 * gets the total number of votes for a county
	 */
	private void calcTotalVotes() {
		this.totalVotes = 0;
		for (int i = 0; i < this.districts.size(); i++) {
			this.totalVotes = this.totalVotes + this.districts.get(i).getTotalVotes();
		}
	}

	/**
	 * @param a
	 *            District you want added to the County
	 */
	public void addDistrict(District newDistrict) {
		this.districts.add(newDistrict);
		getData();
	}

	/**
	 * @return the districts in a county
	 */
	public ArrayList<District> getDistricts() {
		return this.districts;
	}

	/**
	 * @return the name of the county
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @return the number of democratic votes
	 */
	public int getDemVotes() {
		return this.demVotes;
	}

	/**
	 * @return the number of republican votes
	 */
	public int getRepVotes() {
		return this.repVotes;
	}

	/**
	 * @return the number of independent votes
	 */
	public int getIndVotes() {
		return this.indVotes;
	}

	public int getTotalVotes() {
		return this.totalVotes;
	}

	/**
	 * @return the percent of votes that were democratic
	 */
	public double getDemPercent() {
		return (double) checkNum(this.demVotes, this.totalVotes);
	}

	/**
	 * @return the percent of votes that were republican
	 */
	public double getRepPercent() {
		return (double) checkNum(this.repVotes, this.totalVotes);
	}

	/**
	 * @return the percent of votes that were independent
	 */
	public double getIndPercent() {
		return (double) checkNum(this.indVotes, this.totalVotes);
	}

	@Override
	public ArrayList<Region> getSubregions() {
		return new ArrayList<>(getDistricts());
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
