package Project;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;

public class Player {
	private int bid;
	private int tricks = 0;
	private int bags;
	private int totalTricks;
	private int totalBags;
	private int points;
	private int totalPoints;
	private Player partner;
	private String name;
	private int[] playZone;
	private int[] handZone;
	private Card selectedCard;
	private int handZoneWidth;
	private int[] handZoneCardOffsetModifier;
	private int[] handZoneOffset;
	private int[] handZoneOffsetMarker;
	Card playerCard;
	private Rectangle scoreZone;
	JDialog dialog;

	private CopyOnWriteArrayList <Card> hand;

	public Player(String name) {
		this.name = name;
		hand = new CopyOnWriteArrayList <Card>();
		playZone = new int[2];
		handZone = new int[2];
		handZoneOffsetMarker = new int[2];
		scoreZone = new Rectangle();
	}

	public void update() {
		if (selectedCard != null) {
			selectedCard.update();
		}
		for (Card card : getHand()) {
			card.update();
		}
	}

	public void draw(Graphics2D g2) {
		g2.setFont(new Font("Sego UI semibold", Font.PLAIN, 12));
		g2.setColor(Color.WHITE);
		g2.drawString(String.valueOf("Name: " + this.getName()), getScoreZone().x, getScoreZone().y + 12);
		g2.drawString(String.valueOf("Bid: " + this.getBid()), getScoreZone().x, getScoreZone().y + 12 * 2);
		g2.drawString(String.valueOf("Tricks: " + this.getTricks()), getScoreZone().x, getScoreZone().y + (12 * 3));
		if (selectedCard != null) {
			selectedCard.draw(g2);
		}
		for (Card card : getHand()) {
			card.draw(g2);
		}
	}

	public void reInitHand() {
		hand = new CopyOnWriteArrayList <Card>();
	}

	public void addCard(Card card) {
		card.setOwner(this);
		card.setDestination(getHandZoneX(), getHandZoneY());
		hand.add(card);
		this.calcAndApplyOffsets();
		this.setRectangles();
		card.setFaceUp(true);
	}

	public boolean waitForCards() {
		for (Card card : hand) {
			if (card.getPosition()[0] != card.getDestination()[0]) {
				return true;
			}
		}
		return false;
	}

