import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieToolTipGenerator;
import org.jfree.chart.plot.MultiplePiePlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.urls.StandardCategoryURLGenerator;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.TextAnchor;

public class Grapher {
	
    static SyncedCategoryPlot createPlot(String categoryAxisLabel,
    		String valueAxisLabel, CategoryDataset dataset) {

        CategoryAxis categoryAxis = new CategoryAxis(categoryAxisLabel);
        ValueAxis valueAxis = new NumberAxis(valueAxisLabel);

        TexturedBarRenderer renderer = new TexturedBarRenderer();
        
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
		
		SyncedCategoryPlot plot = createPlot("", "", categoryDataset(region));
		
        JFreeChart chart = new JFreeChart("Election Results", plot);
        
        new StandardChartTheme("JFree").apply(chart);
        
        // you have to do this after you apply the theme, because the theme
        // resets the BarPainter
        TexturedBarRenderer renderer = (TexturedBarRenderer) plot.getRenderer();
        renderer.setBarPainter(new StandardBarPainter());

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
		final int headerSize = 50;
		
		JSplitPane splitPane = new JSplitPane();

		ChartPanel sisterPanel = new DeadChartPanel(chart(region));
		JScrollPane sisterScrollPane = new JScrollPane(sisterPanel);
		sisterScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		JViewport axisPort = new JViewport();
		axisPort.add(sisterScrollPane);
		axisPort.setPreferredSize(new Dimension(ChartPanel.DEFAULT_WIDTH, headerSize));
		axisPort.setMinimumSize(axisPort.getPreferredSize());
		axisPort.setMaximumSize(axisPort.getPreferredSize());
		
		ChartPanel chartPanel = chartPanel(region);
		SyncedCategoryPlot plot = (SyncedCategoryPlot) chartPanel.getChart().getPlot();
		plot.setSisterPlot(sisterPanel.getChart().getCategoryPlot());
		
		JViewport port = new JViewport();
		port.add(chartPanel);
		
		JScrollPane scrollPane = new JScrollPane(port);
		int scrollBarSize = 30;
		scrollPane.setPreferredSize(new Dimension(ChartPanel.DEFAULT_WIDTH+scrollBarSize, ChartPanel.DEFAULT_HEIGHT-headerSize));
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
		
		scrollPane.getViewport().setViewPosition(new Point(0, headerSize));
		
		navigationPanel.add(scrollableList);
		navigationPanel.setMinimumSize(navigationPanel.getPreferredSize());
		navigationPanel.setMaximumSize(navigationPanel.getPreferredSize());

		splitPane.setRightComponent(navigationPanel);
	
		JFrame frame = new JFrame("Election Results");
		
		splitPane.setEnabled(false);
		splitPane.setDividerSize(0);

		frame.add(splitPane);
		frame.pack();
		
		clipFrame(frame);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double screenWidth = screenSize.getWidth();
		double frameWidth = frame.getPreferredSize().getWidth();
		frame.setLocation(new Point((int) (screenWidth-frameWidth), 0));
		frame.setVisible(true);
	}

	static void clipFrame(JFrame frame) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double screenWidth = screenSize.getWidth();
		double frameWidth = frame.getPreferredSize().getWidth();
		double screenHeight = screenSize.getHeight();
		double frameHeight = frame.getPreferredSize().getHeight();
		
		double width = Math.min(screenWidth, frameWidth);
		double height = Math.min(screenHeight, frameHeight);
		frame.setPreferredSize(new Dimension((int) width, (int) height));
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
		// TODO: show both subregions _and_ the name of the region
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
	
	static CategoryDataset categoryDataset(Region region) {
		DefaultCategoryDataset data = new DefaultCategoryDataset();
		ArrayList<Region> subregions = region.getSubregions();
		
		subregions.sort(RegionSorter.reverse(RegionSorter.byPopulation()));
		for (Region r : subregions) {
			data.setValue(r.getRepVotes(), "Republican", r.getName());
			data.setValue(r.getDemVotes(), "Democrat", r.getName());
			data.setValue(r.getIndVotes(), "Independent", r.getName());
			
		}

		return data;
	}
	
