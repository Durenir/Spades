import java.awt.Graphics2D;

public class Human extends Player {
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