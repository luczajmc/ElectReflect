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
	
    public ZoomablePieChartPanel(JFreeChart chart) {
		super(chart);
		// TODO Auto-generated constructor stub
	}
    
    @Override
    public void paintComponent(Graphics g) {
		PlotRenderingInfo info = this.getChartRenderingInfo().getPlotInfo();
		PiePlot plot = (PiePlot) this.getChart().getPlot();
		Rectangle2D dataArea = info.getDataArea();

    	super.paintComponent(g);
    	fillArc(g, this.startAngle, this.endAngle-this.startAngle);
    }

    @Override
    public void mousePressed(MouseEvent e) {
    	this.startAngle = getAngle(e.getPoint());
    }
    @Override
    public void mouseDragged(MouseEvent e) {
    	double endAngle = getAngle(e.getPoint());
    	if (endAngle-this.startAngle > 0) {
    		endAngle -= 2*Math.PI;
    	}
    	
    	this.endAngle = endAngle;
    	
    	this.repaint();
    }
    @Override
    public void mouseReleased(MouseEvent e) {
    	this.endAngle = this.startAngle;
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
		
    	g.setXORMode(Color.gray);
		g.fillArc((int) arc.getMinX(), (int) arc.getMinY(), (int) arc.getWidth(), 
				(int) arc.getHeight(), (int) startAngleDegrees,
				(int) arcAngleDegrees);
		g.setPaintMode();
    }
}
