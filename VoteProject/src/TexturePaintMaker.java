import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;

public class TexturePaintMaker {
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
	
	static BufferedImage patternSquare() {
		return new BufferedImage(8, 8, BufferedImage.TYPE_INT_ARGB);
	}
	// clobbers its argument
	static BufferedImage indPattern() {
		BufferedImage swatch = patternSquare();
		
		int width = swatch.getWidth();
		int height = swatch.getHeight();
		for (int i=0; i<width*height; i++) {
			int y = i/width;
			int x = i%width;
			System.out.print(swatch.getRGB(x,y));
			if (i%30<15 && i%30>0) {
				System.out.println();
				swatch.setRGB(i%width, i/width, green());
				System.out.print("("+swatch.getRGB(x, y)+")");
				swatch.getRGB(0, 0);
			}
		}
		return swatch;
	}

	static BufferedImage demPattern() {
		BufferedImage swatch = patternSquare();
		
		Graphics2D big = swatch.createGraphics();
		big.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    big.setColor(Color.blue);
	    big.drawLine(0, 0, swatch.getWidth(), swatch.getHeight());
	    return swatch;
	}
	
	static BufferedImage repPattern() {
		BufferedImage swatch = patternSquare();
		
		Graphics2D big = swatch.createGraphics();
	    big.setColor(Color.red);
	    big.fillRect(0, 0, swatch.getWidth(), swatch.getHeight());
	    int yOffset = swatch.getHeight()/4;
	    int xOffset = swatch.getWidth()/4;
	    int width = swatch.getWidth();
	    int height = swatch.getHeight();
	    big.setColor(Color.lightGray);
	    big.fillRect(xOffset, yOffset, width-xOffset, height-yOffset);
	    return swatch;
		
	}
	
	static TexturePaint getPaint(BufferedImage texture) {
	    Rectangle r = new Rectangle(0, 0, 5, 5);

	    return new TexturePaint(texture, r);
	}
	
	static TexturePaint[] getPaints() {
		TexturePaint[] paints = {getPaint(repPattern()), getPaint(demPattern()), getPaint(indPattern())};
		return paints;
	}
	  
}
