import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JViewport;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.Vector;
import org.jfree.ui.RectangleEdge;

public class Grapher {
	static JFreeChart chart(Region region) {
		// TODO: label the axes
		// TODO: figure out how to handle the fact that some counties are _so_ much larger
		//		 than some others
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
	    return new ChartPanel(
	            chart(region),
	            ChartPanel.DEFAULT_WIDTH, /** The default panel width. */
	            ChartPanel.DEFAULT_HEIGHT+extraSpace(region), /** The default panel height. */
	            ChartPanel.DEFAULT_MINIMUM_DRAW_WIDTH, /** The default limit below which chart scaling kicks in. */
	            ChartPanel.DEFAULT_MINIMUM_DRAW_HEIGHT, /** The default limit below which chart scaling kicks in. */
	            ChartPanel.DEFAULT_MAXIMUM_DRAW_WIDTH,
	            ChartPanel.DEFAULT_MAXIMUM_DRAW_HEIGHT+extraSpace(region),
	            ChartPanel.DEFAULT_BUFFER_USED,
	            true,  // properties
	            true,  // save
	            true,  // print
	            false,  // zoom
	            true   // tooltips
	        );
	}
	
	static class JumpList extends JList<Region> {
		Region[] regions;
		JScrollPane pane;
		ChartPanel chartPanel;
		
		void jumpToCounty() {
			CategoryPlot plot = chartPanel.getChart().getCategoryPlot();
			CategoryAxis axis = plot.getDomainAxis();
			Rectangle2D area = chartPanel.getChartRenderingInfo().getPlotInfo().getDataArea();

			double offset = axis.getCategoryStart(this.getSelectedIndex(), this.regions.length,
					area, RectangleEdge.LEFT);
			pane.getViewport().setViewPosition(new Point(0, (int) offset));
		}
		
		JumpList(Region[] regions, JScrollPane pane, ChartPanel chartPanel) {
			super(regions);
			this.regions = regions;
			this.pane = pane;
			this.chartPanel = chartPanel;
			this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			this.addListSelectionListener(new ListSelectionListener() {
				
				public void valueChanged(ListSelectionEvent e) {
					jumpToCounty();
				}
			});
		}
		
		
	}
	
	static class SyncedChartPanel extends ChartPanel {
		ChartPanel sister;
		
		@Override
		public void mouseEntered(MouseEvent e) {
			super.mouseEntered(e);
			sister.mouseEntered(e);
		}
		
		@Override
		public void mouseExited(MouseEvent e) {
			super.mouseExited(e);
			sister.mouseExited(e);
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
			super.mousePressed(e);
			sister.mousePressed(e);
		}
		
		@Override
		public void mouseDragged(MouseEvent e) {
			super.mouseDragged(e);
			sister.mouseDragged(e);
		}
		
		@Override
		public void mouseReleased(MouseEvent e) {
			super.mouseReleased(e);
			sister.mouseReleased(e);
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			super.mouseClicked(e);
			sister.mouseClicked(e);
		}
		
		public SyncedChartPanel(JFreeChart chart, int width, int height, int minWidth,
				int minHeight, int maxWidth, int maxHeight, boolean defaultBuffer,
				boolean properties, boolean save, boolean print, boolean zoom,
				boolean tooltips) {
			super(chart, width, height, minWidth, minHeight, maxWidth, maxHeight,
					defaultBuffer, properties, save, print, zoom, tooltips, tooltips);
			
		}
		
		public void setSisterPanel(ChartPanel sister) {
			this.sister = sister;
		}
	}
	
	static SyncedChartPanel syncedChartPanel(Region region) {
		// TODO: see if you can't add padding / shrink the window to make sure the width of
		// 		 the plot is always the same size
		// FIXME: try to get rid of the extra padding that shows up in graphs with more bars
	    return new SyncedChartPanel(
	            chart(region),
	            ChartPanel.DEFAULT_WIDTH, /** The default panel width. */
	            ChartPanel.DEFAULT_HEIGHT+extraSpace(region), /** The default panel height. */
	            ChartPanel.DEFAULT_MINIMUM_DRAW_WIDTH, /** The default limit below which chart scaling kicks in. */
	            ChartPanel.DEFAULT_MINIMUM_DRAW_HEIGHT, /** The default limit below which chart scaling kicks in. */
	            ChartPanel.DEFAULT_MAXIMUM_DRAW_WIDTH,
	            ChartPanel.DEFAULT_MAXIMUM_DRAW_HEIGHT+extraSpace(region),
	            ChartPanel.DEFAULT_BUFFER_USED,
	            true,  // properties
	            true,  // save
	            true,  // print
	            false,  // zoom
	            true   // tooltips
	        );
	}

	
	static void barGraph(Region region) {
		// FIXME: I'm not sure if this works for Regions that don't have any subregions
		// FIXME: this still gets out of sync further down in the graph
		// TODO: make clone preserve your viewport in both scrollpanes
		// TODO: maybe we could show the range axis still as you're scrolling
		// TODO: maybe sync the horizontal scrolling of the two charts also
		JSplitPane splitPane = new JSplitPane();

		ChartPanel sisterPanel = chartPanel(region);
		JScrollPane sisterScrollPane = new JScrollPane(sisterPanel);
		sisterScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		JViewport axisPort = new JViewport();
		axisPort.add(sisterScrollPane);
		axisPort.setPreferredSize(new Dimension(ChartPanel.DEFAULT_WIDTH, 50));
		
		SyncedChartPanel chartPanel = syncedChartPanel(region);
		chartPanel.setSisterPanel(sisterPanel);

		JViewport port = new JViewport();
		port.add(chartPanel);

		JScrollPane scrollPane = new JScrollPane(port);
		int scrollBarSize = 30;
		scrollPane.setPreferredSize(new Dimension(ChartPanel.DEFAULT_WIDTH+scrollBarSize, ChartPanel.DEFAULT_HEIGHT+scrollBarSize));

		JSplitPane chartPane = new JSplitPane();
		chartPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		chartPane.setTopComponent(axisPort);
		chartPane.setBottomComponent(scrollPane);
		
		splitPane.setLeftComponent(chartPane);

		JPanel navigationPanel = new JPanel();
		navigationPanel.setLayout(new BoxLayout(navigationPanel, BoxLayout.PAGE_AXIS));
		
		Region[] subregions = new Region[region.getSubregions().size()];
		subregions = region.getSubregions().toArray(subregions);
		JumpList list = new JumpList(subregions, scrollPane, chartPanel);
		JScrollPane scrollableList = new JScrollPane(list);
		scrollableList.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		navigationPanel.add(scrollableList);
		
		JButton cloneButton = new JButton("Clone graph");
		cloneButton.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent arg0) {
				barGraph(region);
			}
			
		});
		navigationPanel.add(cloneButton);

		splitPane.setRightComponent(navigationPanel);
	
		JFrame frame = new JFrame("Election Results");
		frame.add(splitPane);
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
		// TODO: make sure to clearly differentiate the larger region from the subregions
		// TODO: maybe don't show a total if there's only one county?
		String displayText = "";
		
		for (Region subregion : region.getSubregions()) {
			displayText += describe(subregion);				
		}
		
		displayText += "\n";
		displayText += describe(region);
		
		JTextArea displayArea = new JTextArea(displayText);
		displayArea.setEditable(false);
		
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
