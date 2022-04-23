/* TEAM: William Miller and Mike Stevens
   DATE: 4/22/2022
   ABOUT: Extends the Player class. Note: Unused but included to
   allow for flexibility with maintenance and future improvements.
   object.
 */

import java.awt.Graphics2D;
import java.io.Serializable;

public class Human extends Player implements Serializable{

	/* Constructor */
	public Human(String name) {
		super(name);
	}

	public void update() {
		if (getSelectedCard() != null) {
			getSelectedCard().update();
		}
		for (Card card : getHand()) {
			card.update();
		}
	}

	public void draw(Graphics2D g2) {
		if (getSelectedCard() != null) {
			getSelectedCard().draw(g2);
		}
		for (Card card : getHand()) {
			card.draw(g2);
		}
	}
}