import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.TextAttribute;
import java.io.*;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;
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
	private static JPanel blueStripe = new JPanel();
	
	private JButton addRegion = new JButton();
	private JButton showData = new JButton();
	private JButton addSubregion = new JButton();
	private JButton removeSubregion = new JButton();
	
	private JCheckBox barGraph = new JCheckBox("Bar Graph");
	private JCheckBox pieChart = new JCheckBox("Pie Chart");
	private JCheckBox textSum = new JCheckBox("Text Summary");
	private JCheckBox allDisplays = new JCheckBox("All Displays");
	
	private final JFileChooser fc = new JFileChooser("user.home");
	
	private JList<Region> regionSelect = new JList<Region>();
	private JList<Region> selectedValues = new JList<Region>();
	
	private JMenuBar menu = new JMenuBar();
	private JMenu file = new JMenu();
	private JMenu help = new JMenu();
	private JMenuItem save = new JMenuItem(); //TODO: save data selected
	private JMenuItem exit = new JMenuItem();
	private JMenuItem userGuide = new JMenuItem();
	
	private JTextPane title = new JTextPane();
	private JScrollPane regionPane = new JScrollPane(regionSelect);
	private JScrollPane gerrymanderPane = new JScrollPane(selectedValues);
	
	private JToolTip addStateTip = new JToolTip();
	
	public Region[] regions;
	public Region[] selected;
	public State state;
	
	public int numItems = 0;
	
	public Gui(){
		
		//========================================================================== Constructor
		super();
		super.setBackground(Color.white);
		
		window.setResizable(false);
		window.setBounds(0, 0, 450, 350);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(true);
		this.setLayout(null);
		window.add(this);
		window.setVisible(true);
		
		add(blueStripe);
		blueStripe.setBackground(Color.decode("#4085F4"));
		blueStripe.setSize(450,65);
		blueStripe.setLocation(0, 250);
		
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e){
			e.printStackTrace();
		}
		
		//This frame is the frame used for dialog boxes when an error occurs
		frame.setSize(100, 100);
		frame.setLocation((window.getWidth() - frame.getWidth()) / 2, (window.getHeight() - frame.getHeight())/2);
		
		//========================================================================== Title
		
		// this underlines the title
		Font font = new Font("Times New Roman", 0,32);
		Map attr = font.getAttributes();
		attr.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		
		// this centers the title
		StyledDocument doc = title.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength()-1, center, false);
				
		title.setVisible(true);
		title.setText("ElectReflect");
		title.setSize(450,65);
		title.setLocation(0,20);
		title.setFont(font.deriveFont(attr));
		title.setBackground(Color.decode("#FE4841"));
		title.setAlignmentX(JTextPane.CENTER_ALIGNMENT);
		title.setEditable(false);
		title.setToolTipText("<html>" +"A software that interprets" + "<br>" + "and displays voter data." + "<html>");
		add(title);		
		
		//========================================================================== JCheckBoxes

		add(allDisplays); // an option to select all three displays
		allDisplays.setBackground(Color.white);
		allDisplays.setSize(125,25);
		allDisplays.setLocation(163, 90);
		
		add(barGraph); // an option to just display the bar graph
		barGraph.setBackground(Color.white);
		barGraph.setSize(125, 25);
		barGraph.setLocation(163, 115);
		
		add(pieChart); // an option to just display the pie chart
		pieChart.setBackground(Color.white);
		pieChart.setSize(125, 25);
		pieChart.setLocation(163, 140);
		
		add(textSum); // an option to just display a text summary
		textSum.setBackground(Color.white);
		textSum.setSize(125, 25);
		textSum.setLocation(163, 165);
		
		//========================================================================== JTextPanes
		add(regionPane);
		regionPane.setLocation(10, 90);
		regionPane.setSize(125, 100);
		
		add(gerrymanderPane);
		gerrymanderPane.setLocation(300, 90);
		gerrymanderPane.setSize(125,100);
		
		//========================================================================== Menu Bar
		
		add(fc);

		add(menu);
		menu.setSize(window.getWidth(), 20);
		menu.setLocation(0, 0);
		menu.setVisible(true);
		menu.add(file);
		menu.add(help);
		
		help.setText("Help");
		help.add(userGuide);
		userGuide.setText("User Guide");
		userGuide.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				File userGuide = new File("User_Guide.pdf");
				try {
					Desktop.getDesktop().open(userGuide);
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, "File Not Found");
				}
			}
		});
		
		file.setText("File");
		file.add(save);
		file.add(exit);
		
		save.setText("Save");
		save.setToolTipText("Save current selected data.");
		save.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO
				fc.showSaveDialog(Gui.this);
				File selectedData = new File(fc.getSelectedFile()+".txt");
				PrintWriter out = null;
				
				try {
					out = new PrintWriter(selectedData);
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
				
				if(selected == null){
					JOptionPane.showMessageDialog(null, "No data selected.");
					return;
				}
				for(int i = 0; i < selected.length; i++){
					if(selected[i] != null){
						try{
							out.println(selected[i].toString() + "County - Number of Republican votes: " + selected[i].getRepVotes()
									+ ", Number of Democratic votes: " + selected[i].getDemVotes()+
									", Number of Independent votes: " + selected[i].getIndVotes());
							
						} finally{}
					}
				}
				out.close();
				
				save.setEnabled(true);
			}
		});
		
		exit.setText("Exit");
		exit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
				
			}
			
		});
		
		//========================================================================== JButtons
		/**
		 * This is a JButton that adds a region to the gui.  It locally stores the state in
		 * an array and then transfers that to a JList.
		 * 
		 * This button also enables the show data button if a valid file is given.
		 */
		add(addRegion);
		addRegion.setText("Add State");
		addRegion.setLocation(22,195);
		addRegion.setSize(100,50);
		addRegion.setToolTipText("<html>" + "Add a text or csv file" + "<br>" + "with voter data." + "<html>");
		addRegion.setBackground(Color.white);
		addRegion.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent arg0) {				
				state = DataHandler.makeState();
				regions = new Region[state.getCounties().size()];
				System.out.println(state.getCounties().size());
				
				for(int i = 0; i < regions.length; i++){
					regions[i] = state.getCounties().get(i);
				}
				
				regionSelect.setListData(regions);
				
				if(regions.length >= 1){
					showData.setEnabled(true);
					addSubregion.setEnabled(true);
					removeSubregion.setEnabled(true);
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
		showData.setLocation(312,195);
		showData.setSize(100,50);
		showData.setBackground(Color.white);
		showData.setToolTipText("<html>" + "Opens selected displays" + "<br>" + "in a separate window." + "<html>");
		showData.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent arg0) {
//				selected = new Region[regionSelect.getSelectedValuesList().size()];
//				
//				for(int i = 0; i < regionSelect.getSelectedValuesList().size(); i++){
//					selected[i] = regionSelect.getSelectedValuesList().get(i);
//				}
//				
//				selectedValues.setListData(selected);
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
		
		add(addSubregion);
		addSubregion.setText("<html>" + "Add County" + "<html>");
		addSubregion.setLocation(153, 195);
		addSubregion.setEnabled(false);
		addSubregion.setSize(125,25);
		addSubregion.setBackground(Color.white);
		addSubregion.setToolTipText("<html>" + "Add a county to display." + "<html>");
		addSubregion.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent arg0) {
				ArrayList<Region> oldSelection = new ArrayList<Region>();
				if(selected == null){
					selected = new Region[regions.length];
				} else {
					for(int i = 0; i < oldSelection.size(); i++){
						while(selected[i] != null)
						oldSelection.set(i, selected[i]);
					}
				}
				
				for(int i = 0; i < regionSelect.getSelectedValuesList().size(); i++){
					selected[i] = regionSelect.getSelectedValuesList().get(i);
					oldSelection.add(regionSelect.getSelectedValuesList().get(i));
				}
				
				for(int i = 0; i <oldSelection.size(); i++){
					selected[i] = oldSelection.get(i);
				}
				
				selectedValues.setListData(selected);
			}
		});
		
		add(removeSubregion);
		removeSubregion.setText("<html>" + "Remove County" + "<html>");
		removeSubregion.setLocation(153, 220);
		removeSubregion.setEnabled(false);
		removeSubregion.setSize(125,25);
		removeSubregion.setBackground(Color.white);
		removeSubregion.setToolTipText("<html>" + "Remove a county from the" +"<br>" + "list of counties to display." + "<html>");
		removeSubregion.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent arg0) {
				
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