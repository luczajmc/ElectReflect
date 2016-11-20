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
	private JList<County> countySelect = new JList<County>();
	private JList<County> selectedValues;
	private JTextPane title = new JTextPane();
	private JScrollPane regionPane = new JScrollPane(countySelect);
	private JScrollPane gerrymanderPane = new JScrollPane(selectedValues);
	public County[] counties;
	
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
		
		add(regionPane);
		regionPane.setLocation(10, 200);
		regionPane.setSize(100, 100);
		
		add(gerrymanderPane);
		gerrymanderPane.setLocation(120, 200);
		gerrymanderPane.setSize(100,100);
		
		add(addRegion);
		addRegion.setText("Add State");
		addRegion.setLayout(null);
		addRegion.setLocation(120,400);
		addRegion.setSize(100,50);
		addRegion.setMnemonic(KeyEvent.VK_ENTER);
		addRegion.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent arg0) {
				State state = new State();
				counties = new County[state.getCounties().size()];
				
				for(int i = 0; i < counties.length; i++){
					counties[i] = state.getCounties().get(i);
				}
				countySelect.setListData(counties);
			}
			
		});
		
		add(showData);
		showData.setText("Show Data");
		showData.setLayout(null);
		showData.setLocation(20,400);
		showData.setSize(100,50);
		showData.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent arg0) {
				County[] selected = new County[countySelect.getSelectedValuesList().size()];
				
				for(int i = 0; i < countySelect.getSelectedValuesList().size(); i++){
					selected[i] = countySelect.getSelectedValuesList().get(i);
				}
				selectedValues.setListData(selected);
//				if(graphSelect.getSelectedItem().equals("Bar Graph")){
//					Grapher.barGraph();
//				}
//				
//				if(graphSelect.getSelectedItem().equals("Pie Chart")){
//					Grapher.pieChart();
//				}
			}
			
		});
	}
	
	public static void main(String[] args){
		new Gui();
	}

}
