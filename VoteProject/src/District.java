
public class District {
	private String name;
	private int repVotes;
	private int demVotes;
	private int indVotes;
	
	public District(String name, int data, int data2, int data3) {
		this.name = name;
		this.repVotes = data;
		this.demVotes = data2;
		this.indVotes = data3;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getDemVotes() {
		return this.demVotes;
	}
	
	public int getRepVotes() {
		return this.repVotes;
	}
	
	public int getIndVotes() {
		return this.getIndVotes();
	}
}
