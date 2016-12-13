import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.Arc2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.swing.Scrollable;
import javax.swing.SwingConstants;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.MultiplePiePlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotRenderingInfo;

/**
 * A Swing GUI component for displaying a {@link JFreeChart} object.
 * <P>
 * The panel registers with the chart to receive notification of changes to any
 * component of the chart.  The chart is redrawn automatically whenever this
 * notification is received.
 */
public class ZoomablePieChartPanel extends ChartPanel {

	double startAngle = 0.0f;
	double endAngle = 0.0f;
	double arcAngle = 0.0f;
	Color highlightColor = Color.gray;
	int plotIndex = -1;
	
    public ZoomablePieChartPanel(JFreeChart chart) {
		super(chart);
		// TODO Auto-generated constructor stub
	}
    
    double clip(double angle) {
    	if (angle>0) {
    		angle = 0;
    	}
    	if (angle < -2*Math.PI) {
    		angle = -2*Math.PI;
    	}
   	
    	return angle;
    }
    
    Rectangle2D getCurrentDataArea() {
    	// TODO: this should also use instanceof, because -1 should be invalid
    	// 		 for a MultiplePiePlot: it means you don't know which subplot you're
    	//		 talking about
    	PlotRenderingInfo info = this.getChartRenderingInfo().getPlotInfo();

    	MultiplePiePlot plot = (MultiplePiePlot) this.getChart().getPlot();
//    	System.out.println(String.format("%d: %s", this.plotIndex, 
//    	    	plot.getPieChart().getTitle().getText()));
    	if (this.plotIndex==-1) {
    		return info.getDataArea();
    	}
    	else {
    		return info.getSubplotInfo(this.plotIndex).getDataArea();
    	}
    }
    
    // can be null
    ZoomablePiePlot getZoomablePiePlot() {
    	Plot plot = this.getChart().getPlot();
    	if (plot instanceof ZoomableMultiplePiePlot) {
    		// this already wouldn't work for regular MultiplePiePlots, so it's fine
    		// to be more restrictive
    		if (this.plotIndex==-1) {
    			return null;
    		}
    		ZoomableMultiplePiePlot multiplePlot = (ZoomableMultiplePiePlot) plot;
    		JFreeChart chart = multiplePlot.getPieChart(this.plotIndex);
    		return (ZoomablePiePlot) chart.getPlot();
    	}
    	else {
    		return (ZoomablePiePlot) plot;
    	}
    }
    int getSubplotIndex(Point p) {
    	PlotRenderingInfo info = this.getChartRenderingInfo().getPlotInfo();
    	
    	return info.getSubplotIndex(p);
    }
    
    void paintOverlap(Graphics g, Comparable key) {
        double arcAngle = this.arcAngle;
        arcAngle = clip(arcAngle);
        
        ZoomablePiePlot plot = this.getZoomablePiePlot();
        
        if (plot==null) {
        	return;
        }
        
        double[] sector = plot.getSlice(key);
        if (plot.wrapsAround(sector[0], sector[1], this.startAngle, arcAngle)) {
                double[] headOverlap = plot.headOverlapOnto(sector[0], sector[1], this.startAngle, arcAngle);
                fillArc(g, headOverlap[0], headOverlap[1]);
                double[] tailOverlap = plot.tailOverlapOnto(sector[0], sector[1], this.startAngle, arcAngle);
                fillArc(g, tailOverlap[0], tailOverlap[1]);
                double percentage = (headOverlap[1]/sector[1]+tailOverlap[1]/sector[1]);
                System.out.println(String.format("%s: %.2f ((%.2f+%.2f)/%.2f)", key, percentage,
                		headOverlap[1], tailOverlap[1], sector[1]));
        }
        else {
                double[] overlap = plot.overlapOnto(sector[0], sector[1], this.startAngle, arcAngle);
                fillArc(g, overlap[0], overlap[1]);
                double percentage = overlap[1]/sector[1];
                System.out.println(String.format("%s: %.2f (%.2f/%.2f)", key, percentage,
                		overlap[1], sector[1]));
                
        }

    }

    public void highlightCurrentDataArea(Graphics g) {
    	Rectangle2D dataArea = this.getCurrentDataArea();
    	
    	g.setXORMode(this.highlightColor);
    	g.drawRect((int) dataArea.getX(), (int) dataArea.getY(), 
    			(int) dataArea.getWidth(), (int) dataArea.getHeight());
		g.setPaintMode();
		

    }
    @Override
    public void paintComponent(Graphics g) {
    	super.paintComponent(g);
    	
        this.highlightColor = Color.red;
        paintOverlap(g, "Republican");
        
        this.highlightColor = Color.blue;
        paintOverlap(g, "Democrat");

        this.highlightColor = Color.green;
        paintOverlap(g, "Independent");
        
        this.highlightCurrentDataArea(g);
   }
    

