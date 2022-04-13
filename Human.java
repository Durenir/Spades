package ProjectScratch;

import java.awt.Graphics2D;

public class Human extends Player {
	public Human(String name) {
		super(name);
	}

	public void update() {
		for(Card card : getHand()) {
			card.update();
		}
	}

	public void draw(Graphics2D g2) {
		for(Card card : getHand()) {
			card.draw(g2);
		}
	}
}
