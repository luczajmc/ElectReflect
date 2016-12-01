/* ===========================================================
 * JFreeChart : a free chart library for the Java(tm) platform
 * ===========================================================
 *
 * (C) Copyright 2000-2016, by Object Refinery Limited and Contributors.
 *
 * Project Info:  http://www.jfree.org/jfreechart/index.html
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Oracle and Java are registered trademarks of Oracle and/or its affiliates. 
 * Other names may be trademarks of their respective owners.]
 *
 * ------------
 * PiePlot.java
 * ------------
 * (C) Copyright 2000-2016, by Andrzej Porebski and Contributors.
 *
 * Original Author:  Andrzej Porebski;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *                   Martin Cordova (percentages in labels);
 *                   Richard Atkinson (URL support for image maps);
 *                   Christian W. Zuckschwerdt;
 *                   Arnaud Lelievre;
 *                   Martin Hilpert (patch 1891849);
 *                   Andreas Schroeder (very minor);
 *                   Christoph Beck (bug 2121818);
 *
 * Changes
 * -------
 * 21-Jun-2001 : Removed redundant JFreeChart parameter from constructors (DG);
 * 18-Sep-2001 : Updated header (DG);
 * 15-Oct-2001 : Data source classes moved to com.jrefinery.data.* (DG);
 * 19-Oct-2001 : Moved series paint and stroke methods from JFreeChart.java to
 *               Plot.java (DG);
 * 22-Oct-2001 : Renamed DataSource.java --> Dataset.java etc. (DG);
 * 13-Nov-2001 : Modified plot subclasses so that null axes are possible for
 *               pie plot (DG);
 * 17-Nov-2001 : Added PieDataset interface and amended this class accordingly,
 *               and completed removal of BlankAxis class as it is no longer
 *               required (DG);
 * 19-Nov-2001 : Changed 'drawCircle' property to 'circular' property (DG);
 * 21-Nov-2001 : Added options for exploding pie sections and filled out range
 *               of properties (DG);
 *               Added option for percentages in chart labels, based on code
 *               by Martin Cordova (DG);
 * 30-Nov-2001 : Changed default font from "Arial" --> "SansSerif" (DG);
 * 12-Dec-2001 : Removed unnecessary 'throws' clause in constructor (DG);
 * 13-Dec-2001 : Added tooltips (DG);
 * 16-Jan-2002 : Renamed tooltips class (DG);
 * 22-Jan-2002 : Fixed bug correlating legend labels with pie data (DG);
 * 05-Feb-2002 : Added alpha-transparency to plot class, and updated
 *               constructors accordingly (DG);
 * 06-Feb-2002 : Added optional background image and alpha-transparency to Plot
 *               and subclasses.  Clipped drawing within plot area (DG);
 * 26-Mar-2002 : Added an empty zoom method (DG);
 * 18-Apr-2002 : PieDataset is no longer sorted (oldman);
 * 23-Apr-2002 : Moved dataset from JFreeChart to Plot.  Added
 *               getLegendItemLabels() method (DG);
 * 19-Jun-2002 : Added attributes to control starting angle and direction
 *               (default is now clockwise) (DG);
 * 25-Jun-2002 : Removed redundant imports (DG);
 * 02-Jul-2002 : Fixed sign of percentage bug introduced in 0.9.2 (DG);
 * 16-Jul-2002 : Added check for null dataset in getLegendItemLabels() (DG);
 * 30-Jul-2002 : Moved summation code to DatasetUtilities (DG);
 * 05-Aug-2002 : Added URL support for image maps - new member variable for
 *               urlGenerator, modified constructor and minor change to the
 *               draw method (RA);
 * 18-Sep-2002 : Modified the percent label creation and added setters for the
 *               formatters (AS);
 * 24-Sep-2002 : Added getLegendItems() method (DG);
 * 02-Oct-2002 : Fixed errors reported by Checkstyle (DG);
 * 09-Oct-2002 : Added check for null entity collection (DG);
 * 30-Oct-2002 : Changed PieDataset interface (DG);
 * 18-Nov-2002 : Changed CategoryDataset to TableDataset (DG);
 * 02-Jan-2003 : Fixed "no data" message (DG);
 * 23-Jan-2003 : Modified to extract data from rows OR columns in
 *               CategoryDataset (DG);
 * 14-Feb-2003 : Fixed label drawing so that foreground alpha does not apply
 *               (bug id 685536) (DG);
 * 07-Mar-2003 : Modified to pass pieIndex on to PieSectionEntity and tooltip
 *               and URL generators (DG);
 * 21-Mar-2003 : Added a minimum angle for drawing arcs
 *               (see bug id 620031) (DG);
 * 24-Apr-2003 : Switched around PieDataset and KeyedValuesDataset (DG);
 * 02-Jun-2003 : Fixed bug 721733 (DG);
 * 30-Jul-2003 : Modified entity constructor (CZ);
 * 19-Aug-2003 : Implemented Cloneable (DG);
 * 29-Aug-2003 : Fixed bug 796936 (null pointer on setOutlinePaint()) (DG);
 * 08-Sep-2003 : Added internationalization via use of properties
 *               resourceBundle (RFE 690236) (AL);
 * 16-Sep-2003 : Changed ChartRenderingInfo --> PlotRenderingInfo (DG);
 * 29-Oct-2003 : Added workaround for font alignment in PDF output (DG);
 * 05-Nov-2003 : Fixed missing legend bug (DG);
 * 10-Nov-2003 : Re-added the DatasetChangeListener to constructors (CZ);
 * 29-Jan-2004 : Fixed clipping bug in draw() method (DG);
 * 11-Mar-2004 : Major overhaul to improve labelling (DG);
 * 31-Mar-2004 : Made an adjustment for the plot area when the label generator
 *               is null.  Fixed null pointer exception when the label
 *               generator returns null for a label (DG);
 * 06-Apr-2004 : Added getter, setter, serialization and draw support for
 *               labelBackgroundPaint (AS);
 * 08-Apr-2004 : Added flag to control whether null values are ignored or
 *               not (DG);
 * 15-Apr-2004 : Fixed some minor warnings from Eclipse (DG);
 * 26-Apr-2004 : Added attributes for label outline and shadow (DG);
 * 04-Oct-2004 : Renamed ShapeUtils --> ShapeUtilities (DG);
 * 04-Nov-2004 : Fixed null pointer exception with new LegendTitle class (DG);
 * 09-Nov-2004 : Added user definable legend item shape (DG);
 * 25-Nov-2004 : Added new legend label generator (DG);
 * 20-Apr-2005 : Added a tool tip generator for legend labels (DG);
 * 26-Apr-2005 : Removed LOGGER (DG);
 * 05-May-2005 : Updated draw() method parameters (DG);
 * 10-May-2005 : Added flag to control visibility of label linking lines, plus
 *               another flag to control the handling of zero values (DG);
 * 08-Jun-2005 : Fixed bug in getLegendItems() method (not respecting flags
 *               for ignoring null and zero values), and fixed equals() method
 *               to handle GradientPaint (DG);
 * 15-Jul-2005 : Added sectionOutlinesVisible attribute (DG);
 * ------------- JFREECHART 1.0.x ---------------------------------------------
 * 09-Jan-2006 : Fixed bug 1400442, inconsistent treatment of null and zero
 *               values in dataset (DG);
 * 28-Feb-2006 : Fixed bug 1440415, bad distribution of pie section
 *               labels (DG);
 * 27-Sep-2006 : Initialised baseSectionPaint correctly, added lookup methods
 *               for section paint, outline paint and outline stroke (DG);
 * 27-Sep-2006 : Refactored paint and stroke methods to use keys rather than
 *               section indices (DG);
 * 03-Oct-2006 : Replaced call to JRE 1.5 method (DG);
 * 23-Nov-2006 : Added support for URLs for the legend items (DG);
 * 24-Nov-2006 : Cloning fixes (DG);
 * 17-Apr-2007 : Check for null label in legend items (DG);
 * 19-Apr-2007 : Deprecated override settings (DG);
 * 18-May-2007 : Set dataset for LegendItem (DG);
 * 14-Jun-2007 : Added label distributor attribute (DG);
 * 18-Jul-2007 : Added simple label option (DG);
 * 21-Nov-2007 : Fixed labelling bugs, added debug code, restored default
 *               white background (DG);
 * 19-Mar-2008 : Fixed IllegalArgumentException when drawing with null
 *               dataset (DG);
 * 31-Mar-2008 : Adjust the label area for the interiorGap (DG);
 * 31-Mar-2008 : Added quad and cubic curve label link lines - see patch
 *               1891849 by Martin Hilpert (DG);
 * 02-Jul-2008 : Added autoPopulate flags (DG);
 * 15-Aug-2008 : Added methods to clear section attributes (DG);
 * 15-Aug-2008 : Fixed bug 2051168 - problem with LegendItemEntity
 *               generation (DG);
 * 23-Sep-2008 : Added getLabelLinkDepth() method - see bug 2121818 reported
 *               by Christoph Beck (DG);
 * 18-Dec-2008 : Use ResourceBundleWrapper - see patch 1607918 by
 *               Jess Thrysoee (DG);
 * 10-Jul-2009 : Added optional drop shadow generator (DG);
 * 03-Sep-2009 : Fixed bug where sinmpleLabelOffset is ignored (DG);
 * 04-Nov-2009 : Add mouse wheel rotation support (DG);
 * 18-Oct-2011 : Fixed tooltip offset with shadow generator (DG);
 * 20-Nov-2011 : Initialise shadow generator as null (DG);
 * 01-Jul-2012 : General label once only in drawSimpleLabels() (DG);
 * 02-Jul-2013 : Use ParamChecks (DG);
 * 12-Sep-2013 : Check for KEY_SUPPRESS_SHADOW_GENERATION rendering hint (DG);
 * 
 */

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RadialGradientPaint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Arc2D;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;
import org.jfree.chart.JFreeChart;

