import java.awt.event.MouseEvent;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

public class SyncedChartPanel extends ChartPanel {
		ChartPanel sister;
		
		@Override
		public void mouseEntered(MouseEvent e) {
			super.mouseEntered(e);
			if (sister != null) {
				sister.mouseEntered(e);
			}
		}
		
		@Override
		public void mouseExited(MouseEvent e) {
			super.mouseExited(e);
			if (sister != null) {
				sister.mouseExited(e);
			}
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
			super.mousePressed(e);
			if (sister != null) {
				sister.mousePressed(e);
			}
		}
		
		@Override
		public void mouseDragged(MouseEvent e) {
			super.mouseDragged(e);
			if (sister != null) {
				sister.mouseDragged(e);
			}
		}
		
		@Override
		public void mouseReleased(MouseEvent e) {
			super.mouseReleased(e);
			if (sister != null) {
				sister.mouseReleased(e);
			}
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			super.mouseClicked(e);
			if (sister != null) {
				sister.mouseClicked(e);
			}
		}
		
		public SyncedChartPanel(JFreeChart chart, int width, int height, int minWidth,
				int minHeight, int maxWidth, int maxHeight, boolean defaultBuffer,
				boolean properties, boolean save, boolean print, boolean zoom,
				boolean tooltips) {
			super(chart, width, height, minWidth, minHeight, maxWidth, maxHeight,
					defaultBuffer, properties, save, print, zoom, tooltips, tooltips);
			
		}
		
		public void setSisterPanel(ChartPanel sister) {
			this.sister = sister;
		}
		
		public ChartPanel getSisterPanel() {
			return this.sister;
		}
	}
