import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;

public class ClipButton extends JButton {
	JList list;
	
	void remove() {
		ArrayList<Region> regions = new ArrayList<>();
		
		ListModel model = list.getModel();
		ListSelectionModel selectionModel = list.getSelectionModel();
		
		for (int i=0; i<model.getSize(); i++) {
			if (selectionModel.isSelectedIndex(i)) {
				regions.add((Region) model.getElementAt(i));
			}
		}

		list.setListData(regions.toArray());
	}
	
	public ClipButton(JList list) {
		super("Clip");
		this.list = list;
		
		this.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				remove();
			}
			
		});
	}
}
