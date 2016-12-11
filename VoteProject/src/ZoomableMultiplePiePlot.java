import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.List;

import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.event.PlotChangeEvent;
import org.jfree.chart.plot.MultiplePiePlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.PlotState;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.util.ParamChecks;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.CategoryToPieDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.util.Rotation;
import org.jfree.util.TableOrder;

public class ZoomableMultiplePiePlot extends MultiplePiePlot implements ZoomablePie {

	private CategoryDataset unzoomedDataset;
	
    /**
     * Creates a new plot with no data.
     */
    public ZoomableMultiplePiePlot() {
        this(null);
    }

    /**
     * Creates a new plot.
     *
     * @param dataset  the dataset ({@code null} permitted).
     */
    public ZoomableMultiplePiePlot(CategoryDataset dataset) {
        super(dataset);
        setDataset(dataset);
        ZoomablePiePlot piePlot = new ZoomablePiePlot(new DefaultPieDataset());
        piePlot.setIgnoreNullValues(true);
        JFreeChart pieChart = new JFreeChart(piePlot);
 
        pieChart.removeLegend();
        pieChart.setBackgroundPaint(null);

        TextTitle seriesTitle = new TextTitle("Series Title",
                new Font("SansSerif", Font.BOLD, 12));
        seriesTitle.setPosition(RectangleEdge.BOTTOM);
        pieChart.setTitle(seriesTitle);
        
        this.setPieChart(pieChart);

    	this.unzoomedDataset = dataset;
    }
	
	public void zoomOut() {
		this.setDataset(unzoomedDataset);
	}
	
	void trimPies(double[] zoomPercentages) {
		// TODO: this should update the tooltips on the pie charts also
		DefaultCategoryDataset zoomedDataset = new DefaultCategoryDataset();
		CategoryDataset dataset = this.getDataset();
		System.out.println(dataset.getRowCount()+","+dataset.getColumnCount());
		List regions = dataset.getColumnKeys();
		List parties = dataset.getRowKeys();
		for (int region=0; region<regions.size(); region++) {
			for (int party=0; party<parties.size(); party++) {
				Comparable partyName = dataset.getRowKey(party);
				Comparable regionName = dataset.getColumnKey(region);
				double zoom = zoomPercentages[party];
				double voteCount = dataset.getValue(party, region).doubleValue();
				System.out.println(String.format("%s, %s: %.0f*%.2f=%.0f", partyName.toString(), regionName.toString(), voteCount, zoom,
						zoom*voteCount));
				zoomedDataset.addValue(zoom*voteCount, partyName, regionName);
			}
		}

		super.setDataset(zoomedDataset);
	}
	
    public void zoomSelection(double startAngle, double arcAngle) {
    	ZoomablePiePlot plot = (ZoomablePiePlot) this.getPieChart().getPlot();
    	double[] zoomPercentages = plot.getZoomPercentages(startAngle, arcAngle);
    	trimPies(zoomPercentages);
    	
    	plot.zoomSelection(startAngle, arcAngle);
    	
    }
    
    @Override
    public void setDataset(CategoryDataset dataset) {
    	super.setDataset(dataset);
    	this.unzoomedDataset = dataset;
    }
    
}
