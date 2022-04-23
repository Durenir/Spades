/* TEAM: William Miller and Mike Stevens
   DATE: 4/22/2022
   ABOUT: Here it is, the all mighty main method! Runs the game. Creates a
   JFrame to render the game to the display, creates and displays the menu and
   their associated menu items as well as associated action listeners to respond
   to user interation.
 */

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class SpadesGUI {

	final static SpadesPanel spadesPanel = new SpadesPanel();

	public static void main(String[] args) {

		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setTitle("Spades");

		// CREATE:  JMenuBar -> add to window frame
		JMenuBar menuBar = new JMenuBar();
		window.add(menuBar);

		// *** FILE MENU **********************************************************/
		JMenu file = new JMenu("File");
		file.setMnemonic(KeyEvent.VK_F);

		// FILE menu items -> NEW, LOAD, RULES --- CREATE AND ADD
		JMenuItem newGame = new JMenuItem("New Game", KeyEvent.VK_N);
		JMenuItem loadGame = new JMenuItem("Load Game", KeyEvent.VK_L);
		JMenuItem rules = new JMenuItem("Rules", KeyEvent.VK_R);
		file.add(newGame);
		file.add(loadGame);
		file.add(rules);

		newGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SpadesPanel.resetGame();
			}
		});
		
		loadGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SpadesPanel.loadAndStart();
			}
		});

		rules.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RulesFrame theRules = new RulesFrame();
				theRules.display();
			}
		});

		// *** QUIT MENU **********************************************************/
		JMenu quit = new JMenu("Quit");
		quit.setMnemonic(KeyEvent.VK_Q);

		// QUIT menu items -> EXIT --- CREATE AND ADD
		JMenuItem exit = new JMenuItem("Exit", KeyEvent.VK_E);
		quit.add(exit);

		// ACTION LISTENER for EXIT
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		// *** QUIT MENU  END  ****************************************************/

		// Add MENUS to MENU BAR
		menuBar.add(file);
		menuBar.add(quit);
		window.setJMenuBar(menuBar);

		// Add the spades panel the JFrame
		window.add(spadesPanel);
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);

		spadesPanel.newGame();

	}
}