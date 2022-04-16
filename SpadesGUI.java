package Project;

import javax.swing.JFrame;

public class SpadesGUI implements onMenuPanelListener{
	static JFrame window;
	public static void main(String[] args) {
		window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(true);
		window.setTitle("Spades");

		MenuPanel menuPanel = new MenuPanel();
		window.add(menuPanel);
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
	}

	public void changePanels() {
		window.removeAll();
		SpadesPanel spadesPanel = new SpadesPanel();
		window.add(spadesPanel);
		window.revalidate();
		window.repaint();
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		spadesPanel.initGameThread();
		spadesPanel.playSpades();
	}
}
