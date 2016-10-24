import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Gui extends JPanel{
	
	private JFrame window = new JFrame("ElectReflect");
	private JButton start = new JButton();
	
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
		start.setLocation(0,100);
		start.setSize(100,50);
		add(start);
		start.setMnemonic(KeyEvent.VK_ENTER);
	}
	
	public void start(){
		
	}
	
	public static void main(String[] args){
		new Gui();
	}

}
