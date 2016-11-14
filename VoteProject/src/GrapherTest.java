import java.io.File;

public class GrapherTest {
	static String name(int i) {
		return Integer.toString(i);
	}
	static Region region(int i) {
		return new County(name(i), 100+i, 90-i, 50+2*i);
	}
	static Region gerrymander() {
		Gerrymander g = new Gerrymander("Southwest Ohio");
		for (int i=0; i<20; i++) {
			g.addRegion(region(i));
		}
		return g;
	}
	public static void main(String[] args) {
		File f = new File("../Data/ExampleData.txt");
		System.out.println(f.getAbsolutePath());
		System.out.println(f.exists());

		
		State ohio = new State("../Data/ExampleData.txt");
		Grapher.barGraphState(ohio);
		
		if (true) { return;}
		State state = new State(20, 39, 48);
		Grapher.pieChartState(state);
		
		District district = new District("Oxford", 30, 28, 306);
		Grapher.pieChartDistrict(district);
		
		County county = new County("Butler", 38, 20, 94);
		Grapher.pieChartCounty(county);
		
		
		Grapher.barGraphState(state);
		
		Grapher.barGraphDistrict(district);
		Grapher.barGraphCounty(county);

		Grapher.textCounty(county);
		Grapher.textDistrict(district);
		Grapher.textState(state);
		
		Region g = gerrymander();
		Grapher.barGraph(g);
	}
}
