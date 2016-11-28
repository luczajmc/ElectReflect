import java.awt.Dimension;
import java.awt.Paint;
import java.awt.TexturePaint;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Comparator;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JViewport;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DefaultDrawingSupplier;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.urls.StandardCategoryURLGenerator;
import org.jfree.chart.util.ParamChecks;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.ui.TextAnchor;

public class Grapher {
	
    static SyncedCategoryPlot createPlot(String categoryAxisLabel,
    		String valueAxisLabel, CategoryDataset dataset) {

        CategoryAxis categoryAxis = new CategoryAxis(categoryAxisLabel);
        ValueAxis valueAxis = new NumberAxis(valueAxisLabel);

        BarRenderer renderer = new BarRenderer();
        
        ItemLabelPosition position1 = new ItemLabelPosition(
                ItemLabelAnchor.OUTSIDE3, TextAnchor.CENTER_LEFT);
        renderer.setBasePositiveItemLabelPosition(position1);
        ItemLabelPosition position2 = new ItemLabelPosition(
                ItemLabelAnchor.OUTSIDE9, TextAnchor.CENTER_RIGHT);
        renderer.setBaseNegativeItemLabelPosition(position2);
        
        renderer.setBaseToolTipGenerator(
                new StandardCategoryToolTipGenerator());
        renderer.setBaseItemURLGenerator(
                new StandardCategoryURLGenerator());

        SyncedCategoryPlot plot = new SyncedCategoryPlot(dataset, categoryAxis, valueAxis,
                renderer);
        plot.setOrientation(PlotOrientation.HORIZONTAL);
        
        return plot;
    }

	static JFreeChart chart(Region region) {
		// TODO: label the axes
		DefaultCategoryDataset data = new DefaultCategoryDataset();
		ArrayList<Region> subregions = region.getSubregions();
		
		subregions.sort(RegionSorter.reverse(RegionSorter.byPopulation()));
		for (Region r : subregions) {
			data.setValue(r.getRepVotes(), "Republican", r.getName());
			data.setValue(r.getDemVotes(), "Democrat", r.getName());
			data.setValue(r.getIndVotes(), "Independent", r.getName());
			
		}
		
		SyncedCategoryPlot plot = createPlot("", "", data);
		
        JFreeChart chart = new JFreeChart("Election Results", plot);
        
        new StandardChartTheme("JFree").apply(chart);
        return chart;
	}
	
	final static int maxCounties = 10;
	final static int padding = 20;
	// @return  approximately how much height, in pixels, the bars for one Region will
	// 			take up on screen
	static int barGroupHeight(int numCounties) {
		if (numCounties > maxCounties) {
			numCounties = maxCounties;
		}
		
		return (ChartPanel.DEFAULT_HEIGHT-padding)/numCounties;
	}
		
	// @return  approximately how much extra height the chart needs, in pixels, to
	//			display all the subregions without squishing them
	static int extraSpace(Region region) {
		int numCounties = region.getSubregions().size();
		int extraHeight = 0;
		
		if (numCounties>maxCounties) {
			extraHeight += (numCounties-maxCounties)*barGroupHeight(numCounties);
		}
		
		return extraHeight;
	}
	
	static ChartPanel chartPanel(Region region) {
		// TODO: see if you can't add padding / shrink the window to make sure the width of
		// 		 the plot is always the same size
		// FIXME: try to get rid of the extra padding that shows up in graphs with more bars
	    ChartPanel panel = new ChartPanel(chart(region));
	    panel.setMaximumDrawHeight(ChartPanel.DEFAULT_MAXIMUM_DRAW_HEIGHT+extraSpace(region));
	    panel.setPreferredSize(new Dimension(ChartPanel.DEFAULT_WIDTH, ChartPanel.DEFAULT_HEIGHT+extraSpace(region)));

	    return panel;
	}
	
