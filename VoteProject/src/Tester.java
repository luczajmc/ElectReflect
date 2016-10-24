import java.io.File;
import javax.swing.JFileChooser;

public class Tester {

	public static void main(String[] args) {
		State Ohio = new State(State.getFile(), State.getFile());
		System.out.println(Ohio.toString());
	}
}
