/* TEAM: William Miller and Mike Stevens
   DATE: 4/22/2022
   ABOUT: Creates the frame for the end of game display.
   Important methods are display().
 */

import javax.swing.*;
import java.util.Deque;

public class EndFrame extends JFrame {

	private SpadesPanel sp;
	private Deque<Player> p;

	/* Constructor */
  public EndFrame(SpadesPanel sp, Deque<Player> p){
    // sets frame to center of frame
    this.setLocationRelativeTo(null);
    this.sp = sp;
    this.p = p;
  }

	// Displays the frame
  public void display(){

		// Create an array of objects. These are the text annunciations that
		// will display on the option buttons.
	  Object[] options = {"Play Again", "Quit"};


		// need to obtain info about each team to render on the panel -> team names
		// and team points.
		int team1TotalPts =
						sp.players.getFirst().getTotalPoints() + sp.players.getFirst().getPartner().getTotalPoints();
		int team2TotalPts =
						sp.players.getLast().getTotalPoints() + sp.players.getLast().getPartner().getTotalPoints();

	    String team1P1 = sp.players.getFirst().getName();
	    String team1P2 = sp.players.getFirst().getPartner().getName();
	    String team2P1 = sp.players.getLast().getName();
	    String team2P2 = sp.players.getLast().getPartner().getName();


			/* Controls the flow of the game based on the selection made on the
			 * JOptionPane. Displays the winning team and prompts to play again. */

	    int choice;

			// if team 1 wins
	    if(team1TotalPts > team2TotalPts){
	      choice = JOptionPane.showOptionDialog(null,
	              "Winning team " + team1P1 + " " + team1P2,
	              "End of Game",
	              JOptionPane.YES_NO_OPTION,
	              JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
	    }
		  // if team 2 wins
	    else{
	      choice = JOptionPane.showOptionDialog(null,
	              "Winning team " + team2P1 + " " + team2P2,
	              "End of Game",
	              JOptionPane.YES_NO_OPTION,
	              JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
	    }

			// control flow based on option selcted -> yes starts game, no exits
		  // program gracefully.
	    if(choice == JOptionPane.YES_OPTION){
	    	SpadesPanel.resetGame();
	    	this.dispose();
	    }

	    if(choice == JOptionPane.NO_OPTION || choice == JOptionPane.CLOSED_OPTION){
	      System.exit(0);
	    }
  }
}