	public static void pieChart(Region region) {
		

		// if it's a multiple-pie plot, it has one plot for each subregion
		ZoomableMultiplePiePlot plot = new ZoomableMultiplePiePlot(
				categoryDataset(region));
		
		// do some setup the Factory would normally do
        plot.setInsets(new RectangleInsets(0.0, 5.0, 5.0, 5.0));

        // in a multiple-pie plot, the subplot gets tooltips instead
        PiePlot subPlot = (PiePlot) plot.getPieChart().getPlot();
        subPlot.setToolTipGenerator(new StandardPieToolTipGenerator());

//        // if it's a single-pie plot, it uses the total cumulative votes from
//        // across the whole region
//		DefaultPieDataset data = new DefaultPieDataset();
//		data.setValue("Republican", region.getRepVotes());
//		data.setValue("Democrat", region.getDemVotes());
//		data.setValue("Independent", region.getIndVotes());
//
//        // make a normal single-pie plot
//		ZoomablePiePlot plot = new ZoomablePiePlot(
//				data);

//        // do some setup the Factory would normally do
//        plot.setInsets(new RectangleInsets(0.0, 5.0, 5.0, 5.0));
//        plot.setToolTipGenerator(new StandardPieToolTipGenerator());

		JFreeChart chart = new JFreeChart("Election Results", plot);
	
		ZoomablePieChartPanel chartPanel = new ZoomablePieChartPanel(chart);

		// leave enough room in the chart panel for you to see the _whole_ chart
		int pieCount = plot.getPieCount();
		int scrollableHeight = ChartPanel.DEFAULT_HEIGHT*pieCount;
		chartPanel.setPreferredSize(new Dimension(ChartPanel.DEFAULT_WIDTH,
				scrollableHeight));

		// the chart should also tend to keep its original size when you resize
		// the window
		chartPanel.setMinimumSize(chartPanel.getPreferredSize());
		chartPanel.setMaximumSize(chartPanel.getPreferredSize());
		
		// one chart's worth of buffer seems adequate; also, it handles the case
		// where there are no subplots
		chartPanel.setMaximumDrawHeight(scrollableHeight+ChartPanel.DEFAULT_HEIGHT);

		// hopefully this is faster (no, it's slower)
		// TODO: figure out how the bar chart is able to do this reasonably
		JViewport port = new JViewport();
		port.setScrollMode(JViewport.BACKINGSTORE_SCROLL_MODE);

		port.add(chartPanel);
		
		// FIXME: this should make the viewport the same size as a regular chart
		JPanel panel = new JPanel();
		panel.add(port);
		panel.setPreferredSize(new Dimension(ChartPanel.DEFAULT_WIDTH,
				ChartPanel.DEFAULT_HEIGHT));
		panel.setMinimumSize(panel.getPreferredSize());
		panel.setMaximumSize(panel.getPreferredSize());
		
		// FIXME: this scroll pane scrolls abysmally slowly
		JScrollPane scrollPane = new JScrollPane(port);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		
		// just split the window into left and right halves; don't let the user adjust
		// the size of those halves
		JSplitPane splitPane = new JSplitPane();
		splitPane.setEnabled(false);
		splitPane.setDividerSize(0);

		
		// the chart goes on the left
		splitPane.setLeftComponent(scrollPane);
		
		JTextArea list = new JTextArea(regionList(region));
		list.setEditable(false);
		
		// when you resize the window, both these panes should tend to want to
		// keep their original height
		list.setMinimumSize(list.getPreferredSize());
		list.setMaximumSize(list.getPreferredSize());
		
		JViewport listPort = new JViewport();
		listPort.add(list);

		JScrollPane scrollableList = new JScrollPane(listPort);
		scrollableList.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		// scrollable list of counties goes on the right
		splitPane.setRightComponent(scrollableList);
		
		JFrame frame = new JFrame("Election Results");
		frame.add(splitPane);
		frame.pack();
		
		// frame can't be taller than the amount of space you have on your monitor
		clipFrame(frame);

		// frame goes at the lower right corner
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double screenWidth = screenSize.getWidth();
		double frameWidth = frame.getPreferredSize().getWidth();
		double screenHeight = screenSize.getHeight();
		double frameHeight = frame.getPreferredSize().getHeight();
		frame.setLocation(new Point((int) (screenWidth-frameWidth),
				(int) (screenHeight-frameHeight)));
		
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
		// TODO: add a save function
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
		
		// TODO: leave 450 pixels for the main GUI
		clipFrame(frame);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double screenHeight = screenSize.getHeight();
		double frameHeight = frame.getPreferredSize().getHeight();
		frame.setLocation(new Point(0,
				(int) (screenHeight-frameHeight)));
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
