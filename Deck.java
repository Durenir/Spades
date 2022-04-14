package Project;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;

public class Deck {

	private ArrayList<Card> deck = new ArrayList<Card>();
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
		for(Card card : deck) {
			card.draw(g2);
		}
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
