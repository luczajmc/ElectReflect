import java.io.File;
import javax.swing.JFileChooser;

public class Tester {

	public static void main(String[] args) {
		State Ohio = DataHandler.makeState();
		System.out.println(Ohio.getDemPercent());
		System.out.println(Ohio.getRepPercent());
		System.out.println(Ohio.getIndPercent());
		System.out.println(Ohio.getDemVotes());
		System.out.println(Ohio.getRepVotes());
		System.out.println(Ohio.getIndVotes());
		System.out.println(Ohio.getTotalVotes());
		County Adams = Ohio.selectCounty("Adams");
		System.out.println(Adams.getDemPercent());
		System.out.println(Adams.getRepPercent());
		System.out.println(Adams.getIndPercent());
		System.out.println(Adams.getDemVotes());
		System.out.println(Adams.getRepVotes());
		System.out.println(Adams.getIndVotes());
		System.out.println(Adams.getTotalVotes());
		District BrattonW2 = Adams.selectDistrict("BrattonW2");
		System.out.println(BrattonW2.getDemPercent());
		System.out.println(BrattonW2.getRepPercent());
		System.out.println(BrattonW2.getIndPercent());
		System.out.println(BrattonW2.getDemVotes());
		System.out.println(BrattonW2.getRepVotes());
		System.out.println(BrattonW2.getIndVotes());
		System.out.println(BrattonW2.getTotalVotes());
	}
}
