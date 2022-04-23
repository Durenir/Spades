/* TEAM: William Miller and Mike Stevens
   DATE: 4/22/2022
   ABOUT: Creates a JFrame to display the score at the end of each round.
   Includes a scroll feature and utilizes a JTable to neatly layout pertinent
   information.
 */

import javax.swing.*;
import java.awt.*;

public class ScoreFrame extends JFrame {

  /* Constructor */
  public ScoreFrame(String[] cn, String[][] d){

    JTable k = new JTable(d, cn) {
        public boolean isCellEditable(int row, int column) {
            //all cells false
            return false;
         }
    };
    JScrollPane sp = new JScrollPane(k);
    add(sp);
  }

  public void display(){
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    Dimension d = new Dimension (450, 650);
    setMinimumSize(d);
    setVisible(true);
    setTitle("SCORES");
  }
}