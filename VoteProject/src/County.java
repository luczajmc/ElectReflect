import java.util.ArrayList;

public class County {
	private String name;
	private ArrayList<District> districts;
	private int demVotes;
	private int repVotes;
	private int indVotes;
	
	public County(String name) {
		this.name = name;
	}
	
	public boolean addDistrict(District newDistrict) {
		districts.add(newDistrict);
		return false;
	}
}
