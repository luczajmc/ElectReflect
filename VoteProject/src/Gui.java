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
	
	private static JButton addRegion = new JButton();
	private static JButton showData = new JButton();
	private static JButton addSubregion = new JButton();
	private static JButton removeSubregion = new JButton();
	
	private static JCheckBox barGraph = new JCheckBox("Bar Graph");
	private static JCheckBox pieChart = new JCheckBox("Pie Chart");
	private static JCheckBox textSum = new JCheckBox("Text Summary");
	private static JCheckBox allDisplays = new JCheckBox("All Displays");
	
	private static JFileChooser fc = null;
	
	private static JList<Region> regionSelect = new JList<Region>();
	private static JList<Region> selectedValues = new JList<Region>();
	
	private static JMenuBar menu = new JMenuBar();
	private static JMenu file = new JMenu();
	private static JMenu help = new JMenu();
	private static JMenuItem save = new JMenuItem(); //TODO: save data selected
	private static JMenuItem exit = new JMenuItem();
	private static JMenuItem userGuide = new JMenuItem();
	
	private static JTextPane title = new JTextPane();
	private static JScrollPane regionPane = new JScrollPane(regionSelect);
	private static JScrollPane gerrymanderPane = new JScrollPane(selectedValues);
	
	private static JToolTip addStateTip = new JToolTip();
	
	private static Region[] regions;
	private static Region[] selected;
	private static State state;
	private static ArrayList<Region> oldSelection = new ArrayList<Region>();
	
	private final int WIDTH = 650;
	private final int HEIGHT = 450;
	
	public Gui(){
		
		//========================================================================== Constructor
		super();
		super.setBackground(Color.white);
		
		window.setBounds(0, 0, WIDTH, HEIGHT);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(true);
		this.setLayout(null);
		window.add(this);
		window.setVisible(true);
		window.setResizable(false);
		
		add(blueStripe);
		blueStripe.setBackground(Color.decode("#4085F4"));
		blueStripe.setSize(WIDTH,85);
		blueStripe.setLocation(0, HEIGHT - (HEIGHT/4));
		
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			fc = new JFileChooser("user.home");
			UIManager.setLookAndFeel(UIManager.getLookAndFeel());
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
		title.setSize(WIDTH,65);
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
		allDisplays.setLocation(WIDTH - WIDTH/4, 100);
		
		add(barGraph); // an option to just display the bar graph
		barGraph.setBackground(Color.white);
		barGraph.setSize(125, 25);
		barGraph.setLocation(WIDTH - WIDTH/4, 135);
		
		add(pieChart); // an option to just display the pie chart
		pieChart.setBackground(Color.white);
		pieChart.setSize(125, 25);
		pieChart.setLocation(WIDTH - WIDTH/4, 170);
		
		add(textSum); // an option to just display a text summary
		textSum.setBackground(Color.white);
		textSum.setSize(125, 25);
		textSum.setLocation(WIDTH - WIDTH/4, 205);
		
		//========================================================================== JTextPanes
		add(regionPane);
		regionPane.setLocation(10, title.getHeight() + 30);
		regionPane.setSize(125, HEIGHT/2);
		
		add(gerrymanderPane);
		gerrymanderPane.setLocation(310, title.getHeight() + 30);
		gerrymanderPane.setSize(125, HEIGHT/2);
		
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
				// TODO add confirmation dialogue
				save();
				
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
		addRegion.setLocation(WIDTH/5 + 30, HEIGHT/6 + 50);
		addRegion.setSize(125,50);
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
		showData.setLocation(WIDTH - WIDTH/4, HEIGHT/2 + 25);
		showData.setSize(125,50);
		showData.setBackground(Color.white);
		showData.setToolTipText("<html>" + "Opens selected displays" + "<br>" + "in a separate window." + "<html>");
		showData.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent arg0) {
				if(allDisplays.isSelected()){
					Grapher.barGraph(new Gerrymander(oldSelection));
					Grapher.pieChart(new Gerrymander(oldSelection));
					Grapher.text(new Gerrymander(oldSelection));
					return; // this prevents the user from selecting all check boxes and having two of each display pop up
				}
				if(barGraph.isSelected()){
					Grapher.barGraph(new Gerrymander(oldSelection));
				}
				if(pieChart.isSelected()){
					Grapher.pieChart(new Gerrymander(oldSelection));
				}
				if(textSum.isSelected()){
					Grapher.text(new Gerrymander(oldSelection));
				}
			}
			
		});
		
		add(addSubregion);
		addSubregion.setText("<html>" + "Add County" + "<html>");
		addSubregion.setLocation(WIDTH/5 + 30, HEIGHT/2);
		addSubregion.setEnabled(false);
		addSubregion.setSize(125,25);
		addSubregion.setBackground(Color.white);
		addSubregion.setToolTipText("<html>" + "Add a county to display." + "<html>");
		addSubregion.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent arg0) {
				if(oldSelection.isEmpty()){
					for(int i = 0; i < oldSelection.size(); i++){
						while(selected[i] != null){
							oldSelection.set(i, selected[i]);
						}
					}
				}
				for(int i = 0; i < regionSelect.getSelectedValuesList().size(); i++){
					if(!oldSelection.contains(regionSelect.getSelectedValuesList().get(i))){
						oldSelection.add(regionSelect.getSelectedValuesList().get(i));
					}
				}					
				update(selected);
			}
		});
		
		add(removeSubregion);
		removeSubregion.setText("<html>" + "Remove County" + "<html>");
		removeSubregion.setLocation(WIDTH/5 + 30, HEIGHT/2 + 50);
		removeSubregion.setEnabled(false);
		removeSubregion.setSize(125,25);
		removeSubregion.setBackground(Color.white);
		removeSubregion.setToolTipText("<html>" + "Remove a county from the" +"<br>" + "list of counties to display." + "<html>");
		removeSubregion.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent arg0) {
				for(int i = 0; i < oldSelection.size(); i++){
					for(int j = 0; j < selectedValues.getSelectedValuesList().size(); j++){
						if(oldSelection.get(i).equals(selectedValues.getSelectedValuesList().get(j))){
							selectedValues.getSelectedValuesList().remove(j);
							oldSelection.remove(i);
						}
					}
				}
				update(selected);
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
	
	//========================================================================== Methods
	
	private void update(Region[] r){
		r = new Region[oldSelection.size()];
		for(int i = 0; i < oldSelection.size(); i++){
			r[i] = oldSelection.get(i);
		}
		
		selectedValues.setListData(r);
	}
	
	private void save(){
		fc.showSaveDialog(Gui.this);
		File selectedData = new File(fc.getSelectedFile()+".txt");
		PrintWriter out = null;
		
		confirm(selectedData);
		
		try {
			out = new PrintWriter(selectedData);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		
		if(oldSelection == null){
			JOptionPane.showMessageDialog(null, "No data selected.");
			return;
		}
		for(int i = 0; i < oldSelection.size(); i++){
			if(oldSelection.get(i) != null){
				try{
					out.println(oldSelection.get(i).toString() + "County - Number of Republican votes: " + 
							oldSelection.get(i).getRepVotes()
							+ ", Number of Democratic votes: " + oldSelection.get(i).getDemVotes()+
							", Number of Independent votes: " + oldSelection.get(i).getIndVotes());
					
				} finally{}
			}
		}
		out.close();
	}
	
	private void confirm(File f){
		for(int i = 0; i < fc.getCurrentDirectory().listFiles().length; i++){
			if(fc.getCurrentDirectory().listFiles()[i].getName().equals(f.getName())){
				int result = JOptionPane.showConfirmDialog(null, "File already exists. Overwrite?");
				if(result == JOptionPane.NO_OPTION){
					save();
				} else if(result == JOptionPane.YES_OPTION){
					return;
				}
			}
				
		}
	}
	
	//========================================================================== Main method
	
	public static void main(String[] args){
		new Gui();
	}
}