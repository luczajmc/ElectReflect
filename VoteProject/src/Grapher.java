import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

public class Grapher {
	static void barGraph(Region region) {
		DefaultCategoryDataset data = new DefaultCategoryDataset();
		data.setValue(region.getRepVotes(), "Republican", region.getName());
		data.setValue(region.getDemVotes(), "Democrat", region.getName());
		data.setValue(region.getIndVotes(), "Independent", region.getName());
		
		JFreeChart chart = ChartFactory.createBarChart("Election Results",
				"", "", 
				data,
				PlotOrientation.VERTICAL,
				true, true, false);
		ChartFrame frame = new ChartFrame("Election Results", chart);
		frame.pack();
		frame.setVisible(true);

	}
	
	public static void barGraphState(State state) {
		barGraph(state);
	}
	
	public static void barGraphCounty(County county) {
		barGraph(county);
	}
	
	public static void barGraphDistrict(District district) {
		barGraph(district);
	}
	
	public static void pieChart(Region region) {
		DefaultPieDataset data = new DefaultPieDataset();
		data.setValue("Republican", region.getRepVotes());
		data.setValue("Democrat", region.getDemVotes());
		data.setValue("Independent", region.getIndVotes());
		
		JFreeChart chart = ChartFactory.createPieChart("Election Results", data,
				true, true, false);
		
		ChartFrame frame = new ChartFrame("Election Results", chart);
		frame.pack();
		frame.setVisible(true);
	}
	public static void pieChartState(State state) {
		pieChart(state);
	}
	
	public static void pieChartCounty(County county) {
		pieChart(county);
	}
	
	public static void pieChartDistrict(District district) {
		pieChart(district);
	}
	
	public static void text(Region region) {
		System.out.println(region.getName());
		System.out.println("\t"+"Republican: "+region.getRepPercent()*100+"% ("+region.getRepVotes()+")");
		System.out.println("\t"+"Democrat: "+region.getDemPercent()*100+"% ("+region.getDemVotes()+")");
		System.out.println("\t"+"Independent: "+region.getIndPercent()*100+"% ("+region.getIndVotes()+")");		
	}
	
	public static void textState(State state) {
		text(state);
	}
	
	public static void textCounty(County county) {
		text(county);
	}
	
	public static void textDistrict(District district) {
		text(district);
	}
}
