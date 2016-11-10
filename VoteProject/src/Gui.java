import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class Gui extends JPanel{
	
	private JFrame window = new JFrame("ElectReflect");
	private JButton start = new JButton();
	private JComboBox graphSelect = new JComboBox();
	
	public Gui(){
		super();
		window.setBounds(0, 0, 600, 600);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(true);
		this.setLayout(null);
		window.add(this);
		window.setVisible(true);
		
		start.setText("Start");
		start.setLayout(null);
		start.setLocation(10,400);
		start.setSize(100,50);
		add(start);
		start.setMnemonic(KeyEvent.VK_ENTER);
		
		start.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(graphSelect.getSelectedItem().equals("Pie Chart")){
					Grapher.pieChart(new State());
				}
			}
			
		});
		
		graphSelect.setLayout(null);
		graphSelect.setLocation(10, 300);
		graphSelect.setSize(100, 25);
		add(graphSelect);
		graphSelect.addItem("Choose Display");
		graphSelect.addItem("Bar Graph");
		graphSelect.addItem("Pie Chart");
		graphSelect.addItem("Text Summary");
	}
	
	public static void main(String[] args){
		new Gui();
	}

}
