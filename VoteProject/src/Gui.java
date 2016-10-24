import javax.swing.*;
import java.awt.*;

public class Gui extends JPanel{
	
	private JFrame window = new JFrame("ElectReflect");
	private JButton start = new JButton("Start");
	
	public Gui(){
		window.setBounds(0, 0, 600, 600);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(true);
		window.add(this);
		window.setVisible(true);
		window.add(start);
	}
	
	public static void main(String[] args){
		new Gui();
	}

}
