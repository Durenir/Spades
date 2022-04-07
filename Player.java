package Project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class Player {
	private int bid;
	private int tricks;
	private int bags;
	private int totalTricks;
	private int totalBags;
	private int points;
	private int totalPoints;
	private Player partner;
	private String name;

	private ArrayList<Card> hand;

	public Player(String name) {
		this.name = name;
		hand = new ArrayList<Card>();
	}

	public void addCard(Card card) {
		card.setOwner(this);
		hand.add(card);
	}

	public Card playCard(Card card, Suit suit, boolean spadesBroken) {
		boolean valid;
		Card playerCard;
		do {
			valid = true;
			System.out.println(getHand());
			//Note: Range validation not needed because this is a test. Well be using onClickListeners for images in the GUI
			System.out.print("Pick a card index: ");
			Scanner in = new Scanner(System.in);
			playerCard = getHand().get(in.nextInt());
			if (playerCard.getSuit() == Suit.SPADE) {
				if (card == null && suit == null && !spadesBroken) {
					System.out.println("Spades have not been broken yet.");
					valid = false;
				} else {
					for (Card currentCard : getHand()) {
						// Played a spade while still having the suit.
						if (currentCard.getSuit() == suit
								&& !suit.equals(Suit.SPADE)) {
							System.out.println("You can not play a "
									+ playerCard.getSuit() + " while you have "
									+ suit);
							valid = false;
							break;
						}
					}
				}
			} else {
				for (Card currentCard : getHand()) {
					// Played a spade while still having the suit.
					if (currentCard.getSuit() == suit && playerCard.getSuit() != suit) {
						System.out.println("You can not play a "
								+ playerCard.getSuit() + " while you have "
								+ suit);
						valid = false;
						break;
					}
				}
			}
		} while (!valid);
		hand.remove(playerCard);
		return playerCard;
	}

	public void sortHand() {
		ArrayList<Card> hearts = new ArrayList<Card>();
		ArrayList<Card> spades = new ArrayList<Card>();
		ArrayList<Card> diamonds = new ArrayList<Card>();
		ArrayList<Card> clubs = new ArrayList<Card>();

		for (Card card : hand) {
			if (card.getSuit() == Suit.HEART) {
				hearts.add(card);
			} else if (card.getSuit() == Suit.SPADE) {
				spades.add(card);
			} else if (card.getSuit() == Suit.DIAMOND) {
				diamonds.add(card);
			} else if (card.getSuit() == Suit.CLUB) {
				clubs.add(card);
			}
		}
		Collections.sort(hearts, new Comparator<Card>() {

			public int compare(Card heart1, Card heart2) {
				return heart1.getValue() - heart2.getValue();
			}

		});
		Collections.sort(spades, new Comparator<Card>() {

			public int compare(Card spade1, Card spade2) {
				return spade1.getValue() - spade2.getValue();
			}

		});
		Collections.sort(diamonds, new Comparator<Card>() {

			public int compare(Card diamond1, Card diamond2) {
				return diamond1.getValue() - diamond2.getValue();
			}

		});
		Collections.sort(clubs, new Comparator<Card>() {

			public int compare(Card club1, Card club2) {
				return club1.getValue() - club2.getValue();
			}

		});
		hand.clear();
		hand.addAll(hearts);
		hand.addAll(spades);
		hand.addAll(diamonds);
		hand.addAll(clubs);
	}

	public int getBid() {
		return bid;
	}

	public void setBid(int bid) {
		this.bid = bid;
	}

	public int getTricks() {
		return tricks;
	}

	public void setTricks(int tricks) {
		this.tricks = tricks;
	}

	public int getTotalBags() {
		return totalBags;
	}

	public void setTotalBags(int totalBags) {
		this.totalBags = totalBags;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public int getTotalPoints() {
		return totalPoints;
	}

	public void setTotalPoints(int totalPoints) {
		this.totalPoints = totalPoints;
	}

	public ArrayList<Card> getHand() {
		return hand;
	}

	public Player getPartner() {
		return partner;
	}

	public void setPartner(Player partner) {
		this.partner = partner;
	}

	public void setHand(ArrayList<Card> hand) {
		this.hand = hand;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getBags() {
		return bags;
	}

	public void setBags(int bags) {
		this.bags = bags;
	}

	public int getTotalTricks() {
		return totalTricks;
	}

	public void setTotalTricks(int totalTricks) {
		this.totalTricks = totalTricks;
	}

}
