package Project;

import javax.swing.*;
import javax.swing.text.html.Option;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Deque;

public class EndFrame extends JFrame {

	private SpadesPanel sp;
	private Deque<Player> p;
  public EndFrame(SpadesPanel sp, Deque<Player> p){
    // sets frame to center of frame
    this.setLocationRelativeTo(null);
    this.sp = sp;
    this.p = p;
  }

  public void display(){
	  Object[] options = {"Play Again", "Quit"};

	    int team1TotalPts =
	            sp.players.getFirst().getTotalPoints() + sp.players.getFirst().getPartner().getTotalPoints();
	    int team2TotalPts =
	            sp.players.getLast().getTotalPoints() + sp.players.getLast().getPartner().getTotalPoints();

	    String team1P1 = sp.players.getFirst().getName();
	    String team1P2 = sp.players.getFirst().getPartner().getName();
	    String team2P1 = sp.players.getLast().getName();
	    String team2P2 = sp.players.getLast().getPartner().getName();

	    int choice;

	    if(team1TotalPts > team2TotalPts){
	      choice = JOptionPane.showOptionDialog(null,
	              "Winning team " + team1P1 + " " + team1P2,
	              "End of Game",
	              JOptionPane.YES_NO_OPTION,
	              JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
	    }
	    else{
	      choice = JOptionPane.showOptionDialog(null,
	              "Winning team " + team2P1 + " " + team2P2,
	              "End of Game",
	              JOptionPane.YES_NO_OPTION,
	              JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

	    }


//	    int choice = JOptionPane.showOptionDialog(null,
//	            "Winning team " + name + " " + name2,
//	            "End of Game",
//	            JOptionPane.YES_NO_OPTION,
//	            JOptionPane.QUESTION_MESSAGE, null, options, options[0]);


	    if(choice == JOptionPane.YES_OPTION){
	    	SpadesPanel.resetGame();
	    	this.dispose();
	    }

	    if(choice == JOptionPane.NO_OPTION){
	      System.exit(0);
	    }
  }
}