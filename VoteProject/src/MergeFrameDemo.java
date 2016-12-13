import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class MergeFrameDemo {
	public static void main(String[] args) {
		State panOhio = DataHandler.makeState("../Data/");

		JFrame mergeFrame = new JFrame("Merge");
		mergeFrame.getContentPane().setLayout(new BoxLayout(mergeFrame.getContentPane(), BoxLayout.LINE_AXIS));
		
		JList gerryList = new JList(panOhio.getCounties().toArray());
		mergeFrame.add(gerryList);

		JList regionList = new JList();
		regionList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				// Oh, OK, this particular NullPointerException is OK
				// TODO: find out if the other list gets these
				Region r = (Region) ((JList) e.getSource()).getSelectedValue();
				System.out.println(r.getSubregions());
			}
			
		});

		mergeFrame.add(regionList);
		
		mergeFrame.add(new SortMenu(RegionSorter.getOrderings().toArray(), gerryList));
		
		mergeFrame.add(new MergeButton(gerryList, regionList));
		
		mergeFrame.add(new SplitButton(regionList, gerryList));
		
		mergeFrame.add(new ClipButton(gerryList));
		
		mergeFrame.add(new GraphButton(regionList));
		
		mergeFrame.pack();
		mergeFrame.setVisible(true);
		

	}
}
