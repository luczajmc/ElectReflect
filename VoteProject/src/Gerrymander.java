import java.util.ArrayList;
import java.util.Collection;

public class Gerrymander extends Region {
	private String name;
	private ArrayList<Region> regions = new ArrayList<Region>(); //holds all the regions
	private int demVotes;
	private int repVotes;
	private int indVotes;
	private int totalVotes;
	
	/**
	 * @param name, the name of the county
	 */
	public Gerrymander(String name) {
		this.name = name;
		demVotes = 0;
		repVotes = 0;
		indVotes = 0;
		totalVotes = 0;
	}
	
	public Gerrymander(String name, int repVotes, int demVotes, int indVotes) {
		this.name = name;
		this.repVotes = repVotes;
		this.demVotes = demVotes;
		this.indVotes = indVotes;
		this.totalVotes = repVotes+demVotes+indVotes;
		
	}

	public Gerrymander(Collection<Region> subregions) {
		for (Region r : subregions) {
			addRegion(r);
		}
	}

		
	
	/**
	 * @param a Region you want added as a subregion
	 */
	public void addRegion(Region r) {
		this.regions.add(r);
	}
	
	/**
	 * @return the subregions in a region
	 */
	public ArrayList<Region> getSubregions() {
		return this.regions;
	}

	/**
	 * @return the name of the region
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
		return (double)this.demVotes/this.totalVotes;
	}
	
	/**
	 * @return the percent of votes that were republican
	 */
	public double getRepPercent() {
		return (double)this.repVotes/this.totalVotes;
	}
	
	/**
	 * @return the percent of votes that were independent
	 */
	public double getIndPercent() {
		return (double)this.indVotes/this.totalVotes;
	}
	
}
