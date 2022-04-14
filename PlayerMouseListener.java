package Project;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;


public class PlayerMouseListener implements MouseListener{

	ArrayList<Card> cards;

	public PlayerMouseListener(ArrayList<Card> cards) {
		this.cards = cards;
	}

	public void mouseClicked(MouseEvent e) {
		for(Card card : cards) {
			if(card.getRec().contains(e.getPoint())) {
				System.out.println(card + " was clicked");
			}
		}
	}
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}

}
