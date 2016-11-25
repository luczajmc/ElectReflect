import java.util.ArrayList;

public class District extends Region {
	private String name;
	private int repVotes;
	private int demVotes;
	private int indVotes;
	private int totalVotes;

	/**
	 * @param name
	 * @param republican
	 * @param democrat
	 * @param independent
	 */
	public District(String name, int republican, int democrat, int independent) {
		this.name = name;
		this.repVotes = republican;
		this.demVotes = democrat;
		this.indVotes = independent;
		this.totalVotes = getTotalVotes();
	}
	
	/**
	 * @return the name of the District
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
	
	/**
	 * @return the total number of votes
	 */
	public int getTotalVotes() { 
		return this.repVotes + this.demVotes + this.indVotes;
	}
	
	/**
	 * @return the percentage of democratic votes
	 */
	public double getDemPercent() {
		return (double) checkNum(this.demVotes, this.totalVotes);
	}
	
	/**
	 * @return the percentage of republican votes
	 */
	public double getRepPercent() {
		return (double) checkNum(this.repVotes, this.totalVotes);
	}
	
	/**
	 * @return the percentage of independent votes
	 */
	public double getIndPercent() {
		return (double) checkNum(this.indVotes, this.totalVotes);
	}

	@Override
	public ArrayList<Region> getSubregions() {
		return new ArrayList<>();
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
