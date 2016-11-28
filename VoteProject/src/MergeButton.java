import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListModel;

public class MergeButton extends JButton {
	JList<Region> source;
	JList<Region> destination;
	
	void merge() {
		ListModel<Region> model = source.getModel();
		
		Gerrymander g = new Gerrymander(JOptionPane.showInputDialog("Enter a name:"));
		
		for (int i=0; i<model.getSize(); i++) {
			g.addRegion(model.getElementAt(i));
		}
		
		ListModel<Region> destinationModel = destination.getModel();
		Region[] regions = new Region[destinationModel.getSize()+1];
		
		for (int i=0; i<destinationModel.getSize(); i++) {
			regions[i] = destinationModel.getElementAt(i);
		}

		regions[regions.length-1] = g;
		
		destination.setListData(regions);
	}
	
	public MergeButton(JList<Region> source, JList<Region> destination) {
		this.source = source;
		this.destination = destination;
		
		this.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				merge();
			}
			
		});
	}
}