	public Card playCard(Card card, Suit suit, boolean spadesBroken,
			SpadesPanel sp) {
		boolean valid = true;
		boolean lock = true;
		playerCard = null;
		while (lock) {
			lock = waitForCards();
		}
		setRectangles();
		sp.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Iterator<Card> itr = hand.iterator();
				while(itr.hasNext()) {
					Card card = itr.next();
					if (card.getRec().contains(e.getPoint())) {
						System.out.println(card + " was clicked");
						playerCard = card;
						System.out.println(playerCard.toString());
					}
				}
			}
		});
		while (true) {
			if (playerCard != null) {
				System.out.println("Picked a card");
				if (playerCard.getSuit() == Suit.SPADE) {
					if (card == null && suit == null && !spadesBroken) {
						System.out.println("Spades have not been broken yet.");
						valid = false;
						continue;
					} else {
						for (Card currentCard : getHand()) {
							// Played a spade while still having the suit.
							if (currentCard.getSuit() == suit
									&& !suit.equals(Suit.SPADE)) {
								System.out.println("You can not play a "
										+ playerCard.getSuit()
										+ " while you have " + suit);
								valid = false;
								continue;
							}
						}
					}
				} else {
					for (Card currentCard : getHand()) {
						// Played a spade while still having the suit.
						if (currentCard.getSuit() == suit
								&& playerCard.getSuit() != suit) {
							System.out.println("You can not play a "
									+ playerCard.getSuit() + " while you have "
									+ suit);
							valid = false;
							continue;
						}
					}
				}
				if(valid) {
					selectedCard = playerCard;
					selectedCard.setDestination(playZone);
					hand.remove(playerCard);
					if(hand.size() > 0) {
						resetAndRecalcOffset();
					}
					return playerCard;
				} else {
					playerCard = null;
					valid = true;
				}
			} else {
				if(SpadesPanel.reset) {
					return null;
				}
				Thread.yield();
			}
		}
	}

	private void setRectangles() {
		for (Card card : hand) {
			if (card != hand.get(hand.size() - 1)) {
				card.setRec(new Rectangle(card.getPosition()[0], card
						.getPosition()[1], Card.getWidth() / 2, Card
						.getHeight()));
			} else {
				card.setRec(new Rectangle(card.getPosition()[0], card
						.getPosition()[1], Card.getWidth(), Card.getHeight()));
			}
		}
	}

	public void getBidInput(SpadesPanel sp) {
		// Enter bid -- only do this for a human player
		System.out.println("Enter bid...");

		/*https://stackoverflow.com/questions/10522121/instantiate-jdialog-from
		-jpanel*/

		Window parentWindow = SwingUtilities.windowForComponent(sp);
		Frame parentFrame = null;
		if(parentWindow instanceof Frame){
			parentFrame = (Frame) parentWindow;
		}

		// populate this Dialog box iff there is a human player...
		// NOTE - the game will still execute if this nothing happens here...
		dialog = new JDialog(parentFrame, "Bid Input");
		dialog.setLayout(new FlowLayout());

		dialog.setSize(700, 100);
		dialog.setResizable(false);
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);

		JRadioButton rb1, rb2, rb3, rb4, rb5, rb6, rb7, rb8, rb9, rb10, rb11,
						rb12, rb13, rb14;
		JButton setBid;

		rb1 = new JRadioButton("1");
		rb1.setBounds(100,5,100,30);
		rb1.setSelected(true);

		rb2 = new JRadioButton("2");
		rb2.setBounds(100,10,100,30);

		rb3 = new JRadioButton("3");
		rb3.setBounds(100,15,100,30);

		rb4 = new JRadioButton("4");
		rb4.setBounds(100,20,100,30);

		rb5 = new JRadioButton("5");
		rb5.setBounds(100,25,100,30);

		rb6 = new JRadioButton("6");
		rb6.setBounds(100,30,100,30);

		rb7 = new JRadioButton("7");
		rb7.setBounds(100,35,100,30);

		rb8 = new JRadioButton("8");
		rb8.setBounds(100,40,100,30);

		rb9 = new JRadioButton("9");
		rb9.setBounds(100,45,100,30);

		rb10 = new JRadioButton("10");
		rb10.setBounds(100,50,100,30);

		rb11 = new JRadioButton("11");
		rb11.setBounds(100,55,100,30);

		rb12 = new JRadioButton("12");
		rb12.setBounds(100,60,100,30);

		rb13 = new JRadioButton("13");
		rb13.setBounds(100,65,100,30);

		rb14 = new JRadioButton("Save and Quit");
		rb14.setBounds(100, 70, 100, 30);

		final ButtonGroup bg = new ButtonGroup();
		bg.add(rb1);
		bg.add(rb2);
		bg.add(rb3);
		bg.add(rb4);
		bg.add(rb5);
		bg.add(rb6);
		bg.add(rb7);
		bg.add(rb8);
		bg.add(rb9);
		bg.add(rb10);
		bg.add(rb11);
		bg.add(rb12);
		bg.add(rb12);
		bg.add(rb13);
		bg.add(rb14);
		setBid = new JButton("Set Bid");
		setBid.setBounds(10, 10, 20, 20);
		dialog.add(rb1);
		dialog.add(rb2);
		dialog.add(rb3);
		dialog.add(rb4);
		dialog.add(rb5);
		dialog.add(rb6);
		dialog.add(rb7);
		dialog.add(rb8);
		dialog.add(rb9);
		dialog.add(rb10);
		dialog.add(rb11);
		dialog.add(rb12);
		dialog.add(rb13);
		dialog.add(rb14);
		dialog.add(setBid);
		dialog.revalidate();
		dialog.repaint();
		setBid.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			    for (Enumeration<AbstractButton> buttons = bg.getElements(); buttons.hasMoreElements();) {
			        AbstractButton button = buttons.nextElement();
			        if (button.isSelected()) {
			        	if(button.getText() == "Save and Quit") {
			        		SpadesPanel.saveGame();
			        	} else {
			        		System.out.println(button.getText() + " was selected");
			        		setBid(Integer.parseInt(button.getText()));
			        	}
		        		dialog.dispose();
			        }
			    }
			}
		});
		while(dialog.isVisible()) {
			if(SpadesPanel.reset || SpadesPanel.load) {
				dialog.dispose();
			}
		}
	}

	public void reset() {
		bid = 0;
		tricks = 0;
		bags = 0;
		System.out.println("Handzone is " + this.handZone[0] + ", " + this.handZone[1]);
		System.out.println("Handzone offset is " + this.handZoneOffset[0] + ", " + this.handZoneOffset[1]);
		System.out.println("HandzoneOffsetModifier is " + this.handZoneCardOffsetModifier[0] + ", " + this.handZoneCardOffsetModifier[1]);
		this.handZoneOffset[0] = this.handZoneOffsetMarker[0];
		this.handZoneOffset[1] = this.handZoneOffsetMarker[1];
		System.out.println("Handzone offset is " + this.handZoneOffset[0] + ", " + this.handZoneOffset[1]);
		System.out.println("Handzone is " + this.handZone[0] + ", " + this.handZone[1]);
		System.out.println("HandzoneOffsetModifier is " + this.handZoneCardOffsetModifier[0] + ", " + this.handZoneCardOffsetModifier[1]);
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

	public void calcAndApplyOffsets() {
		int[] temp = { handZoneOffset[0], handZoneOffset[1] };
		for (Card card : hand) {
			int[] firstPoint = { this.handZone[0] + temp[0],
					this.handZone[1] + temp[1] };
			card.setDestination(firstPoint);
			temp[0] += this.handZoneCardOffsetModifier[0];
			temp[1] += this.handZoneCardOffsetModifier[1];
		}
	}

	public void resetAndRecalcOffset() {
		System.out.println("---------" + handZoneOffset[0] + " "
				+ handZoneOffset[1] + "----------------");
		if (handZoneOffset[0] > 0) {
			handZoneOffset[0] += Card.getWidth() / hand.size();
		} else if (handZoneOffset[0] < 0) {
			handZoneOffset[0] -= (Card.getWidth() / hand.size());
			;
		} else if (handZoneOffset[1] > 0) {
			handZoneOffset[1] += Card.getWidth() / hand.size();
		} else if (handZoneOffset[1] < 0) {
			handZoneOffset[1] -= (Card.getWidth() / hand.size());
			;
		}
		calcAndApplyOffsets();
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

	public void incrementTricks(){ this.tricks++; }

	public void resetTricks() {this.tricks = 0; }

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

	public CopyOnWriteArrayList <Card> getHand() {
		return hand;
	}

	public Player getPartner() {
		return partner;
	}

	public void setPartner(Player partner) {
		this.partner = partner;
	}

	public void setHand(CopyOnWriteArrayList <Card> hand) {
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

	public float getPlayZoneX() {
		return playZone[0];
	}

	public float getPlayZoneY() {
		return playZone[1];
	}

	public void setPlayZoneX(int playZoneX) {
		this.playZone[0] = playZoneX;
	}

	public void setPlayZoneY(int playZoneY) {
		this.playZone[1] = playZoneY;
	}

	public int[] getPlayZone() {
		return playZone;
	}

	public void setPlayZone(int x, int y) {
		this.playZone[0] = x;
		this.playZone[1] = y;
	}

	public void setTotalTricks(int totalTricks) {
		this.totalTricks = totalTricks;
	}

	public Card getSelectedCard() {
		return selectedCard;
	}

	public void setSelectedCard(Card selectedCard) {
		this.selectedCard = selectedCard;
	}

	public void setPlayZone(int[] playZone) {
		this.playZone = playZone;
	}

	public int getHandZoneX() {
		return handZone[0];
	}

	public int getHandZoneY() {
		return handZone[1];
	}

	public void setHandZoneX(int handZoneX) {
		this.handZone[0] = handZoneX;
	}

	public void setHandZoneY(int handZoneY) {
		this.handZone[1] = handZoneY;
	}

	public int[] getHandZone() {
		return playZone;
	}

	public void setHandZone(int x, int y, int[] offset, int xOffsetModifier,
			int yOffsetModifier, int width) {
		this.handZone[0] = x;
		this.handZone[1] = y;
		this.handZoneCardOffsetModifier[0] = xOffsetModifier;
		this.handZoneCardOffsetModifier[1] = yOffsetModifier;
		this.handZoneOffset = offset;
		this.handZoneWidth = width;
		this.handZoneOffsetMarker[0] = this.handZoneOffset[0];
		this.handZoneOffsetMarker[1] = this.handZoneOffset[1];
		}

	public void setHandZone(int x, int y, int[] offset, int[] offsetModifier,
			int width) {
		this.handZone[0] = x;
		this.handZone[1] = y;
		this.handZoneCardOffsetModifier = offsetModifier;
		this.handZoneOffset = offset;
		this.handZoneWidth = width;
		this.handZoneOffsetMarker[0] = this.handZoneOffset[0];
		this.handZoneOffsetMarker[1] = this.handZoneOffset[1];
		}

	public void setHandZone(int[] handZone, int[] offset, int[] offsetModifier,
			int width) {
		this.handZone = handZone;
		this.handZoneCardOffsetModifier = offsetModifier;
		this.handZoneOffset = offset;
		this.handZoneWidth = width;
		this.handZoneOffsetMarker[0] = this.handZoneOffset[0];
		this.handZoneOffsetMarker[1] = this.handZoneOffset[1];
		}

	public Rectangle getScoreZone() {
		return scoreZone;
	}

	public void setScoreZone(int x, int y) {
		this.scoreZone.x = x;
		this.scoreZone.y = y;
		this.scoreZone.width = 100;
		this.scoreZone.height = 75;
	}
}