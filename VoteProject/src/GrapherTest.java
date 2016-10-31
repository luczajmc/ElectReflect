
public class GrapherTest {
	public static void main(String[] args) {
		State state = new State(20, 39, 48);
		Grapher.pieChartState(state);
		
		District district = new District("Oxford", 30, 28, 306);
		Grapher.pieChartDistrict(district);
		
		County county = new County("Butler", 38, 20, 94);
		Grapher.pieChartCounty(county);
	}
}