import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.PaintMap;
import org.jfree.chart.StrokeMap;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.entity.PieSectionEntity;
import org.jfree.chart.event.PlotChangeEvent;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.PieToolTipGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PiePlotState;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.PlotState;
import org.jfree.chart.urls.PieURLGenerator;
import org.jfree.chart.util.ParamChecks;
import org.jfree.chart.util.ResourceBundleWrapper;
import org.jfree.chart.util.ShadowGenerator;
import org.jfree.data.DefaultKeyedValues;
import org.jfree.data.KeyedValues;
import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.io.SerialUtilities;
import org.jfree.text.G2TextMeasurer;
import org.jfree.text.TextBlock;
import org.jfree.text.TextBox;
import org.jfree.text.TextUtilities;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.TextAnchor;
import org.jfree.util.ObjectUtilities;
import org.jfree.util.PaintUtilities;
import org.jfree.util.PublicCloneable;
import org.jfree.util.Rotation;
import org.jfree.util.ShapeUtilities;
import org.jfree.util.UnitType;

public class ZoomablePiePlot extends PiePlot implements Cloneable, Serializable {

    public void trimSlices(double[] remainingPercentages) {
    	PieDataset dataset = this.getDataset();
    	DefaultPieDataset zoomedDataset = new DefaultPieDataset(dataset);
    	
    	for (int i=0; i<remainingPercentages.length; i++) {
    		Comparable key = dataset.getKey(i);
    		Number value = dataset.getValue(key);
    		double doubleValue = value.doubleValue();
    		doubleValue *= remainingPercentages[i];
    		zoomedDataset.setValue(key, doubleValue);
    	}
    	
    	this.setDataset(zoomedDataset);
    }
    
