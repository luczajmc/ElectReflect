import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Comparator;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class GrapherTest {
	static String name(int i) {
		return Integer.toString(i);
	}
	static Region region(int i) {
		return new County(name(i), 100+i, 90-i, 50+2*i);
	}
	static Region gerrymander() {
		Gerrymander g = new Gerrymander("Southwest Ohio");
		for (int i=0; i<1; i++) {
			g.addRegion(region(i));
		}
		return g;
	}
	
	static JFrame frame(String title) {
		JFrame frame = new JFrame(title);
		frame.setLocationByPlatform(true);
		frame.setVisible(true);
		
		return frame;
	}
	public static void main(String[] args) {

		JFrame frame = new JFrame("Sort");
		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.LINE_AXIS));

		State panOhio = new State("../Data/Ohio.csv");
		JList countyList = new JList(panOhio.getCounties().toArray());
		frame.add(countyList);
		
		SortMenu sortMenu = new SortMenu(RegionSorter.getOrderings().toArray(), countyList);
		frame.add(sortMenu);
		
		frame.pack();
		frame.setVisible(true);


		File f = new File("../Data/ExampleData.txt");
		System.out.println(f.getAbsolutePath());
		System.out.println(f.exists());

		
		State ohio = new State("../Data/ExampleData.txt");
		Grapher.barGraphState(ohio);
		Grapher.textState(ohio);
		Grapher.pieChartState(ohio);
		
		panOhio = new State("../Data/Ohio.csv");
		Grapher.barGraphState(panOhio);
		Grapher.textState(panOhio);
		Grapher.pieChartState(panOhio);
		
		State state = new State(20, 39, 48);
		Grapher.pieChartState(state);
		
		District district = new District("Oxford", 30, 28, 306);
		Grapher.pieChartDistrict(district);
		
		County county = new County("Butler", 38, 20, 94);
		Grapher.pieChartCounty(county);
		
		Region g = gerrymander();
		Grapher.barGraph(g);
		Grapher.text(g);
	}
}
