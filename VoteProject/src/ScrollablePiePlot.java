import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.plot.MultiplePiePlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.PlotState;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.CategoryToPieDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.RectangleInsets;
import org.jfree.util.TableOrder;

public class ScrollablePiePlot extends MultiplePiePlot {
	public Rectangle2D getPieArea(int pieNumber, Rectangle2D dataArea) {
        int displayCols = 1;
        int displayRows = getPieCount();

        int x = (int) dataArea.getX();
        int y = (int) dataArea.getY();
        int width = ((int) dataArea.getWidth()) / displayCols;
        int height = ((int) dataArea.getHeight()) / displayRows;
        int row = pieNumber;
        int column = 0;
        int diff = (displayRows * displayCols) - getPieCount();
        int xoffset = 0;
        Rectangle rect = new Rectangle();

        rect.setBounds(x + xoffset + (width * column), y + (height * row),
                width, height);

        return rect;
	}
	
	int getPieCount() {

        int pieCount;
        if (this.getDataExtractOrder() == TableOrder.BY_ROW) {
            pieCount = this.getDataset().getRowCount();
        }
        else {
            pieCount = this.getDataset().getColumnCount();
        }

        return pieCount;
	}
	
	public Rectangle2D getDataArea(Rectangle2D totalArea) {
        RectangleInsets insets = getInsets();
        insets.trim(totalArea);
        return totalArea;
	}
	
    @Override
    public void draw(Graphics2D g2, Rectangle2D area, Point2D anchor,
            PlotState parentState, PlotRenderingInfo info) {
    	// FIXME: I think the performance hit I'm taking when I'm scrolling
    	//		  is related to the fact that it has to redraw for each of
    	//		  those scroll steps
    	
        // adjust the drawing area for the plot insets (if any)...
    	area = getDataArea(area);
    	
        drawBackground(g2, area);
        drawOutline(g2, area);

        // check that there is some data to display...
        if (DatasetUtilities.isEmptyOrNull(this.getDataset())) {
            drawNoDataMessage(g2, area);
            return;
        }

        int pieCount = getPieCount();

        for (int pieIndex = 0; pieIndex < pieCount; pieIndex++) {
            Rectangle2D rect = getPieArea(pieIndex, area);
            
            String title;
            if (this.getDataExtractOrder() == TableOrder.BY_ROW) {
                title = this.getDataset().getRowKey(pieIndex).toString();
            }
            else {
                title = this.getDataset().getColumnKey(pieIndex).toString();
            }
            this.getPieChart().setTitle(title);

            PieDataset piedataset;
            PieDataset dd = new CategoryToPieDataset(this.getDataset(),
                    this.getDataExtractOrder(), pieIndex);
            if (this.getLimit() > 0.0) {
                piedataset = DatasetUtilities.createConsolidatedPieDataset(
                        dd, this.getAggregatedItemsKey(), this.getLimit());
            }
            else {
                piedataset = dd;
            }
            PiePlot piePlot = (PiePlot) this.getPieChart().getPlot();
            piePlot.setDataset(piedataset);
            piePlot.setPieIndex(pieIndex);

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

    }

    public ScrollablePiePlot(CategoryDataset dataset) {
    	super(dataset);
    }

}
