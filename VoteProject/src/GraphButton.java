import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListModel;

public class GraphButton extends JButton {
	JList list;
	
	void graph() {
		Gerrymander g = new Gerrymander(list.getSelectedValuesList());
		
		Grapher.barGraph(g);
		Grapher.text(g);
		Grapher.pieChart(g);
	}
	
	public GraphButton(JList list) {
		super("Graph");
		this.list = list;
		
		this.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				graph();
			}
			
		});
	}
}
