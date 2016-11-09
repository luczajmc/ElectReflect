public abstract class Region {	
	/**
	 * @return the number of republican votes
	 */
	public abstract int getRepVotes();
	
	/**
	 * @return the number of democratic votes
	 */
	public abstract int getDemVotes();
	
	/**
	 * @return the number of independent votes
	 */
	public abstract int getIndVotes();
	
	/**
	 * @return the total number of votes
	 */
	public abstract int getTotalVotes();
	
	/**
	 * @return the percent of republican votes
	 */
	public abstract double getRepPercent();
	
	/**
	 * @return the percent of democratic votes
	 */
	public abstract double getDemPercent();
	
	/**
	 * @return the percent of independent votes
	 */
	public abstract double getIndPercent();

	/**
	 * @return the name of the region
	 */
	public abstract String getName();
}
