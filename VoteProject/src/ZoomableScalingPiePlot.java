import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

import org.jfree.chart.plot.PiePlotState;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.general.PieDataset;

public class ZoomableScalingPiePlot extends ZoomablePiePlot implements ScalingPie {
	
	public ZoomableScalingPiePlot(PieDataset dataset) {
		super(dataset);
		double total = DatasetUtilities.calculatePieDatasetTotal(this.getDataset());
		this.maxWindow = total;
		// TODO Auto-generated constructor stub
	}

	private final boolean DEBUG_DRAW_MAX = true;
	private final boolean DEBUG_DRAW_INTERIOR = true;
	private final boolean DEBUG_DRAW_LINK_AREA = true;
	private final boolean DEBUG_DRAW_PIE_AREA = true;
	private double maxWindow;
	private PiePlotState state;
	
	public PiePlotState getState() {
		return this.state;
	}
	
	private double getScaleFactor() {
		double total = DatasetUtilities.calculatePieDatasetTotal(this.getDataset());
		return total/this.maxWindow;
	}
	@Override
	protected void drawPie(Graphics2D g2, Rectangle2D plotArea,
			PlotRenderingInfo info) {

		PiePlotState state = initialise(g2, plotArea, this, null, info);

		// adjust the plot area for interior spacing and labels...
		double labelReserve = 0.0;
		if (this.getLabelGenerator() != null && !this.getSimpleLabels()) {
			labelReserve = this.getLabelGap() + this.getMaximumLabelWidth();
		}
		// "a double-sized plot would have half the gap" is false
		// a plot that represents the max number of voters should fit the area normally
		// a plot that represents half the max number of voters should cover half the area
		// pir^2=1/2pi(rmax^2) when r=rmax*sqrt(1/2)
		// rmax = plotArea-gapmax
		// 2r=2rmax-gap
		// 2r=dmax-gap
		// d=2r
		// dmax=2rmax
		// dmax=2rmax-2gapmax
		// r=rmax*sqrt(1/2) when gap=dmax-d
		// dmax=plotArea-gapmax
		// 
		double defaultGapHorizontal = plotArea.getWidth() * labelReserve * 2.0;
		double defaultGapVertical = plotArea.getHeight() * this.getInteriorGap() * 2.0;
		double hDiameterMax = plotArea.getWidth() - defaultGapHorizontal;
		double vDiameterMax = plotArea.getHeight() - defaultGapVertical;
		if (DEBUG_DRAW_MAX ) {
			double hGap = (plotArea.getWidth() - hDiameterMax)/2;
			double vGap = (plotArea.getHeight() - vDiameterMax)/2;

			double igx1 = plotArea.getX() + hGap;
			double igx2 = plotArea.getMaxX() - hGap;
			double igy1 = plotArea.getY() + vGap;
			double igy2 = plotArea.getMaxY() - vGap;
			g2.setPaint(Color.red);
			g2.draw(new Rectangle2D.Double(igx1, igy1, igx2 - igx1,
					igy2 - igy1));
		}

		// TODO: only do this when the pie is supposed to be circular
		double diameterMax = Math.min(hDiameterMax, vDiameterMax);
		double radiusMax = diameterMax/2;
		double radiusDesired = radiusMax*Math.sqrt(this.getScaleFactor());
		double hRadiusDesired = radiusDesired;
		double vRadiusDesired = radiusDesired;
		/*
		double hRadiusMax = hDiameterMax/2;
		double vRadiusMax = vDiameterMax/2;
		double hRadiusDesired = hRadiusMax*Math.sqrt(this.pieScaleFactor);
		double vRadiusDesired = vRadiusMax*Math.sqrt(this.pieScaleFactor);
		*/
		double hDiameterDesired = 2*hRadiusDesired;
		double vDiameterDesired = 2*vRadiusDesired;

		double hGapDifference = hDiameterMax-hDiameterDesired;
		double vGapDifference = vDiameterMax-vDiameterDesired;
		double gapHorizontal = defaultGapHorizontal+hGapDifference;
		double gapVertical = defaultGapVertical+vGapDifference;
		System.out.println(String.format("h: Wanted %.2f, so gap is %.2f+%.2f-%.2f=%.2f", hRadiusDesired, defaultGapHorizontal, hDiameterMax, hDiameterDesired, gapHorizontal));
		System.out.println(String.format("v: Wanted %.2f, so gap is %.2f+%.2f-%.2f=%.2f", vRadiusDesired, defaultGapVertical, vDiameterMax, vDiameterDesired, gapVertical));
		
		
		System.out.println(String.format("%.2f: >%.2f<, v%.2f^", this.getScaleFactor(), gapHorizontal, gapVertical));


		if (DEBUG_DRAW_INTERIOR ) {
			double hGap = plotArea.getWidth() * this.getInteriorGap();
			double vGap = plotArea.getHeight() * this.getInteriorGap();

			double igx1 = plotArea.getX() + hGap;
			double igx2 = plotArea.getMaxX() - hGap;
			double igy1 = plotArea.getY() + vGap;
			double igy2 = plotArea.getMaxY() - vGap;
			g2.setPaint(Color.gray);
			g2.draw(new Rectangle2D.Double(igx1, igy1, igx2 - igx1,
					igy2 - igy1));
		}

		double linkX = plotArea.getX() + gapHorizontal / 2;
		double linkY = plotArea.getY() + gapVertical / 2;
		double linkW = plotArea.getWidth() - gapHorizontal;
		double linkH = plotArea.getHeight() - gapVertical;
/*
		// make the link area a square if the pie chart is to be circular...
		if (this.isCircular()) {
			double min = Math.min(linkW, linkH) / 2;
			linkX = (linkX + linkX + linkW) / 2 - min;
			linkY = (linkY + linkY + linkH) / 2 - min;
			linkW = 2 * min;
			linkH = 2 * min;
		}
*/
		// the link area defines the dog leg points for the linking lines to
		// the labels
		Rectangle2D linkArea = new Rectangle2D.Double(linkX, linkY, linkW,
				linkH);
		state.setLinkArea(linkArea);

		if (DEBUG_DRAW_LINK_AREA ) {
			g2.setPaint(Color.blue);
			g2.draw(linkArea);
			g2.setPaint(Color.yellow);
			g2.draw(new Ellipse2D.Double(linkArea.getX(), linkArea.getY(),
					linkArea.getWidth(), linkArea.getHeight()));
		}

		// the explode area defines the max circle/ellipse for the exploded
		// pie sections.  it is defined by shrinking the linkArea by the
		// linkMargin factor.
		double lm = 0.0;
		if (!this.getSimpleLabels()) {
			lm = this.getLabelLinkMargin();
		}
		double hh = linkArea.getWidth() * lm * 2.0;
		double vv = linkArea.getHeight() * lm * 2.0;
		Rectangle2D explodeArea = new Rectangle2D.Double(linkX + hh / 2.0,
				linkY + vv / 2.0, linkW - hh, linkH - vv);

		state.setExplodedPieArea(explodeArea);

		// the pie area defines the circle/ellipse for regular pie sections.
		// it is defined by shrinking the explodeArea by the explodeMargin
		// factor.
		double maximumExplodePercent = getMaximumExplodePercent();
		double percent = maximumExplodePercent / (1.0 + maximumExplodePercent);

		double h1 = explodeArea.getWidth() * percent;
		double v1 = explodeArea.getHeight() * percent;
		Rectangle2D pieArea = new Rectangle2D.Double(explodeArea.getX()
				+ h1 / 2.0, explodeArea.getY() + v1 / 2.0,
				explodeArea.getWidth() - h1, explodeArea.getHeight() - v1);

		if (DEBUG_DRAW_PIE_AREA) {
			g2.setPaint(Color.green);
			g2.draw(pieArea);
		}
		state.setPieArea(pieArea);
		state.setPieCenterX(pieArea.getCenterX());
		state.setPieCenterY(pieArea.getCenterY());
		state.setPieWRadius(pieArea.getWidth() / 2.0);
		state.setPieHRadius(pieArea.getHeight() / 2.0);
		System.out.println(String.format("%.2fx%.2f", state.getPieWRadius(),
				state.getPieHRadius()));
		
		this.state = state;

		// plot the data (unless the dataset is null)...
		if ((this.getDataset() != null) && (this.getDataset().getKeys().size() > 0)) {

			List keys = this.getDataset().getKeys();
			double totalValue = DatasetUtilities.calculatePieDatasetTotal(
					this.getDataset());

			int passesRequired = state.getPassesRequired();
			for (int pass = 0; pass < passesRequired; pass++) {
				double runningTotal = 0.0;
				for (int section = 0; section < keys.size(); section++) {
					Number n = this.getDataset().getValue(section);
					if (n != null) {
						double value = n.doubleValue();
						if (value > 0.0) {
							runningTotal += value;
							drawItem(g2, section, explodeArea, state, pass);
						}
					}
				}
			}
			if (this.getSimpleLabels()) {
				drawSimpleLabels(g2, keys, totalValue, plotArea, linkArea,
						state);
			}
			else {
				drawLabels(g2, keys, totalValue, plotArea, linkArea, state);
			}

		}
		else {
			drawNoDataMessage(g2, plotArea);
		}
	}

	@Override
	public void setWindow(double maxWindow) {
		// TODO Auto-generated method stub
		this.maxWindow = maxWindow;
	}

	@Override
	public double getWindow() {
		// TODO Auto-generated method stub
		return this.maxWindow;
	}

}
