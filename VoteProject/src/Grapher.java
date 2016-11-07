import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

public class Grapher {
	public static void barGraphState(State state) {
		DefaultCategoryDataset data = new DefaultCategoryDataset();
		data.setValue(state.getRepVotes(), "Republican", "Ohio");
		data.setValue(state.getDemVotes(), "Democrat", "Ohio");
		data.setValue(state.getIndVotes(), "Independent", "Ohio");
		
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
		data.setValue(county.getRepVotes(), "Republican", county.getName());
		data.setValue(county.getDemVotes(), "Democrat", county.getName());
		data.setValue(county.getIndVotes(), "Independent", county.getName());
		
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
