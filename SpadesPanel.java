package Project;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Deque;
import java.util.LinkedList;

import javax.swing.JPanel;

public class SpadesPanel extends JPanel implements Runnable{

	Thread gameThread;
	int FPS = 60;
	Deque<Player> players = new LinkedList<Player>();
	Deck deck;
	boolean spadesBroken = false;


	public SpadesPanel() {
		this.setPreferredSize(new Dimension(1000, 1000));
		this.setBackground(new Color(71, 113, 72));
		this.setDoubleBuffered(true);
		this.setFocusable(true);
		//Initialize deck
		deck = new Deck();
		deck.shuffle();
	}

	public void initGame() {
		//Calculate PlayZones
		//TODO move this when resizable
		int xCenter = this.getWidth()/2;
		int yCenter = this.getHeight()/2;
		int zone1X = xCenter - (Card.getWidth() / 2);
		int zone1Y = yCenter + (Card.getHeight() / 2);
		int zone2X = (int) (xCenter -(Card.getWidth() * 1.5f));
		int zone2Y = yCenter - (Card.getHeight() / 2);
		int zone3X = xCenter -(Card.getWidth() / 2);
		int zone3Y = (int) (yCenter - (Card.getHeight() * 1.5f));
		int zone4X = xCenter + (Card.getWidth() / 2);
		int zone4Y = yCenter - (Card.getHeight() / 2);

		//Calculate HandZones
		//TODO fix these
		//Zone 1
		int handZone1X = 0;
		int handZone1Y = this.getHeight() - Card.getHeight();
		int handZone1Width = this.getWidth();
		//Zone 2
		int handZone2X = 0;
		int handZone2Y = 0;
		int handZone2Width = this.getHeight();
		//Zone 3
		int handZone3X = this.getWidth() - Card.getWidth();
		int handZone3Y = 0;
		int handZone3Width = this.getWidth();
		//Zone 4
		int handZone4X = this.getWidth() - Card.getWidth();
		int handZone4Y = this.getHeight() - Card.getHeight();
		int handZone4Width = this.getHeight();

		//Initialize players
		Computer computer1 = new Computer("Computer 1");
		Computer computer2 = new Computer("Computer 2");
		Computer computer3 = new Computer("Computer 3");
		Player player = new Player("Player");

		//Set up partners
		computer1.setPartner(computer3);
		computer3.setPartner(computer1);
		player.setPartner(computer2);
		computer2.setPartner(player);

		player.setPlayZone(zone1X, zone1Y);
		computer1.setPlayZone(zone2X, zone2Y);
		computer2.setPlayZone(zone3X, zone3Y);
		computer3.setPlayZone(zone4X, zone4Y);

		int[] handZone1Offset = {(this.getWidth() - (13 * Card.getWidth() / 2)) / 2, 0};
		int[] handZone2Offset = {0, (this.getHeight() - (13 * Card.getWidth() / 2)) / 2};
		int[] handZone3Offset = {-(this.getWidth() - (13 * Card.getWidth() / 2)) / 2, 0};
		int[] handZone4Offset = {0, -(this.getHeight() - (13 * Card.getWidth() / 2)) / 2};

		int[] handZone1OffsetModifier = {Card.getWidth() / 2, 0};
		int[] handZone2OffsetModifier = {0, Card.getWidth() / 2};
		int[] handZone3OffsetModifier = {-(Card.getWidth() / 2), 0};
		int[] handZone4OffsetModifier = {0, -(Card.getWidth() / 2)};

		player.setHandZone(handZone1X, handZone1Y, handZone1Offset, handZone1OffsetModifier, handZone1Width);
		computer1.setHandZone(handZone2X, handZone2Y, handZone2Offset, handZone2OffsetModifier, handZone2Width);
		computer2.setHandZone(handZone3X, handZone3Y, handZone3Offset, handZone3OffsetModifier, handZone3Width);
		computer3.setHandZone(handZone4X, handZone4Y, handZone4Offset, handZone4OffsetModifier, handZone4Width);




		//Add players to array of players
		players.clear();
		players.add(player);
		players.add(computer1);
		players.add(computer2);
		players.add(computer3);

		deck.setPosition(this.getWidth()/2, this.getHeight()/2);
		boolean lock = false;
		while(!lock) {
			lock = deck.checkPositionReady();
		}
		//Deal cards
		while(!deck.isEmpty()) {
			for(Player p : players) {
				p.addCard(deck.drawCard());
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		//Sort hands
		for(Player p : players) {
			p.sortHand();
			System.out.println(p.getHand());
			p.calcAndApplyOffsets();
		}
	}

	public void initGameThread() {
		gameThread = new Thread(this);
		gameThread.start();
	}

	public void playSpades() {
		initGame();
		for(int i = 0; i < 13; i++) {
			Card highest = null;
			Suit suit = null;
			for(Player p : players) {
				Card playedCard = p.playCard(highest, suit, spadesBroken, this);
				if(playedCard.getSuit() == Suit.SPADE && !spadesBroken) {
					spadesBroken = true;
				}
				System.out.println(p.getName() + " played " + playedCard);
				if(highest == null || ((playedCard.getValue() > highest.getValue()) &&
						(playedCard.getSuit() == suit || playedCard.getSuit() == Suit.SPADE))) {
					highest = playedCard;
				}
				if(suit == null) {
					suit = playedCard.getSuit();
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println("Winner is: " + highest.getOwner().getName());
			while(players.getFirst() != highest.getOwner()) {
				Player temp = players.removeFirst();
				players.add(temp);
			}
			for(Player p : players) {
				p.setSelectedCard(null);
				System.out.println(p.getName());
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void run() {
		double drawTime = 1000000000/FPS;
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		while(gameThread != null) {
			currentTime = System.nanoTime();
			delta += (currentTime - lastTime) / drawTime;
			lastTime = currentTime;
			if(delta >= 1) {
				//Update
				update();
				//Draw
				repaint();
				delta--;
			}
		}
	}

	public void update() {
		deck.update();
		for(Player player : players) {
			player.update();
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		deck.draw(g2);
		for(Player player : players) {
			player.draw(g2);
		}
		g2.dispose();
	}
}