    public void trimSlice(Comparable key, double remainingPercentage) {
    	PieDataset dataset = this.getDataset();
    	DefaultPieDataset zoomedDataset = new DefaultPieDataset(dataset);

		Number value = dataset.getValue(key);
		double doubleValue = value.doubleValue();
		doubleValue *= remainingPercentage;
		zoomedDataset.setValue(key, doubleValue);
    	
	   	this.setDataset(zoomedDataset);

    }

    double[] getZoomPercentages(double startAngle, double arcAngle) {
    	double total = DatasetUtilities.calculatePieDatasetTotal(this.getDataset());
    	
    	double pieStart = this.getStartAngle();
    	double pieEnd;
    	
    	// this.direction must be CLOCKWISE at the current time
        if (this.getDirection() != Rotation.CLOCKWISE) {
            throw new IllegalStateException("Rotation type not supported.");
        }
        
        PieDataset dataset = this.getDataset();
        
        double[] zoomPercentages = new double[dataset.getKeys().size()];
        System.out.println("===");
        for (int i=0; i<dataset.getKeys().size(); i++) {
        	double value = dataset.getValue(i).doubleValue();
        	pieEnd = pieStart - value / total * 360.0;
        	
        	double pieStartRadians = Math.toRadians(pieStart);
        	double pieEndRadians = Math.toRadians(pieEnd);
        	double pieArc = arcAngleFrom(pieStartRadians, pieEndRadians);
        	
        	Comparable key = dataset.getKey(i);
        	double percentage;
        	if (wrapsAround(pieStartRadians, pieArc, startAngle, arcAngle)) {
            	double[] headOverlap = headOverlapOnto(pieStartRadians, pieArc, startAngle, arcAngle);
            	double[] tailOverlap = tailOverlapOnto(pieStartRadians, pieArc, startAngle, arcAngle);
            	percentage = (headOverlap[1]/pieArc+tailOverlap[1]/pieArc);
            	System.out.println(key+": "+percentage);
        	}
        	else {
            	double[] overlap = overlapOnto(pieStartRadians, pieArc, startAngle, arcAngle);
            	percentage = (overlap[1]/pieArc);
            	System.out.println(key+": "+percentage);
        		
        	}
        	zoomPercentages[i] = percentage;
        	pieStart = pieEnd;
        }

        return zoomPercentages;
    }
    public void zoomSelection(double startAngle, double arcAngle) {
    	trimSlices(getZoomPercentages(startAngle, arcAngle));
    }
    
	
	double arcAngleFrom(double startAngle, double endAngle) {
		double arcAngle = normalize(endAngle-startAngle);
		if (arcAngle > 0) {
			arcAngle -= 2*Math.PI;
		}
		return arcAngle;
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

	boolean contains(double angle, double startAngle, double arcAngle) {
		// all arc angles are counterclockwise, so this returns true if you come to
		// angle within arcAngle of the start angle
		return arcAngleFrom(startAngle, angle) >= arcAngle;
	}
	
	double[] getSlice(Comparable key) {
    	double total = DatasetUtilities.calculatePieDatasetTotal(this.getDataset());
    	
    	double pieStart = this.getStartAngle();
    	double pieEnd;
    	
    	// this.direction must be CLOCKWISE at the current time
        if (this.getDirection() != Rotation.CLOCKWISE) {
            throw new IllegalStateException("Rotation type not supported.");
        }
        
        PieDataset dataset = this.getDataset();
        for (int i=0; i<dataset.getKeys().size(); i++) {
        	double value = dataset.getValue(i).doubleValue();
        	pieEnd = pieStart - value / total * 360.0;
        	
        	if (key==dataset.getKey(i)) {
            	double pieStartRadians = Math.toRadians(pieStart);
            	double pieEndRadians = Math.toRadians(pieEnd);
            	double pieArc = arcAngleFrom(pieStartRadians, pieEndRadians);

        		double[] arc = {pieStartRadians, pieArc};
        		return arc;
        	}
        	pieStart = pieEnd;
        }
		return null;
	}
	
	boolean overlaps(double pieStart, double pieArc, double sweepStart, double sweepArc) {
		double pieEnd = pieStart+pieArc;
		double sweepEnd = sweepStart+sweepArc;

		return contains(pieStart, sweepStart, sweepArc) ||
				contains(pieEnd, sweepStart, sweepArc) ||
				contains(sweepStart, pieStart, pieArc) ||
				contains(sweepEnd, pieStart, pieArc);
	}
	
	boolean wrapsAround(double pieStart, double pieArc, double sweepStart, double sweepArc) {
		double pieEnd = pieStart+pieArc;
		double sweepEnd = sweepStart+sweepArc;

		return contains(pieStart, sweepStart, sweepArc) &&
				contains(pieEnd, sweepStart, sweepArc) &&
				contains(sweepStart, pieStart, pieArc) &&
				contains(sweepEnd, pieStart, pieArc);
	}

	double[] headOverlapOnto(double pieStart, double pieArc, double sweepStart, double sweepArc) {
		double pieEnd = pieStart+pieArc;
		double sweepEnd = sweepStart+sweepArc;
		
		if (!overlaps(pieStart, pieArc, sweepStart, sweepArc)) {
			double[] overlap = {pieStart, 0.0};
			return overlap;
		}
		else {
			if (contains(pieEnd, sweepStart, sweepArc)) {
				sweepEnd = pieEnd;
				sweepArc = arcAngleFrom(sweepStart, sweepEnd);
			}
			if (contains(pieStart, sweepStart, sweepArc)) {
				sweepStart = pieStart;
				sweepArc = arcAngleFrom(sweepStart, sweepEnd);
			}
			
			double[] overlap = {sweepStart, sweepArc};
			return overlap;

		}
		
	}
	
	double[] tailOverlapOnto(double pieStart, double pieArc, double sweepStart, double sweepArc) {
		double pieEnd = pieStart+pieArc;
		double sweepEnd = sweepStart+sweepArc;
		
		if (!overlaps(pieStart, pieArc, sweepStart, sweepArc)) {
			double[] overlap = {pieStart, 0.0};
			return overlap;
		}
		else {
			if (contains(pieStart, sweepStart, sweepArc)) {
				sweepStart = pieStart;
				sweepArc = arcAngleFrom(sweepStart, sweepEnd);
			}
			if (contains(pieEnd, sweepStart, sweepArc)) {
				sweepEnd = pieEnd;
				sweepArc = arcAngleFrom(sweepStart, sweepEnd);
			}
			
			double[] overlap = {sweepStart, sweepArc};
			return overlap;

		}
		
	}

	double[] overlapOnto(double pieStart, double pieArc, double sweepStart, double sweepArc) {
		return tailOverlapOnto(pieStart, pieArc, sweepStart, sweepArc);
	}
}
