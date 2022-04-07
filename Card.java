package Project;

public class Card {

	private Suit suit;
	private Face face;
	private int value;
	private Player owner;

	public Card(Suit suit, Face face) {
		this.suit = suit;
		this.face = face;
		if(face == Face.ACE) {
			value = 14;
		} else {
			value = face.ordinal() + 1;
		}
		if(suit == Suit.SPADE) {
			value += 13;
		}
	}

	public Suit getSuit() {
		return suit;
	}

	public void setSuit(Suit suit) {
		this.suit = suit;
	}

	public Face getFace() {
		return face;
	}

	public void setFace(Face face) {
		this.face = face;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public String toString() {
		return(getFace().toString() + " of " + getSuit().toString() +
				" has a value of " + getValue());
	}
}
