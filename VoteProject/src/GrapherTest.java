import java.io.File;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JList;

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
		
		State ohio = DataHandler.makeState("../Data/");
		Grapher.barGraphState(ohio);
		Grapher.textState(ohio);
		Grapher.pieChartState(ohio);

		Region g = gerrymander();
		Grapher.barGraph(g);
		Grapher.text(g);
		Grapher.pieChart(g);
	}
}