    @Override
    public void mousePressed(MouseEvent e) {
    	super.mousePressed(e);
    	this.plotIndex = getSubplotIndex(e.getPoint());

    	this.startAngle = getAngle(e.getPoint());
    	this.endAngle = this.startAngle;
    	this.arcAngle = 0.0f;
    	
    }
    @Override
    public void mouseDragged(MouseEvent e) {
    	// FIXME: when you zoom in on a plot that doesn't have any independent votes,
    	//		  none of the other plots will show independent votes anymore;
    	//		  probably they should?
    	// TODO: this should probably not do anything if you're dragging from off of
    	//		 another component
    	double endAngle = getAngle(e.getPoint());
    	
    	boolean isClockwise = isClockwiseFrom(this.endAngle, endAngle); // you're going clockwise
    	
    	this.arcAngle += normalize(endAngle-this.endAngle);
    	this.endAngle = endAngle;    
    	
    	System.out.println(arcAngle);
    	this.repaint();
    }
    @Override
    public void mouseReleased(MouseEvent e) {
    	// FIXME: it's still possible to zoom so far in that your data goes away
    	ZoomablePie plot = (ZoomablePie) this.getChart().getPlot();
    	double arcAngle = this.arcAngle;
    	
    	double minimumSweep = -Math.PI/100; // clockwise
    	if (arcAngle>0) { // you dragged counterclockwise
    		plot.zoomOut();
    	}
    	else if (arcAngle < minimumSweep) {
    		// you dragged farther than minimumSweep clockwise
        	arcAngle = clip(arcAngle);
        	System.out.println(arcAngle);
        	
        	plot.zoomSelection(this.startAngle, arcAngle);
        	ZoomableMultiplePiePlot multiplePlot = (ZoomableMultiplePiePlot) plot;
        	System.out.println(multiplePlot.zoomPercentages);
            for (int i=0; i<multiplePlot.zoomPercentages.length; i++) {
            	System.out.print(multiplePlot.zoomPercentages[i]+",");
            }
            System.out.println("~~~");

    	}

    	this.endAngle = this.startAngle;
    	this.arcAngle = 0.0;

        this.getChart().setNotify(true);  // force the chart to redraw also
    	this.repaint();
    }
    
    double getAngle(Point2D endpoint) {
		Rectangle2D dataArea = getCurrentDataArea();

		double centerX = dataArea.getCenterX();
		double centerY = dataArea.getCenterY();
		
		double endX = endpoint.getX();
		double endY = endpoint.getY();
		
		double xOffset = endX-centerX;
		double yOffset = -(endY-centerY); // computer coordinates start at the top
		
		double angle = Math.atan2(yOffset, xOffset);
		
		return angle;

    }
    
    public void fillArc(Graphics g, Rectangle2D dataArea, double startAngle, double arcAngle) {

		double diameter = Math.min(dataArea.getWidth(), dataArea.getHeight());
		double radius = diameter/2;

		double centerX = dataArea.getCenterX();
		double centerY = dataArea.getCenterY();
		
		double pieOffsetX = centerX-radius;
		double pieOffsetY = centerY-radius;
		Rectangle2D pieArea = new Rectangle((int) (pieOffsetX), (int) (pieOffsetY),
				(int) diameter, (int) diameter);
				
		double startAngleDegrees = Math.toDegrees(startAngle);
		double arcAngleDegrees = Math.toDegrees(arcAngle);
		
		Arc2D.Double arc = new Arc2D.Double(pieArea, startAngleDegrees,
				arcAngleDegrees, Arc2D.PIE);
		
    	g.setXORMode(this.highlightColor);
		g.fillArc((int) arc.getMinX(), (int) arc.getMinY(), (int) arc.getWidth(), 
				(int) arc.getHeight(), (int) startAngleDegrees,
				(int) arcAngleDegrees);
		g.setPaintMode();
	
    }
    
	public void fillArc(Graphics g, double startAngle, double arcAngle) {
		Rectangle2D dataArea = getCurrentDataArea();
		
		fillArc(g, dataArea, startAngle, arcAngle);
    }
	
	double normalize(double angle) {
		while (angle < -Math.PI) {
			angle += 2*Math.PI;
		}
		while (angle > Math.PI) {
			angle -= 2*Math.PI;
		}
		return angle;
 	}
	
	boolean isClockwiseFrom(double startAngle, double endAngle) {
		if (normalize(endAngle-startAngle) <= 0) {
			return true;
		}
		else {
			return false;
		}
	}
	
	double arcAngleFrom(double startAngle, double endAngle) {
		double arcAngle = normalize(endAngle-startAngle);
		if (arcAngle > 0) {
			arcAngle -= 2*Math.PI;
		}
		return arcAngle;
	}

	public int getPlotIndex() {
		return this.plotIndex;
	}
	
	public void setPlotIndex(int plotIndex) {
		this.plotIndex = plotIndex;
		this.repaint();
	}
}
