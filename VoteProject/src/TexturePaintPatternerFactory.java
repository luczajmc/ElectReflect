import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;

public class TexturePaintPatternerFactory {
	
	static Color dark(Color color) {
		float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
		hsb[2] *= 0.75;
		return Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
	}
	
	static Color light(Color color) {
		float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
		hsb[2] *= 1.5;
		hsb[2] = (float) Math.min(1.0, hsb[2]);
		return Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
	}

	static Color saturated(Color color) {
		float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
		hsb[1] *= 2.0;
		hsb[1] = (float) Math.min(1.0, hsb[1]);
		return Color.getHSBColor(hsb[0], hsb[1], hsb[2]);		
	}
	
	static Color desaturated(Color color) {
		float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
		hsb[1] *= 0.5;
		return Color.getHSBColor(hsb[0], hsb[1], hsb[2]);		
	}

	static Color gray(Color color) {
		int red = color.getRed();
		int blue = color.getBlue();
		int green = color.getGreen();
		
		int grayIntensity = (red+blue+green)/3;
		return new Color(grayIntensity, grayIntensity, grayIntensity);
	}
	static TexturePaintPatterner indPatterner = new TexturePaintPatterner() {
		@Override
        public BufferedImage getPattern(int size) {
            BufferedImage swatch = swatch(size);
            Graphics2D big = swatch.createGraphics();
            
            Color baseColor = Color.green;
            
            big.setColor(desaturated(baseColor));
            big.fillRect(0, 0, size, size);
        
	        double scale = 1.0/2.0;
	        int[] trapezoidX = {0, 0, (int) (scale*swatch.getWidth()), swatch.getWidth()};
	        int[] trapezoidY = {0, (int) ((1-scale)*swatch.getHeight()), swatch.getHeight(), swatch.getHeight()};
	
	        big.setColor(dark(baseColor));
	            big.fill(new Polygon(trapezoidX, trapezoidY, 4));
	
	            return swatch;
	    }
	};

	static TexturePaintPatterner zigzag = new TexturePaintPatterner() {
		@Override
		public BufferedImage getPattern(int size) {
			BufferedImage swatch = swatch(size);
			Graphics2D big = swatch.createGraphics();
			
			Color baseColor = Color.yellow;
			
			big.setColor(desaturated(baseColor));
			big.fillRect(0, 0, size, size/2);

			big.setColor(dark(baseColor));
			big.fillRect(0, size/2, size, size/2);
			
			double scale = 1.0/2.0;
			int[] topRhombusX = {0, (int) (scale*swatch.getWidth()),
					(int) ((scale+scale/2)*swatch.getWidth()),
					(int) ((scale/2)*swatch.getWidth())
			};
			int[] topRhombusY = {0, 0, (int) (scale*swatch.getHeight()),
					(int) (scale*swatch.getHeight())};
			
		    big.setColor(dark(baseColor));
			big.fill(new Polygon(topRhombusX, topRhombusY, 4));

		    int[] bottomRhombusX = topRhombusX;
		    int[] bottomRhombusY = {(int) (scale*swatch.getHeight()),
		    		(int) (scale*swatch.getHeight()),
		    		(int) ((2*scale)*swatch.getHeight()),
		    		(int) ((2*scale)*swatch.getHeight())};

		    big.setColor(desaturated(baseColor));
			big.fill(new Polygon(bottomRhombusX, bottomRhombusY, 4));

			return swatch;
		}
	};

	static TexturePaintPatterner demPatterner = new TexturePaintPatterner() {
		@Override
		public BufferedImage getPattern(int size) {
			BufferedImage swatch = swatch(size);
		
			Graphics2D big = swatch.createGraphics();
			
			Color baseColor = Color.blue;
			
			big.setColor(desaturated(baseColor));
			big.fillRect(0, 0, size, size);

			big.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			big.setColor(dark(baseColor));
			big.fillRect(0, 0, swatch.getWidth()/2, swatch.getHeight()/2);
			big.fillRect(swatch.getWidth()/2, swatch.getHeight()/2, swatch.getWidth(), swatch.getHeight());
			return swatch;
		}
	};

	static TexturePaintPatterner repPatterner = new TexturePaintPatterner() {
		@Override
		public BufferedImage getPattern(int size) {
			BufferedImage swatch = swatch(size);
			
			Graphics2D big = swatch.createGraphics();
			
			Color baseColor = Color.red;
			
			big.setColor(desaturated(baseColor));
			big.fillRect(0, 0, size, size);

		    big.setColor(dark(baseColor));
		    int[] triangleX = {0, swatch.getWidth(), 0};
		    int[] triangleY = {0, 0, swatch.getHeight()};
		    big.fill(new Polygon(triangleX, triangleY, 3));
		    return swatch;
			
		}
	};
	
	public static TexturePaintPatterner[] getPatterners() {
		TexturePaintPatterner[] patterners = {repPatterner, demPatterner, indPatterner};
		
		return patterners;
	}
	 
}
