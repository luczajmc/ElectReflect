import java.awt.Point;
import java.awt.geom.Rectangle2D;

import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.ui.RectangleEdge;


public class JumpList extends JList<Region> {
		Region[] regions;
		JScrollPane pane;
		ChartPanel chartPanel;
		
		static int maxVotes(Region region) {
			return region.getTotalVotes();
		}
		

		void jumpToCounty() {
			if (this.getSelectedValue() == null) {
				return;
			}
			
			SyncedCategoryPlot plot = (SyncedCategoryPlot) chartPanel.getChart().getCategoryPlot();
			CategoryAxis axis = plot.getDomainAxis();
			
			PlotRenderingInfo info = chartPanel.getChartRenderingInfo().getPlotInfo();
			Rectangle2D area = info.getDataArea();

			double offset = axis.getCategoryStart(this.getSelectedIndex(), this.regions.length,
					area, RectangleEdge.LEFT);
			Point destination = new Point(0, (int) offset);

			double maxVotesShown = maxVotes(this.getSelectedValue());
			double maxVotes = plot.getRangeAxis().getRange().getUpperBound();
			double scale = maxVotesShown/maxVotes;
			
			plot.zoomRangeAxes(0, scale, info, destination);
						
			pane.getViewport().setViewPosition(destination);

			

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
