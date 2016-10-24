
public class District {
	private String name;
	private int repVotes;
	private int demVotes;
	private int indVotes;

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
		return this.demVotes / this.getTotalVotes();
	}
	
	/**
	 * @return the percentage of republican votes
	 */
	public double getRepPercent() {
		return this.repVotes / this.getTotalVotes();
	}
	
	/**
	 * @return the percentage of independent votes
	 */
	public double getIndPercent() {
		return this.indVotes / this.getTotalVotes();
	}
}
