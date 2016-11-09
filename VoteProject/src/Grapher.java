import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

public class Grapher {
	public static void barGraphState(State state) {
		DefaultCategoryDataset data = new DefaultCategoryDataset();
		data.setValue(state.getRepVotes(), "Ohio", "Republican");
		data.setValue(state.getDemVotes(), "Ohio", "Democrat");
		data.setValue(state.getIndVotes(), "Ohio", "Independent");
		
		JFreeChart chart = ChartFactory.createBarChart("Election Results",
				"Party", "State", 
				data,
				PlotOrientation.VERTICAL,
				true, true, false);
		ChartFrame frame = new ChartFrame("Election Results", chart);
		frame.pack();
		frame.setVisible(true);
	}
	
	public static void barGraphCounty(County county) {
		DefaultCategoryDataset data = new DefaultCategoryDataset();
		data.setValue(county.getRepVotes(), county.getName(), "Republican");
		data.setValue(county.getDemVotes(), county.getName(), "Democrat");
		data.setValue(county.getIndVotes(), county.getName(), "Independent");
		
		JFreeChart chart = ChartFactory.createBarChart("Election Results",
				"Party", "State", 
				data,
				PlotOrientation.VERTICAL,
				true, true, false);
		ChartFrame frame = new ChartFrame("Election Results", chart);
		frame.pack();
		frame.setVisible(true);
		
	}
	
	public static void barGraphDistrict(District district) {
		DefaultCategoryDataset data = new DefaultCategoryDataset();
		data.setValue(district.getRepVotes(), "Republican", district.getName());
		data.setValue(district.getDemVotes(), "Democrat", district.getName());
		data.setValue(district.getIndVotes(), "Independent", district.getName());
		
		JFreeChart chart = ChartFactory.createBarChart("Election Results",
				"Party", "State", 
				data,
				PlotOrientation.VERTICAL,
				true, true, false);
		ChartFrame frame = new ChartFrame("Election Results", chart);
		frame.pack();
		frame.setVisible(true);
		
		
	}
	
	public static void pieChartState(State state) {
		DefaultPieDataset data = new DefaultPieDataset();
		data.setValue("Republican", state.getRepVotes());
		data.setValue("Democrat", state.getDemVotes());
		data.setValue("Independent", state.getIndVotes());
		
		JFreeChart chart = ChartFactory.createPieChart("Election Results", data,
				true, true, false);
		
		ChartFrame frame = new ChartFrame("Election Results", chart);
		frame.pack();
		frame.setVisible(true);
	}
	
	public static void pieChartCounty(County county) {
		DefaultPieDataset data = new DefaultPieDataset();
		data.setValue("Republican", county.getRepVotes());
		data.setValue("Democrat", county.getDemVotes());
		data.setValue("Independent", county.getIndVotes());
		
		JFreeChart chart = ChartFactory.createPieChart("Election Results", data,
				true, true, false);
		
		ChartFrame frame = new ChartFrame("Election Results", chart);
		frame.pack();
		frame.setVisible(true);
		
	}
	
	public static void pieChartDistrict(District district) {
		DefaultPieDataset data = new DefaultPieDataset();
		data.setValue("Republican", district.getRepVotes());
		data.setValue("Democrat", district.getDemVotes());
		data.setValue("Independent", district.getIndVotes());
		
		JFreeChart chart = ChartFactory.createPieChart("Election Results", data,
				true, true, false);
		
		ChartFrame frame = new ChartFrame("Election Results", chart);
		frame.pack();
		frame.setVisible(true);
		
	}
	
	public static void textState(State state) {
		
	}
	
	public static void textCounty(County county) {
		
	}
	
	public static void textDistrict(District district) {
		
	}
}
