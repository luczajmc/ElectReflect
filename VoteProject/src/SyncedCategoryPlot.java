import java.awt.geom.Point2D;

import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;

public class SyncedCategoryPlot extends CategoryPlot {
	CategoryPlot sisterPlot;
	
	public SyncedCategoryPlot(CategoryDataset dataset, CategoryAxis categoryAxis, ValueAxis valueAxis,
			BarRenderer renderer) {
		super(dataset, categoryAxis, valueAxis, renderer);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void zoomRangeAxes(double lowerPercent, double upperPercent,
			PlotRenderingInfo state, Point2D source) {
		super.zoomRangeAxes(lowerPercent, upperPercent, state, source);
		
		if (sisterPlot != null) {
			sisterPlot.zoomRangeAxes(lowerPercent, upperPercent, state, source);
		}
	}
	
	@Override
	public void zoomRangeAxes(double factor, PlotRenderingInfo state, Point2D source) {
		super.zoomRangeAxes(factor, state, source);
		
		if (sisterPlot != null) {
			sisterPlot.zoomRangeAxes(factor, state, source);
		}
	}
	
	@Override
    public void zoomRangeAxes(double factor, PlotRenderingInfo info,
                              Point2D source, boolean useAnchor) {
		super.zoomRangeAxes(factor, info, source, useAnchor);
		
		if (sisterPlot != null) {
			sisterPlot.zoomRangeAxes(factor, info, source, useAnchor);
		}
	}
	
    public void setSisterPlot(CategoryPlot sisterPlot) {
		this.sisterPlot = sisterPlot;
	}


}
