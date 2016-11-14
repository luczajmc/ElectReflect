import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

public class Grapher {
	static ChartPanel panel(JFreeChart chart) {
		// TODO: should adjust the width based on how many subregions you're graphing
	    return new ChartPanel(
	            chart,
	            1000, /** The default panel width. */
	            ChartPanel.DEFAULT_HEIGHT, /** The default panel height. */
	            ChartPanel.DEFAULT_MINIMUM_DRAW_WIDTH, /** The default limit below which chart scaling kicks in. */
	            ChartPanel.DEFAULT_MINIMUM_DRAW_HEIGHT, /** The default limit below which chart scaling kicks in. */
	            ChartPanel.DEFAULT_MAXIMUM_DRAW_WIDTH,
	            ChartPanel.DEFAULT_MAXIMUM_DRAW_HEIGHT,
	            ChartPanel.DEFAULT_BUFFER_USED,
	            true,  // properties
	            true,  // save
	            true,  // print
	            false,  // zoom
	            true   // tooltips
	        );
	}
	static int scrollBarSize() {
		return 30;
	}
	static void barGraph(Region region) {
		DefaultCategoryDataset data = new DefaultCategoryDataset();
		for (Region r : region.getSubregions()) {
			data.setValue(r.getRepVotes(), "Republican", r.getName());
			data.setValue(r.getDemVotes(), "Democrat", r.getName());
			data.setValue(r.getIndVotes(), "Independent", r.getName());
			
		}
		
		JFreeChart chart = ChartFactory.createBarChart("Election Results",
				"", "", 
				data,
				PlotOrientation.VERTICAL,
				true, true, false);
		
		JScrollPane scrollPane = new JScrollPane(panel(chart));
		scrollPane.setPreferredSize(new Dimension(ChartPanel.DEFAULT_WIDTH, ChartPanel.DEFAULT_HEIGHT+scrollBarSize()));
		JFrame frame = new JFrame("Election Results");
		frame.add(scrollPane);
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
