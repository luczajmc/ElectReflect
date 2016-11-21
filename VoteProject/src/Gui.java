import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * This class is the user interface for the Elect Reflect project for CSE 201.
 * It allows users to select files, select displays, and look at voter data.
 * 
 * @author Steven Bower
 * @version 2016-11-21
 *
 */

public class Gui extends JPanel{
	// FIXME: somehow there's a null element that gets in the JList and causes a
	//		  NullPointerException when it gets passed to Gerrymander()
	private static JFrame window = new JFrame("ElectReflect");
	private static JFrame frame = new JFrame("Message");
	
	private JButton addRegion = new JButton();
	private JButton showData = new JButton();
	
	private JCheckBox barGraph = new JCheckBox("Bar Graph");
	private JCheckBox pieChart = new JCheckBox("Pie Chart");
	private JCheckBox textSum = new JCheckBox("Text Summary");
	
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
		
		frame.setSize(100, 100);
		frame.setLocation((window.getWidth() - frame.getWidth()) / 2, (window.getHeight() - frame.getHeight())/2);
		
		title.setVisible(true);
		title.setText("ElectReflect");
		title.setSize(145,50);
		title.setLocation(150,20);
		title.setFont(new Font("Times New Roman", 0,28));
		title.setForeground(Color.blue);
		title.setAlignmentY(CENTER_ALIGNMENT);
		title.setEditable(false);
		add(title);
		
		add(barGraph);
		barGraph.setBackground(Color.white);
		barGraph.setSize(125, 25);
		barGraph.setLocation(10, 200);
		
		add(pieChart);
		pieChart.setBackground(Color.white);
		pieChart.setSize(125, 25);
		pieChart.setLocation(10, 225);
		
		add(textSum);
		textSum.setBackground(Color.white);
		textSum.setSize(125, 25);
		textSum.setLocation(10, 250);
		
		add(regionPane);
		regionPane.setLocation(10, 100);
		regionPane.setSize(125, 100);
		
		add(gerrymanderPane);
		gerrymanderPane.setLocation(160, 100);
		gerrymanderPane.setSize(125,100);
		
		add(addRegion);
		addRegion.setText("Add State");
		addRegion.setLocation(10,300);
		addRegion.setSize(100,50);
		addRegion.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent arg0) {				
				State state = new State();
				regions = new Region[state.getCounties().size()];
				
				for(int i = 0; i < regions.length; i++){
					regions[i] = state.getCounties().get(i);
				}
				
				regionSelect.setListData(regions);
				
				if(regions.length > 1){
					showData.setEnabled(true);
				}
			}
			
		});
		
		add(showData);
		showData.setText("Show Data");
		showData.setEnabled(false);
		showData.setLocation(120,300);
		showData.setSize(100,50);
		showData.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent arg0) {
				Region[] selected = new Region[regionSelect.getSelectedValuesList().size()];
				
				for(int i = 0; i < regionSelect.getSelectedValuesList().size(); i++){
					selected[i] = regionSelect.getSelectedValuesList().get(i);
				}
				
				selectedValues.setListData(selected);
				if(barGraph.isSelected()){
					Grapher.barGraph(new Gerrymander(regionSelect.getSelectedValuesList()));
				}
				if(pieChart.isSelected()){
					Grapher.pieChart(new Gerrymander(regionSelect.getSelectedValuesList()));
				}
				if(textSum.isSelected()){
					Grapher.text(new Gerrymander(regionSelect.getSelectedValuesList()));
				}
			}
			
		});
		
		window.repaint();
	}
	
	//========================================================================== Getters
	
	public static Component getFrame(){
		return frame;
	}
	
	//========================================================================== Main method
	
	public static void main(String[] args){
		new Gui();
	}

}
