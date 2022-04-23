/* TEAM: William Miller and Mike Stevens
   DATE: 4/22/2022
   ABOUT: Models a standard 52 card deck of playing cards.
 */

import java.awt.Graphics2D;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class Deck {

	// CopyOnWriteArrayList provides thread safety
	private CopyOnWriteArrayList<Card> deck = new CopyOnWriteArrayList<>();
	private static final int NUM_CARDS_OF_SUIT = 13;

	/* Constructor */
	public Deck() {
		int cardCounter = 0;

		// creates the 4 suits in a deck of cards
		for(int i = 0; i < Suit.values().length; i++) {

			// creates the cards within each suit: 2 to Ace
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
