import java.util.ArrayList;

public class County {
	private String name;
	private ArrayList<District> districts = new ArrayList<District>();;
	private int demVotes;
	private int repVotes;
	private int indVotes;
	private int totalVotes;
	
	public County(String name) {
		this.name = name;
		demVotes = 0;
		repVotes = 0;
		indVotes = 0;
		totalVotes = 0;
		getData();
	}
	
	private void getData() {
		calcDemVotes();
		calcRepVotes();
		calcIndVotes();
		calcTotalVotes();
	}
	
	private void calcDemVotes() {
		for (int i = 0; i < this.districts.size(); i++) {
			this.demVotes = this.demVotes + this.districts.get(i).getDemVotes();
		}
	}
	
	private void calcRepVotes() {
		for (int i = 0; i < this.districts.size(); i++) {
			this.repVotes = this.repVotes + this.districts.get(i).getRepVotes();
		}
	}
	
	private void calcIndVotes() {
		for (int i = 0; i < this.districts.size(); i++) {
			this.indVotes = this.indVotes + this.districts.get(i).getIndVotes();
		}
	}
	
	private void calcTotalVotes() {
		this.totalVotes = this.demVotes + this.repVotes + this.indVotes;
	}
	
	public void addDistrict(District newDistrict) {
		this.districts.add(newDistrict);
		getData();
	}
	
	public ArrayList getDistricts() {
		return this.districts;
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
	
	public int getIdnVotes() {
		return this.indVotes;
	}
	
}
