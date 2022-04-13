package ProjectScratch;

import javax.swing.JFrame;

public class SpadesGUI {
	public static void main(String[] args) {
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(true);
		window.setTitle("Spades");

		SpadesPanel spadesPanel = new SpadesPanel();
		window.add(spadesPanel);
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		spadesPanel.initGameThread();
		spadesPanel.playSpades();
	}
}
