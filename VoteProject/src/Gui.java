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
 * @update 2016-11-22:  Added  a select all displays check box and some comments.
 * @update 2016-11-26:  Added JToolTips to the JButtons
 *
 */

public class Gui extends JPanel{
	private static JFrame window = new JFrame("ElectReflect");
	private static JFrame frame = new JFrame("Message");
	
	private JButton addRegion = new JButton();
	private JButton showData = new JButton();
	
	private JCheckBox barGraph = new JCheckBox("Bar Graph");
	private JCheckBox pieChart = new JCheckBox("Pie Chart");
	private JCheckBox textSum = new JCheckBox("Text Summary");
	private JCheckBox allDisplays = new JCheckBox("All Displays");
	
	private JList<Region> regionSelect = new JList<Region>();
	private JList<Region> selectedValues = new JList<Region>();
	
	private JTextPane title = new JTextPane();
	private JScrollPane regionPane = new JScrollPane(regionSelect);
	private JScrollPane gerrymanderPane = new JScrollPane(selectedValues);
	
	private JToolTip addStateTip = new JToolTip();
	
	public Region[] regions;
	
	public Gui(){
		
		//========================================================================== Constructor
		super();
		super.setBackground(Color.white);
		window.setBounds(0, 0, 450, 350);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(true);
		this.setLayout(null);
		window.add(this);
		window.setVisible(true);
		
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e){
			e.printStackTrace();
		}
		
		//This frame is the frame used for dialog boxes when an error occurs
		frame.setSize(100, 100);
		frame.setLocation((window.getWidth() - frame.getWidth()) / 2, (window.getHeight() - frame.getHeight())/2);
		
		//========================================================================== Title
		title.setVisible(true);
		title.setText("ElectReflect");
		title.setSize(145,50);
		title.setLocation(150,20);
		title.setFont(new Font("Times New Roman", 0,28));
		title.setForeground(Color.blue);
		title.setAlignmentY(CENTER_ALIGNMENT);
		title.setEditable(false);
		title.setToolTipText("<html>" +"A software that interprets" + "<br>" + "and displays voter data." + "<html>");
		add(title);
		
		//========================================================================== JCheckBoxes
		
		add(allDisplays); // an option to select all three displays
		allDisplays.setBackground(Color.white);
		allDisplays.setSize(125,25);
		allDisplays.setLocation(163, 100);
		
		add(barGraph); // an option to just display the bar graph
		barGraph.setBackground(Color.white);
		barGraph.setSize(125, 25);
		barGraph.setLocation(163, 125);
		
		add(pieChart); // an option to just display the pie chart
		pieChart.setBackground(Color.white);
		pieChart.setSize(125, 25);
		pieChart.setLocation(163, 150);
		
		add(textSum); // an option to just display a text summary
		textSum.setBackground(Color.white);
		textSum.setSize(125, 25);
		textSum.setLocation(163, 175);
		
		//========================================================================== JTextPanes
		add(regionPane);
		regionPane.setLocation(10, 100);
		regionPane.setSize(125, 100);
		
		add(gerrymanderPane);
		gerrymanderPane.setLocation(300, 100);
		gerrymanderPane.setSize(125,100);
		
		//========================================================================== JButtons
		/**
		 * This is a JButton that adds a region to the gui.  It locally stores the state in
		 * an array and then transfers that to a JList.
		 * 
		 * This button also enables the show data button if a valid file is given.
		 */
		add(addRegion);
		addRegion.setText("Add State");
		addRegion.setLocation(22,200);
		addRegion.setSize(100,50);
		addRegion.setToolTipText("Add a text file with voter data.");
		addRegion.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent arg0) {				
				State state = new State();
				regions = new Region[state.getCounties().size()];
				
				for(int i = 0; i < regions.length; i++){
					regions[i] = state.getCounties().get(i);
				}
				
				regionSelect.setListData(regions);
				
				if(regions.length >= 1){
					showData.setEnabled(true);
				}
			}
			
		});
		
		/**
		 * This is a JButton that looks at which check boxes are selected and then
		 * displays the selected data from the first list, transfers it to a second
		 * list, and then calls Grapher.java to display the data.
		 */
		add(showData);
		showData.setText("<html>" + "Show Data" + "<html>");
		showData.setEnabled(false);
		showData.setLocation(312,200);
		showData.setSize(100,50);
		showData.setToolTipText("Open selected displays in separate windows.");
		showData.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent arg0) {
				Region[] selected = new Region[regionSelect.getSelectedValuesList().size()];
				
				for(int i = 0; i < regionSelect.getSelectedValuesList().size(); i++){
					selected[i] = regionSelect.getSelectedValuesList().get(i);
				}
				
				selectedValues.setListData(selected);
				if(allDisplays.isSelected()){
					Grapher.barGraph(new Gerrymander(regionSelect.getSelectedValuesList()));
					Grapher.pieChart(new Gerrymander(regionSelect.getSelectedValuesList()));
					Grapher.text(new Gerrymander(regionSelect.getSelectedValuesList()));
					return; // this prevents the user from selecting all check boxes and having two of each display pop up
				}
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
	/**
	 * This getter is so that State.java has access to the frame created in this class.
	 * This allows an easier way to create dialog boxes in State.java for error handling.
	 * 
	 * @return frame
	 */
	public static Component getFrame(){
		return frame;
	}
	
	//========================================================================== Main method
	
	public static void main(String[] args){
		new Gui();
	}

}
