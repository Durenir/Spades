import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class Deck {

	private CopyOnWriteArrayList<Card> deck = new CopyOnWriteArrayList <Card>();
	private static final int NUM_CARDS_OF_SUIT = 13;

	public Deck() {
		int cardCounter = 0;
		for(int i = 0; i < Suit.values().length; i++) {
			for(int j = 0; j < NUM_CARDS_OF_SUIT; j++) {
				deck.add(new Card(Suit.getSuit(i), Face.getFace(j), "Image" + cardCounter + ".png"));
				cardCounter++;
			}
		}
	}

	public void update() {
		for(Card card : deck) {
			card.update();
		}
	}

	public void draw(Graphics2D g2) {
		Iterator<Card> itr = deck.iterator();
		while(itr.hasNext()) {
			Card card = itr.next();
			card.draw(g2);
		}
	}

	public void setPosition(int x, int y) {
		for(Card card: deck) {
			card.setDestination(x, y);
		}
	}

	public boolean checkPositionReady() {
		for(Card card : deck) {
			if(card.getPosition()[0] != card.getDestination()[0]) {
				return false;
			}
		}
		return true;
	}

	public Card drawCard() {
		return deck.remove(0);
	}

	public void shuffle() {
		Collections.shuffle(deck);
	}

	public int getSize() {
		return deck.size();
	}

	public boolean isEmpty() {
		return deck.isEmpty();
	}

	public void printDeck() {
		for(Card card : deck) {
			System.out.println(card);
		}
	}
}