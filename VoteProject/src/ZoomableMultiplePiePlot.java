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
	double[] zoomPercentages;
	private int displayCols;
	
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
    	resetZoomPercentages(); // make sure the plot starts out zoomed correctly
    	
    	this.displayCols = (int) Math.ceil(Math.sqrt(this.getPieCount()));
    }
	
    void resetZoomPercentages() {
		int parties = unzoomedDataset.getRowCount();
		this.zoomPercentages = new double[parties];
		for (int i=0; i<this.zoomPercentages.length; i++) {
			this.zoomPercentages[i] = 1.0;
		}
    }
	public void zoomOut() {
		this.setDataset(unzoomedDataset);
		resetZoomPercentages();
	}
	
	void trimPies(double[] zoomPercentages) {
		assert(this.zoomPercentages.length == zoomPercentages.length);
        for (int i=0; i<this.zoomPercentages.length; i++) {
        	this.zoomPercentages[i] *= zoomPercentages[i];
        	System.out.print(this.zoomPercentages[i]+",");
        }
        System.out.println("===");
	}
	
    public void zoomSelection(double startAngle, double arcAngle) {
    	ZoomablePiePlot plot = (ZoomablePiePlot) this.getPieChart().getPlot();
    	double[] zoomPercentages = plot.getZoomPercentages(startAngle, arcAngle);
    	trimPies(zoomPercentages);
    	
    	plot.zoomSelection(startAngle, arcAngle);
    	
    }
    
    @Override
    public void setDataset(CategoryDataset dataset) {
    	// TODO: make sure this behaves appropriately
    	super.setDataset(dataset);
    	this.unzoomedDataset = dataset;
    	this.resetZoomPercentages();
    }
    
    /**
     * Draws the plot on a Java 2D graphics device (such as the screen or a
     * printer).
     *
     * @param g2  the graphics device.
     * @param area  the area within which the plot should be drawn.
     * @param anchor  the anchor point ({@code null} permitted).
     * @param parentState  the state from the parent plot, if there is one.
     * @param info  collects info about the drawing.
     */
    @Override
    public void draw(Graphics2D g2, Rectangle2D area, Point2D anchor,
            PlotState parentState, PlotRenderingInfo info) {

        // adjust the drawing area for the plot insets (if any)...
        RectangleInsets insets = getInsets();
        insets.trim(area);
        drawBackground(g2, area);
        drawOutline(g2, area);

        // check that there is some data to display...
        if (DatasetUtilities.isEmptyOrNull(this.getDataset())) {
            drawNoDataMessage(g2, area);
            return;
        }

        int pieCount = this.getPieCount();

        int displayCols = this.displayCols;
        int displayRows
            = (int) Math.ceil((double) pieCount / (double) displayCols);

        // swap rows and columns to match plotArea shape
        if (displayCols > displayRows && area.getWidth() < area.getHeight()) {
            int temp = displayCols;
            displayCols = displayRows;
            displayRows = temp;
        }

        int x = (int) area.getX();
        int y = (int) area.getY();
        int width = ((int) area.getWidth()) / displayCols;
        int height = ((int) area.getHeight()) / displayRows;
        int row = 0;
        int column = 0;
        int diff = (displayRows * displayCols) - pieCount;
        int xoffset = 0;
        Rectangle rect = new Rectangle();

        for (int pieIndex = 0; pieIndex < pieCount; pieIndex++) {
            rect.setBounds(x + xoffset + (width * column), y + (height * row),
                    width, height);

            drawPie(pieIndex, g2, rect, info);
            
            ++column;
            if (column == displayCols) {
                column = 0;
                ++row;

                if (row == displayRows - 1 && diff != 0) {
                    xoffset = (diff * width) / 2;
                }
            }
        }

    }

    void drawPie(int pieIndex, Graphics2D g2, Rectangle2D rect, PlotRenderingInfo info) {
    	// TODO: make the title truncate instead of rewrapping
        String title;
        if (this.getDataExtractOrder() == TableOrder.BY_ROW) {
            title = this.getDataset().getRowKey(pieIndex).toString();
        }
        else {
            title = this.getDataset().getColumnKey(pieIndex).toString();
        }

        TextTitle textTitle = new TextTitle(title) {
        	int getStringWidth(String s, Graphics2D g2) {
        		return g2.getFontMetrics(this.getFont()).stringWidth(s);
        	}
        	String trim(String s, int length) {
        		if (length<0 || s.length()<length) {
        			return "";
        		}
        		else {
        			return s.substring(0, length);
        		}
        	}
        	String trimToFit(String s, Graphics2D g2, Rectangle2D area) {
        		int spaceAvailable = (int) area.getWidth();
        		int spaceNeeded = this.getStringWidth(s, g2);
        		while (s != "" && spaceNeeded>spaceAvailable) {
        			s = trim(s, s.length()-1);
        			spaceNeeded = this.getStringWidth(s, g2);
        		}
        		return s;
        	}
    		@Override
    		public Object draw(Graphics2D g2, Rectangle2D area, Object params) {
    			int spaceNeeded = this.getStringWidth(this.getText(), g2);
    			int spaceAvailable = (int) area.getWidth();
    			if (spaceNeeded>spaceAvailable) {
        			System.out.println(String.format("%s: %d needed out of %d available",
        					this.getText(),
        					spaceNeeded,
        					spaceAvailable
        					));
    				
    			}
    			String title = trimToFit(this.getText(), g2, area);
    			if (title.length()<this.getText().length()) {
    				System.out.println(String.format("%s trimmed to %s",
    						this.getText(), title));
    			}
    			
    			this.setText(title);
    			this.setNotify(true);
    			
    			System.out.println("title was " + this.getText());
    			Object result = super.draw(g2, area, params);
    			System.out.println("title is now " + this.getText());
    			return result;
    		}
    	};
    	textTitle.setToolTipText(textTitle.getText());
    	
    	this.getPieChart().setTitle(textTitle);
    	
        PieDataset piedataset;
        PieDataset dd = new CategoryToPieDataset(this.unzoomedDataset,
                this.getDataExtractOrder(), pieIndex);
        if (this.getLimit() > 0.0) {
            piedataset = DatasetUtilities.createConsolidatedPieDataset(
                    dd, this.getAggregatedItemsKey(), this.getLimit());
        }
        else {
            piedataset = dd;
        }
        ZoomablePiePlot piePlot = (ZoomablePiePlot) this.getPieChart().getPlot();
        piePlot.setDataset(piedataset);
        piePlot.setPieIndex(pieIndex);
        piePlot.trimSlices(this.zoomPercentages);


        ChartRenderingInfo subinfo = null;
        if (info != null) {
            subinfo = new ChartRenderingInfo();
        }
        this.getPieChart().draw(g2, rect, subinfo);
        if (info != null) {
            assert subinfo != null;
            info.getOwner().getEntityCollection().addAll(
                    subinfo.getEntityCollection());
            info.addSubplotInfo(subinfo.getPlotInfo());
        }

    }
    
    public int getPieCount() {
        if (this.getDataExtractOrder() == TableOrder.BY_ROW) {
            return this.getDataset().getRowCount();
        }
        else {
            return this.getDataset().getColumnCount();
        }

    }
    
    public void setDisplayCols(int displayCols) {
    	this.displayCols = displayCols;
    }
    
    public int getDisplayCols() {
    	return this.displayCols;
    }
}
