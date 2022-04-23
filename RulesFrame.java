/* TEAM: William Miller and Mike Stevens
   DATE: 4/22/2022
   ABOUT: Creates a JFrame to display the rules of the game. Includes a scroll
   feature.
 */

import javax.swing.*;
import java.awt.*;

public class RulesFrame extends JFrame{

  /* Constructor */
  public RulesFrame(){

    // sets the attributes of the frame
    JTextPane rules = new JTextPane();
    Color c = new Color(245, 245, 245);
    rules.setBackground(c);

    // creates the option to scroll the pane
    JScrollPane ruleScroll = new JScrollPane(rules);

    // sets the textual information
    rules.setContentType("text/html");
    rules.setText("<h1>The Pack</h1>\n" +
            "<p>The standard 52-card pack is used.</p>\n" +
            "<h1>Rank of Suits</h1>\n" +
            "<p>The spade suit is always trump.</p>\n" +
            "<h1>Obejct of the Game</h1>\n" +
            "<p>A (high), K, Q, J, 10, 9, 8, 7, 6, 5, 4, 3, 2.</p>\n" +
            "<h1>The Deal</h1>\n" +
            "<p>To win at least the number of tricks bid.</p>\n" +
            "<h1>The Bidding</h1>\n" +
            "<p>The first dealer is chosen by a draw for high card, and thereafter the turn\n" +
            "    to deal proceeds clockwise. The entire deck is dealt one at a time, face\n" +
            "    down, beginning on the dealer's left. The players then pick up their cards\n" +
            "    and arrange them by suits.</p>\n" +
            "<h1>The Play</h1>\n" +
            "<p>Each player decides how many tricks they will be able to take. The player to\n" +
            "    the dealer's left starts the bidding and, in turn, each player states how\n" +
            "    many tricks they expect to win. There is only one round of bidding, and the\n" +
            "    minimum bid is One. Every player must make a bid; no player may pass. No\n" +
            "    suit is named in the bid, for as the name of the game implies, spades are\n" +
            "    always trump.</p>\n" +
            "<h1>How To Keep Score</h1>\n" +
            "<p>For making the contract (the number of tricks bid), the player scores 10\n" +
            "    points for each trick bid, plus 1 point for each overtrick.\n" +
            "    For example, if the player's bid is Seven and they make seven tricks, the\n" +
            "    score would be 70. If the bid was Five and the player won eight tricks, the\n" +
            "    score would be 53 points: 50 points for the bid, and 3 points for the three\n" +
            "    overtricks. In some games, overtricks are called \"bags\" and a deduction of\n" +
            "    100 points is made every time a player accumulates 10 bags. Thus, the object\n" +
            "    is always to fulfill the bid exactly.\n" +
            "    If the player \"breaks contract,\" that is, if they take fewer than the number\n" +
            "    of tricks bid, the score is 0. For example, if a player bids Four and wins\n" +
            "    only three tricks, no points are awarded.\n" +
            "    One of the players is the scorer and writes the bids down, so that during\n" +
            "    the play and for the scoring afterward, this information will be available\n" +
            "    to all the players. When a hand is over, the scores should be recorded next\n" +
            "    to the bids, and a running score should be kept so that players can readily\n" +
            "    see each other's total points. If there is a tie, then all players\n" +
            "    participate in one more round of play.\n" +
            "</p>");
    rules.setEditable(false);
    add(ruleScroll);


  }

  public void display(){
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    Dimension d = new Dimension (450, 650);
    setMinimumSize(d);
    setVisible(true);
    setTitle("Rules");
  }

}