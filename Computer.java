/* TEAM: William Miller and Mike Stevens
   DATE: 4/22/2022
   ABOUT: Inherits from Player and models a Computer player.
   Important methods getBidInput() which automatically calculates a bid for the
   Computer object prior to a new round. playCard() contains the logic for the
   Computer player to automatically play a card based on their hand.
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.security.SecureRandom;
import java.util.ArrayList;

public class Computer extends Player {

	/* Constructor */
	public Computer(String name) {
		super(name);
	}

	public void update() {
		if(getSelectedCard() != null) {
			getSelectedCard().update();
		}
		for(Card card : getHand()) {
			card.update();
		}
	}

	// renders the Name, Bid and Trick of player next to their position on the
	// display.
	public void draw(Graphics2D g2) {
		g2.setFont(new Font("Sego UI semibold", Font.PLAIN, 12));
		g2.setColor(Color.WHITE);
		g2.drawString("Name: " + this.getName(), getScoreZone().x, getScoreZone().y + 12);
		g2.drawString("Bid: " + this.getBid(), getScoreZone().x, getScoreZone().y + 12 * 2);
		g2.drawString("Tricks: " + this.getTricks(), getScoreZone().x, getScoreZone().y + (12 * 3));
		if(getSelectedCard() != null) {
			getSelectedCard().draw(g2);
		}
		for(Card card : getHand()) {
			card.draw(g2);
		}
	}

	public void addCard(Card card) {
		card.setOwner(this);
		getHand().add(card);
		this.calcAndApplyOffsets();
		card.setFaceUp(false);
	}

	// Automatically calcualte the bid for the Computer object based on their
	// hand
	public void getBidInput(SpadesPanel sp) {
		int bid = 0;
		int hearts = 0;
		int spades = 0;
		int diamonds = 0;
		int clubs = 0;
		for(Card card : getHand()) {
			switch(card.getValue()){
				case 11, 12, 13, 14 -> bid++;
				default -> {
				}
			}
			if(card.getSuit().equals(Suit.CLUB)) {
				clubs++;
			}
			if(card.getSuit().equals(Suit.HEART)) {
				hearts++;
			}
			if(card.getSuit().equals(Suit.DIAMOND)) {
				diamonds++;
			}
			if(card.getSuit().equals(Suit.SPADE)) {
				spades++;
			}
		}
		if(clubs <= 2) {
			bid++;
		}
		if(hearts <= 2) {
			bid++;
		}
		if(spades <= 2) {
			bid--;
		}
		if(diamonds <= 2) {
			bid++;
		}
		SpadesPanel.bid = bid;
	}

	// Logic for the Computer object to play a card
	public Card playCard(Card card, Suit suit, boolean spadesBroken, SpadesPanel sp) {
		// Take in the top card that's been played and the leading suit.
		Card playedCard = null;
		ArrayList<Card> options = new ArrayList<>();
		//Are we the first player in the round and spades aren't broken?
		if (card == null && suit == null && !spadesBroken) {
			//Get all the non spades
			for (Card currentCard : getHand()) {
				if (currentCard.getSuit() != Suit.SPADE) {
					options.add(currentCard);
				}
			}
			//Do we have any nonspades? If yes get the highest
			if (!options.isEmpty()) {
				playedCard = options.get(0);
				for (Card currentCard : options) {
					if (currentCard.getValue() > playedCard.getValue()) {
						playedCard = currentCard;
					}
				}
			} else {
				//If no nonspades, get the highest spade
				playedCard = getHand().get(0);
				for (Card currentCard : getHand()) {
					if (currentCard.getValue() > playedCard.getValue()) {
						playedCard = currentCard;
					}
				}
			}
			// Are we the first player and spades are broken?
		} else if(card == null && suit == null) {
			suit = getHand().get(new SecureRandom().nextInt(getHand().size())).getSuit();

			for (Card currentCard : getHand()) {
				if (currentCard.getSuit() == suit) {
					options.add(currentCard);
				}
			}
			playedCard = options.get(0);
			for (Card currentCard : options) {
				if (currentCard.getValue() > playedCard.getValue()) {
					playedCard = currentCard;
				}
			}
		} else {
			//Else we are not the first player and we need to compare
			//against the current high and leading suit.
			for (Card currentCard : getHand()) {
				if (currentCard.getSuit() == suit) {
					options.add(currentCard);
				}
			}
			// If highestCard is partners
			assert card != null;
			if (card.getOwner() == getPartner()) {
				if (!options.isEmpty()) {
					// play lowest
					playedCard = options.get(0);
					for (Card currentCard : options) {
						if (currentCard.getValue() < playedCard.getValue()) {
							playedCard = currentCard;
						}
					}
				} else {
					// else play lowest off suit
					// Get all cards of other suits.
					for (Card currentCard : getHand()) {
						if (currentCard.getSuit() != Suit.SPADE) {
							options.add(currentCard);
						}
					}
					// If there are any, get the lowest
					if (!options.isEmpty()) {

						playedCard = options.get(0);

						for (Card currentCard : options) {
							if (currentCard.getValue() < playedCard.getValue()) {
								playedCard = currentCard;
							}
						}
					} else {
						// Otherwise, we need to get the lowest spade
						for (Card currentCard : getHand()) {
							if (currentCard.getSuit() == Suit.SPADE) {
								options.add(currentCard);
							}
						}
						playedCard = options.get(0);
						for (Card currentCard : options) {
							if (currentCard.getValue() < playedCard.getValue()) {
								playedCard = currentCard;
							}
						}
					}
				}
			} else {
				if (!options.isEmpty()) {
					// play highest if possible, else play lowest
					Card highest = options.get(0);
					Card lowest = options.get(0);
					for (Card currentCard : options) {
						if (currentCard.getValue() > highest.getValue()) {
							highest = currentCard;
						}
						if (currentCard.getValue() < lowest.getValue()) {
							lowest = currentCard;
						}
					}
					if (highest.getValue() > card.getValue()) {
						playedCard = highest;
					} else {
						playedCard = lowest;
					}
				} else {
					// get spades
					for (Card currentCard : getHand()) {
						if (currentCard.getSuit() == Suit.SPADE) {
							options.add(currentCard);
						}
					}
					// if has spades
					if (!options.isEmpty()) {
						Card highest = options.get(0);
						Card lowest = options.get(0);
						for (Card currentCard : options) {
							if (currentCard.getValue() < lowest.getValue()) {
								lowest = currentCard;
							}
							if(currentCard.getValue() > highest.getValue()) {
								highest = currentCard;
							}
						}
						//If the current high card is a spade and we can beat it, beat it with our highest.
						if(card.getSuit() == Suit.SPADE && (highest.getValue() > card.getValue())) {
							playedCard = highest;
						} else {
							//otherwise throw our lowest to win so we don't waste our high cards.
							playedCard = lowest;
						}
					} else {
						// else play lowest off suit
						for (Card currentCard : getHand()) {
							if (currentCard.getSuit() != Suit.SPADE) {
								options.add(currentCard);
							}
						}
						playedCard = options.get(0);
						for (Card currentCard : options) {
							if (currentCard.getValue() < playedCard.getValue()) {
								playedCard = currentCard;
							}
						}
					}
				}
			}
		}
		playedCard.setFaceUp(true);
		setSelectedCard(playedCard);
		getSelectedCard().setDestination(getPlayZone());
		getHand().remove(playedCard);
		if(getHand().size() > 0) {
			resetAndRecalcOffset();
		}
		return playedCard;
	}
}