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

	static TexturePaintPatterner indPatterner = new TexturePaintPatterner() {
		@Override
		public BufferedImage getPattern(int size) {
			BufferedImage swatch = swatch(size);
			Graphics2D big = swatch.createGraphics();
		    
		    double scale = 1.0/2.0;
		    int[] trapezoidX = {0, 0, (int) (scale*swatch.getWidth()), swatch.getWidth()};
		    int[] trapezoidY = {0, (int) ((1-scale)*swatch.getHeight()), swatch.getHeight(), swatch.getHeight()};

		    big.setColor(Color.green);
			big.fill(new Polygon(trapezoidX, trapezoidY, 4));

			return swatch;
		}
	};

	static TexturePaintPatterner demPatterner = new TexturePaintPatterner() {
		@Override
		public BufferedImage getPattern(int size) {
			BufferedImage swatch = swatch(size);
		
			Graphics2D big = swatch.createGraphics();
			big.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			big.setColor(Color.blue);
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
		    big.setColor(Color.red);
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
