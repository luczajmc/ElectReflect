import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class Gui extends JPanel{
	
	private JFrame window = new JFrame("ElectReflect");
	private JButton start = new JButton();
	private JComboBox graphSelect = new JComboBox();
	private JComboBox regionSelect = new JComboBox();
	private JTextPane title = new JTextPane();
	
	public Gui(){
		super();
		super.setBackground(Color.white);
		window.setBounds(0, 0, 600, 600);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(true);
		this.setLayout(null);
		window.add(this);
		window.setVisible(true);
		
		title.setLayout(null);
		title.setVisible(true);
		title.setText("ElectReflect");
		title.setSize(145,50);
		title.setLocation(227,20);
		title.setFont(new Font("Times New Roman", 0,28));
		title.setForeground(Color.blue);
		title.setAlignmentY(CENTER_ALIGNMENT);
		title.setEditable(false);
		add(title);
		
		add(graphSelect);
		graphSelect.setLayout(null);
		graphSelect.setLocation(10, 300);
		graphSelect.setSize(100, 25);
		graphSelect.addItem("Choose Display");
		graphSelect.addItem("Bar Graph");
		graphSelect.addItem("Pie Chart");
		graphSelect.addItem("Text Summary");
		
		add(regionSelect);
		regionSelect.setLayout(null);
		regionSelect.setLocation(10, 200);
		regionSelect.setSize(100, 25);
		regionSelect.addItem("Choose Region");
		regionSelect.addItem("State");
		regionSelect.addItem("Counties");
		
		add(start);
		start.setText("Start");
		start.setLayout(null);
		start.setLocation(10,400);
		start.setSize(100,50);
		start.setMnemonic(KeyEvent.VK_ENTER);
		start.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(graphSelect.getSelectedItem().equals("Pie Chart")){
					if(regionSelect.getSelectedItem().equals("State")){
						Grapher.pieChartState(new State());
					} else {
						Grapher.pieChartCounty(new County("Butler"));
					}
				} else if(graphSelect.getSelectedItem().equals("Bar Graph")){
					if(regionSelect.getSelectedItem().equals("State")){
						Grapher.barGraphState(new State());
					} else {
						Grapher.barGraphCounty(new County("Butler"));
					}
				} else {
					if(regionSelect.getSelectedItem().equals("State")){
						Grapher.text(new State());
					} else {
						Grapher.text(new County("Butler"));
					}
				}
			}
			
		});
	}
	
	public static void main(String[] args){
		new Gui();
	}

}