	static void barGraph(Region region) {
		// FIXME: I'm not sure if this works for Regions that don't have any subregions
		// TODO: maybe sync the horizontal scrolling of the two charts also
		// FIXME: the plot should start at the same zoom as if you jump to the largest
		//		  county
		JSplitPane splitPane = new JSplitPane();

		ChartPanel sisterPanel = new DeadChartPanel(chart(region));
		JScrollPane sisterScrollPane = new JScrollPane(sisterPanel);
		sisterScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		JViewport axisPort = new JViewport();
		axisPort.add(sisterScrollPane);
		axisPort.setPreferredSize(new Dimension(ChartPanel.DEFAULT_WIDTH, 50));
		
		ChartPanel chartPanel = chartPanel(region);
		SyncedCategoryPlot plot = (SyncedCategoryPlot) chartPanel.getChart().getPlot();
		plot.setSisterPlot(sisterPanel.getChart().getCategoryPlot());
		
		Paint[] paints = TexturePaintMaker.getPaints();
		DefaultDrawingSupplier supplier = new DefaultDrawingSupplier(
				paints,
				DefaultDrawingSupplier.DEFAULT_OUTLINE_PAINT_SEQUENCE,
				DefaultDrawingSupplier.DEFAULT_STROKE_SEQUENCE,
				DefaultDrawingSupplier.DEFAULT_OUTLINE_STROKE_SEQUENCE,
				DefaultDrawingSupplier.DEFAULT_SHAPE_SEQUENCE);
		chartPanel.getChart().getCategoryPlot().setDrawingSupplier(supplier, true);

		CategoryItemRenderer renderer = chartPanel.getChart().getCategoryPlot().getRenderer();
		BarRenderer barRenderer = (BarRenderer) renderer;
		barRenderer.setBarPainter(new StandardBarPainter());

		JViewport port = new JViewport();
		port.add(chartPanel);
		
		JScrollPane scrollPane = new JScrollPane(port);
		int scrollBarSize = 30;
		scrollPane.setPreferredSize(new Dimension(ChartPanel.DEFAULT_WIDTH+scrollBarSize, ChartPanel.DEFAULT_HEIGHT+scrollBarSize));
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		JSplitPane chartPane = new JSplitPane();
		chartPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		chartPane.setTopComponent(axisPort);
		chartPane.setBottomComponent(scrollPane);
		chartPane.setEnabled(false);
		chartPane.setDividerSize(0);
		
		splitPane.setLeftComponent(chartPane);

		JPanel navigationPanel = new JPanel();
		navigationPanel.setLayout(new BoxLayout(navigationPanel, BoxLayout.PAGE_AXIS));
		
		Region[] subregions = new Region[region.getSubregions().size()];
		ArrayList<Region> subregionsList = region.getSubregions();
		subregionsList.sort(RegionSorter.reverse(RegionSorter.byPopulation()));
		subregions = subregionsList.toArray(subregions);
		JumpList list = new JumpList(subregions, scrollPane, chartPanel);
		JScrollPane scrollableList = new JScrollPane(list);
		scrollableList.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		navigationPanel.add(scrollableList);
		
		splitPane.setRightComponent(navigationPanel);
	
		JFrame frame = new JFrame("Election Results");
		frame.add(splitPane);
		frame.pack();
		
		frame.setLocationByPlatform(true);
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
	
	static String regionList(Region region) {
		String name = region.getName();
		
		if (name != null) {
			return name + "\n";
		}
		else {
			String list = "";
			for (Region subregion : region.getSubregions()) {
				list += subregion.getName() + "\n";
			}
			return list;
		}
		
	}
	
	public static void pieChart(Region region) {
		JSplitPane splitPane = new JSplitPane();

		DefaultPieDataset data = new DefaultPieDataset();
		data.setValue("Republican", region.getRepVotes());
		data.setValue("Democrat", region.getDemVotes());
		data.setValue("Independent", region.getIndVotes());
		
		JFreeChart chart = ChartFactory.createPieChart("Election Results", data,
				true, true, false);

		ChartPanel chartPanel = new ChartPanel(chart);
		splitPane.setLeftComponent(chartPanel);
		
		JTextArea list = new JTextArea(regionList(region));
		list.setEditable(false);
		JScrollPane scrollableList = new JScrollPane(list);
		scrollableList.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		splitPane.setRightComponent(scrollableList);
		
		JFrame frame = new JFrame("Election Results");
		frame.add(splitPane);
		frame.pack();
		
		frame.setLocationByPlatform(true);
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
		// TODO: maybe don't show a total if there's only one county?
		String displayText = "";
		
		for (Region subregion : region.getSubregions()) {
			displayText += describe(subregion);				
		}
		
		displayText += "===\n";
		displayText += describe(region);
		
		JTextArea displayArea = new JTextArea(displayText);
		displayArea.setEditable(false);
		
		JScrollPane scrollPane = new JScrollPane(displayArea);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		JFrame frame = new JFrame("Election Results");
		frame.add(scrollPane);
		frame.pack();
		
		frame.setLocationByPlatform(true);
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
