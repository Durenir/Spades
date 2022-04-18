package Project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ScoreFrame extends JFrame {

  public ScoreFrame(String[] cn, String[][] d){

    JTable k = new JTable(d, cn) {
        public boolean isCellEditable(int row, int column) {
            //all cells false
            return false;
         }
    };
    JScrollPane sp = new JScrollPane(k);
    add(sp);

//    JButton playAgain = new JButton("Play next round");
//    playAgain.setBounds(100, 100, 100, 100);
//    add(playAgain);



  }

  public void display(){
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    Dimension d = new Dimension (450, 650);
    setMinimumSize(d);
    setVisible(true);
    setTitle("SCORES");
  }
}