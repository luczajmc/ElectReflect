import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

public class Grapher {
	public static void barGraphState(State state) {
		
	}
	
	public static void barGraphCounty(County county) {
		
	}
	
	public static void barGraphDistrict(District district) {
		
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
