import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.ListModel;

public class SortMenu extends JComboBox {
	JList<Region> target;
	
	Region[] getTargetRegions() {
		ListModel<Region> model = target.getModel();
		
		Region[] regions = new Region[model.getSize()];
		for (int i=0; i<model.getSize(); i++) {
			regions[i] = model.getElementAt(i);
		}

		return regions;
	}
	
	void sortTarget() {
		Region[] regions = getTargetRegions();
		Arrays.sort(regions, (Comparator<Region>) this.getSelectedItem());
		target.setListData(regions);
		
	}
	
	SortMenu(Object[] orderings, JList<Region> target) {
		super(orderings);
		this.target = target;
		sortTarget();
		
		this.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				
				sortTarget();
			}
			
		});
	}
}
