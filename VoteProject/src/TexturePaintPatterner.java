import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class TexturePaintPatterner {
	public static final int DEFAULT_SWATCH_SIZE = 30;
	
	static int rgb(int r, int g, int b) {
		Color c = new Color(r,g,b,255);
		return c.getRGB();
	}
	static int green() {
		return rgb(0, 255, 0);
	}
	static int red() {
		return rgb(255, 0, 0);
	}
	static int blue() {
		return rgb(0,0,255);
	}
	
	static BufferedImage swatch(int size) {
		return new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
	}
	
	public BufferedImage getPattern(int size) {
		BufferedImage swatch = swatch(size);
		
		Graphics2D big = swatch.createGraphics();
	    big.setColor(Color.gray);
	    big.fillRect(0, 0, swatch.getWidth(), swatch.getHeight());
	    return swatch;

	}
	
	public TexturePaint getTexturePaint(int size) {
		BufferedImage swatch = getPattern(size);
		Rectangle r = new Rectangle(size, size);
		
		return new TexturePaint(swatch, r);
	}

}
