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
		System.out.println("Ohio");
		System.out.println("\t"+"Republican: "+state.getRepPercent()*100+"% ("+state.getRepVotes()+")");
		System.out.println("\t"+"Democrat: "+state.getDemPercent()*100+"% ("+state.getDemVotes()+")");
		System.out.println("\t"+"Independent: "+state.getIndPercent()*100+"% ("+state.getIndVotes()+")");
	}
	
	public static void textCounty(County county) {
		System.out.println(county.getName());
		System.out.println("\t"+"Republican: "+county.getRepPercent()*100+"% ("+county.getRepVotes()+")");
		System.out.println("\t"+"Democrat: "+county.getDemPercent()*100+"% ("+county.getDemVotes()+")");
		System.out.println("\t"+"Independent: "+county.getIndPercent()*100+"% ("+county.getIndVotes()+")");
		
	}
	
	public static void textDistrict(District district) {
		System.out.println(district.getName());
		System.out.println("\t"+"Republican: "+district.getRepPercent()*100+"% ("+district.getRepVotes()+")");
		System.out.println("\t"+"Democrat: "+district.getDemPercent()*100+"% ("+district.getDemVotes()+")");
		System.out.println("\t"+"Independent: "+district.getIndPercent()*100+"% ("+district.getIndVotes()+")");
	
	}
}
