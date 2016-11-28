import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListModel;

public class SplitButton extends JButton {
	JList source;
	JList destination;
	
	void split() {
		Region r = (Region) source.getSelectedValue();
		
		destination.setListData(r.getSubregions().toArray());
	}
	
	public SplitButton(JList source, JList destination) {
		super("Split");
		this.source = source;
		this.destination = destination;
		
		this.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				split();
			}
			
		});
	}
}
