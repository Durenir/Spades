package Project;

import java.io.Serializable;

public enum Suit implements Serializable{
	HEART("Hearts"), SPADE("Spades"), CLUB("Clubs"), DIAMOND("Diamond");
	
	private final String suit;
	private static final Suit[] suitList = Suit.values();

	Suit(final String suit) {
		this.suit = suit;
	}
	
	public static Suit getSuit(int i) {
		return suitList[i];
	}
	
	@Override
	public String toString() {
		return suit;
	}
}
