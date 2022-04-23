/* TEAM: William Miller and Mike Stevens
   DATE: 4/22/2022
   ABOUT: enum class to model the suits of a standard 52 card deck.
 */

public enum Suit {
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
