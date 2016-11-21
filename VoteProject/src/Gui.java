import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class Gui extends JPanel{
	private JFrame window = new JFrame("ElectReflect");
	private JButton addRegion = new JButton();
	private JButton showData = new JButton();
	private JComboBox<String> graphSelect = new JComboBox<String>();
	private JList<Region> regionSelect = new JList<Region>();
	private JList<Region> selectedValues = new JList<Region>();
	private JTextPane title = new JTextPane();
	private JScrollPane regionPane = new JScrollPane(regionSelect);
	private JScrollPane gerrymanderPane = new JScrollPane(selectedValues);
	public Region[] regions;
	
	public Gui(){
		super();
		super.setBackground(Color.white);
		window.setBounds(0, 0, 450, 450);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(true);
		this.setLayout(null);
		window.add(this);
		window.setVisible(true);
		
		title.setLayout(null);
		title.setVisible(true);
		title.setText("ElectReflect");
		title.setSize(145,50);
		title.setLocation(150,20);
		title.setFont(new Font("Times New Roman", 0,28));
		title.setForeground(Color.blue);
		title.setAlignmentY(CENTER_ALIGNMENT);
		title.setEditable(false);
		add(title);
		
		add(graphSelect);
		graphSelect.setLayout(null);
		graphSelect.setLocation(10, 200);
		graphSelect.setSize(100, 25);
		graphSelect.addItem("Choose Display");
		graphSelect.addItem("Bar Graph");
		graphSelect.addItem("Pie Chart");
		graphSelect.addItem("Text Summary");
		
		add(regionPane);
		regionPane.setLocation(10, 100);
		regionPane.setSize(100, 100);
		
		add(gerrymanderPane);
		gerrymanderPane.setLocation(120, 100);
		gerrymanderPane.setSize(100,100);
		
		add(addRegion);
		addRegion.setText("Add State");
		addRegion.setLayout(null);
		addRegion.setLocation(10,300);
		addRegion.setSize(100,50);
		addRegion.setMnemonic(KeyEvent.VK_ENTER);
		addRegion.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent arg0) {
				State state = new State();
				regions = new Region[state.getCounties().size()];
				
				for(int i = 0; i < regions.length-1; i++){
					regions[i] = state.getCounties().get(i);
				}
				regionSelect.setListData(regions);
			}
			
		});
		
		add(showData);
		showData.setText("Show Data");
		showData.setLayout(null);
		showData.setLocation(120,300);
		showData.setSize(100,50);
		showData.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent arg0) {
				Region[] selected = new Region[regionSelect.getSelectedValuesList().size()];
				
				for(int i = 0; i < regionSelect.getSelectedValuesList().size(); i++){
					selected[i] = regionSelect.getSelectedValuesList().get(i);
				}
				
				selectedValues.setListData(selected);
				if(graphSelect.getSelectedItem().equals("Bar Graph")){
					Grapher.barGraph(new Gerrymander(regionSelect.getSelectedValuesList()));
				}
				
				if(graphSelect.getSelectedItem().equals("Pie Chart")){
					Grapher.pieChart(new Gerrymander(regionSelect.getSelectedValuesList()));
				}
				
				if(graphSelect.getSelectedItem().equals("Text Summary")){
					Grapher.text(new Gerrymander(regionSelect.getSelectedValuesList()));
				}
			}
			
		});
	}
	
	public static void main(String[] args){
		new Gui();
	}

}
