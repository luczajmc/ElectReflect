import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

public class Grapher {
	static JFreeChart chart(Region region) {
		DefaultCategoryDataset data = new DefaultCategoryDataset();
		for (Region r : region.getSubregions()) {
			data.setValue(r.getRepVotes(), "Republican", r.getName());
			data.setValue(r.getDemVotes(), "Democrat", r.getName());
			data.setValue(r.getIndVotes(), "Independent", r.getName());
			
		}
		
		JFreeChart chart = ChartFactory.createBarChart("Election Results",
				"", "", 
				data,
				PlotOrientation.HORIZONTAL,
				true, true, false);
		
		return chart;
	}
	
	static ChartPanel chartPanel(Region region) {
		// TODO: see if you can't add padding / shrink the window to make sure the width of
		// 		 the plot is always the same size
		// TODO: make fontHeight actually correspond to the font in the graph,
		//		 and possibly this code should be smarter
		int fontHeight = 15;
		int numCounties = region.getSubregions().size();
		int extraHeight = 0;
		if (numCounties>25) {
			extraHeight += (numCounties-25)*fontHeight;
		}
		
	    return new ChartPanel(
	            chart(region),
	            ChartPanel.DEFAULT_WIDTH, /** The default panel width. */
	            ChartPanel.DEFAULT_HEIGHT+extraHeight, /** The default panel height. */
	            ChartPanel.DEFAULT_MINIMUM_DRAW_WIDTH, /** The default limit below which chart scaling kicks in. */
	            ChartPanel.DEFAULT_MINIMUM_DRAW_HEIGHT, /** The default limit below which chart scaling kicks in. */
	            ChartPanel.DEFAULT_MAXIMUM_DRAW_WIDTH,
	            ChartPanel.DEFAULT_MAXIMUM_DRAW_HEIGHT+extraHeight,
	            ChartPanel.DEFAULT_BUFFER_USED,
	            true,  // properties
	            true,  // save
	            true,  // print
	            false,  // zoom
	            true   // tooltips
	        );
	}
	
	static void barGraph(Region region) {
		int scrollBarSize = 30;
		JScrollPane scrollPane = new JScrollPane(chartPanel(region));
		scrollPane.setPreferredSize(new Dimension(ChartPanel.DEFAULT_WIDTH+scrollBarSize, ChartPanel.DEFAULT_HEIGHT+scrollBarSize));
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
		// TODO: this should have more labels
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
	
	private static String describe(Region r) {
		// TODO: finalize the text format
		String description = "";
		String name = r.getName();
		if (name != null) {
			description += name + "\n";
		}
		else {
			description += "total:" + "\n";
		}
		description += String.format("\tRepublican: %1$d (%2$.2f%%)\n", r.getRepVotes(), r.getRepPercent()*100);
		description += String.format("\tDemocrat: %1$d (%2$.2f%%)\n", r.getDemVotes(), r.getDemPercent()*100);
		description += String.format("\tIndependent: %1$d (%2$.2f%%)\n", r.getIndVotes(), r.getIndPercent()*100);
		description += String.format("\tTotal: %1$d\n", r.getTotalVotes());
		return description;
	}
	
	public static void text(Region region) {
		// TODO: make TextArea non-editable
		// TODO: make sure to clearly differentiate the larger region from the subregions
		// TODO: maybe don't show a total if there's only one county?
		String displayText = "";
		
		for (Region subregion : region.getSubregions()) {
			displayText += describe(subregion);				
		}
		
		displayText += "\n";
		displayText += describe(region);
		
		JTextArea displayArea = new JTextArea(displayText);
		JScrollPane scrollPane = new JScrollPane(displayArea);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		JFrame frame = new JFrame("Election Results");
		frame.add(scrollPane);
		frame.pack();
		frame.setVisible(true);
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
