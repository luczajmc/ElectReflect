import java.io.File;
import javax.swing.JFileChooser;

public class Tester {

	public static void main(String[] args) {
		State Ohio = new State();
		County Adams = Ohio.selectCounty("Adams");
		System.out.println(Adams.getDistricts().toString());
	}
}
