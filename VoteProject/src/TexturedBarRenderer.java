import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import org.jfree.chart.LegendItem;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRendererState;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.CategoryDataset;

public class TexturedBarRenderer extends BarRenderer {
	protected double barWidth = TexturePaintPatterner.DEFAULT_SWATCH_SIZE;

    /**
     * Initialises the renderer and returns a state object that will be passed
     * to subsequent calls to the drawItem method.  This method gets called
     * once at the start of the process of drawing a chart.
     *
     * @param g2  the graphics device.
     * @param dataArea  the area in which the data is to be plotted.
     * @param plot  the plot.
     * @param rendererIndex  the renderer index.
     * @param info  collects chart rendering information for return to caller.
     *
     * @return The renderer state.
     */
    @Override
    public CategoryItemRendererState initialise(Graphics2D g2, 
            Rectangle2D dataArea, CategoryPlot plot, int rendererIndex,
            PlotRenderingInfo info) {

    	CategoryItemRendererState state = super.initialise(g2, dataArea, plot,
    			rendererIndex, info);
    	
    	assert(state.getBarWidth() != 0);
        this.barWidth = state.getBarWidth();
        
        return state;

    }

    @Override
    public Paint getSeriesPaint(int series) {
    	TexturePaintPatterner patterner = this.getSeriesPatterner(series);
    	
    	assert(this.barWidth != 0);
    	return patterner.getTexturePaint((int) Math.ceil(this.barWidth));
    }
    
    public TexturePaintPatterner getSeriesPatterner(int series) {
    	TexturePaintPatterner[] patterners = TexturePaintPatternerFactory.getPatterners();
    	
    	TexturePaintPatterner patterner = patterners[series%patterners.length];
 
    	return patterner;
    }
    
    /**
     * Returns a legend item for a series.
     *
     * @param datasetIndex  the dataset index (zero-based).
     * @param series  the series index (zero-based).
     *
     * @return The legend item (possibly {@code null}).
     */
    @Override
    public LegendItem getLegendItem(int datasetIndex, int series) {
    	LegendItem item = super.getLegendItem(datasetIndex, series);
    	TexturePaintPatterner patterner = this.getSeriesPatterner(series);
    	
    	double height = item.getShape().getBounds().getHeight();  	
       	item.setFillPaint(patterner.getTexturePaint((int) height));
    	
        return item;
    }

}
