import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.Arc2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
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
    
    void paintOverlap(Graphics g, Comparable key) {
    	double arcAngle = this.arcAngle;
    	arcAngle = clip(arcAngle);
    	
    	ZoomablePiePlot plot = (ZoomablePiePlot) this.getChart().getPlot();
    	
    	double[] sector = plot.getSlice(key);
    	if (plot.wrapsAround(sector[0], sector[1], this.startAngle, arcAngle)) {
        	double[] headOverlap = plot.headOverlapOnto(sector[0], sector[1], this.startAngle, arcAngle);
        	fillArc(g, headOverlap[0], headOverlap[1]);
        	double[] tailOverlap = plot.tailOverlapOnto(sector[0], sector[1], this.startAngle, arcAngle);
        	fillArc(g, tailOverlap[0], tailOverlap[1]);
        	System.out.println(key+": "+(headOverlap[1]/sector[1]+tailOverlap[1]/sector[1]));
    	}
    	else {
        	double[] overlap = plot.overlapOnto(sector[0], sector[1], this.startAngle, arcAngle);
        	fillArc(g, overlap[0], overlap[1]);
        	System.out.println(key+": "+(overlap[1]/sector[1]));
    		
    	}

    }
    @Override
    public void paintComponent(Graphics g) {
		PlotRenderingInfo info = this.getChartRenderingInfo().getPlotInfo();
		ZoomablePiePlot plot = (ZoomablePiePlot) this.getChart().getPlot();
		Rectangle2D dataArea = info.getDataArea();

    	super.paintComponent(g);
    	this.highlightColor = Color.gray;
    	if (this.arcAngle<0) { // that is, if you're going a net clockwise
    		//fillArc(g, this.startAngle, this.arcAngle);
    	}
    	
    	this.highlightColor = Color.red;
    	paintOverlap(g, "Republican");
    	
    	this.highlightColor = Color.blue;
    	paintOverlap(g, "Democrat");

    	this.highlightColor = Color.green;
    	paintOverlap(g, "Independent");
   }

    @Override
    public void mousePressed(MouseEvent e) {
    	this.startAngle = getAngle(e.getPoint());
    	this.endAngle = this.startAngle;
    	this.arcAngle = 0.0f;
    }
    @Override
    public void mouseDragged(MouseEvent e) {
    	// TODO: this should probably not do anything if you're dragging from off of another component
    	double endAngle = getAngle(e.getPoint());
    	
    	boolean isClockwise = isClockwiseFrom(this.endAngle, endAngle); // you're going clockwise
    	
    	this.arcAngle += normalize(endAngle-this.endAngle);
    	this.endAngle = endAngle;    
    	
    	System.out.println(arcAngle);
    	this.repaint();
    }
    @Override
    public void mouseReleased(MouseEvent e) {
    	// TODO: don't zoom if you just click
    	// TODO: zoom out
    	double arcAngle = this.arcAngle;
    	arcAngle = clip(arcAngle);
    	System.out.println(arcAngle);
    	ZoomablePiePlot plot = (ZoomablePiePlot) this.getChart().getPlot();
    	
    	plot.zoomSelection(this.startAngle, arcAngle);
    	
    	this.endAngle = this.startAngle;
    	this.arcAngle = 0.0;

    	this.repaint();
    }
    
    double getAngle(Point2D endpoint) {
		PlotRenderingInfo info = this.getChartRenderingInfo().getPlotInfo();
		Rectangle2D dataArea = info.getDataArea();

		double centerX = dataArea.getCenterX();
		double centerY = dataArea.getCenterY();
		
		double endX = endpoint.getX();
		double endY = endpoint.getY();
		
		double xOffset = endX-centerX;
		double yOffset = -(endY-centerY); // computer coordinates start at the top
		
		double angle = Math.atan2(yOffset, xOffset);
		
		return angle;

    }
	public void fillArc(Graphics g, double startAngle, double arcAngle) {
		// TODO: this should never go counterclockwise
		PlotRenderingInfo info = this.getChartRenderingInfo().getPlotInfo();
		Rectangle2D dataArea = info.getDataArea();

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
}
