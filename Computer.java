package Project;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;

public class Computer extends Player {

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

	public void draw(Graphics2D g2) {
		g2.setFont(new Font("Sego UI semibold", Font.PLAIN, 12));
		g2.setColor(Color.WHITE);
		g2.drawString(String.valueOf("Name: " + this.getName()), getScoreZone().x, getScoreZone().y + 12);
		g2.drawString(String.valueOf("Bid: " + this.getBid()), getScoreZone().x, getScoreZone().y + 12 * 2);
		g2.drawString(String.valueOf("Tricks: " + this.getTricks()), getScoreZone().x, getScoreZone().y + (12 * 3));
		if(getSelectedCard() != null) {
			getSelectedCard().draw(g2);
		}
		for(Card card : getHand()) {
			card.draw(g2);
		}
	}

	public void addCard(Card card) {
		card.setOwner(this);
//		card.setDestination(getHandZoneX(), getHandZoneY());
		getHand().add(card);
		this.calcAndApplyOffsets();
		card.setFaceUp(false);
	}
	
	public void getBidInput(SpadesPanel sp) {
		int bid = 0;
		int hearts = 0;
		int spades = 0;
		int diamonds = 0;
		int clubs = 0;
		for(Card card : getHand()) {
			switch(card.getValue()) {
			case 11:
			case 12:
			case 13:
			case 14:
				bid++;
				break;
			default:
				break;
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
		this.setBid(bid);
	}

	public Card playCard(Card card, Suit suit, boolean spadesBroken, SpadesPanel sp) {
		// Take in the top card that's been played and the leading suit.
		Card playedCard = null;
		ArrayList<Card> options = new ArrayList<Card>();
		//Are we the first player in the round and spades aren't broken?
		if (card == null && suit == null && !spadesBroken) {
			System.out.println("First to play and spades not broken.");
			//Get all the non spades
			for (Card currentCard : getHand()) {
				if (currentCard.getSuit() != Suit.SPADE) {
					options.add(currentCard);
				}
			}
			//Do we have any nonspades? If yes get the highest
			if (!options.isEmpty()) {
				System.out.println("Playing nonspade.");
				playedCard = options.get(0);
				for (Card currentCard : options) {
					if (currentCard.getValue() > playedCard.getValue()) {
						playedCard = currentCard;
					}
				}
			} else {
				//If no nonspades, get the highest spade
				System.out.println("Playing a spade");
				playedCard = getHand().get(0);
				for (Card currentCard : getHand()) {
					if (currentCard.getValue() > playedCard.getValue()) {
						playedCard = currentCard;
					}
				}
			}
		// Are we the first player and spades are broken?
		} else if(card == null && suit == null && spadesBroken) {
			System.out.println("First to play and spades broken.");
			suit = getHand().get(new SecureRandom().nextInt(getHand().size())).getSuit();
			System.out.println("Selecting random suit: " + suit.toString());
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
			if (card.getOwner() == getPartner()) {
				System.out.println("Highest owned by my Partner");
				if (!options.isEmpty()) {
					// play lowest
					System.out.println("Playing lowest in suit.");
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
						System.out.println("Playing lowest off suit.");
						playedCard = options.get(0);
						for (Card currentCard : options) {
							if (currentCard.getValue() < playedCard.getValue()) {
								playedCard = currentCard;
							}
						}
					} else {
						// Otherwise, we need to get the lowest spade
						System.out.println("Playing lowest off suit spade.");
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
				System.out.println("Highest not owned by my partner");
				if (!options.isEmpty()) {
					// play highest if possible, else play lowest
					System.out.println("Playing on suit.");
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
						System.out.println("Playing highest.");
						playedCard = highest;
					} else {
						System.out.println("Playing lowest.");
						playedCard = lowest;
					}
				} else {
					// get spades
					System.out.println("Playing spade off suit.");
					for (Card currentCard : getHand()) {
						if (currentCard.getSuit() == Suit.SPADE) {
							options.add(currentCard);
						}
					}
					// if has spades
					if (!options.isEmpty()) {
						System.out.println("We have spades.");
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
						System.out
								.println("No spades to play. Playing offsuit.");
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