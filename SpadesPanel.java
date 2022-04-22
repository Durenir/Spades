package Project;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Deque;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedDeque;

import javax.swing.*;

import HW6.Student;

public class SpadesPanel extends JPanel implements Runnable{

	Thread gameThread;
	int FPS = 60;
	ConcurrentLinkedDeque<Player> players = new ConcurrentLinkedDeque<Player>();
	Deck deck;
	boolean spadesBroken;
	public static boolean reset;
	public static boolean save;
	public static boolean load;
	private int team1TotalScore;
	private int team2TotalScore;
	private int team1TotalBags;
	private int team2TotalBags;


	public SpadesPanel() {
		this.setPreferredSize(new Dimension(750, 750));
		this.setBackground(new Color(71, 113, 72));
		this.setDoubleBuffered(true);
		this.setFocusable(true);
		//Initialize deck
		deck = new Deck();
		deck.shuffle();
	}

	public void initGame() {
		spadesBroken = false;
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
		Computer computer1 = new Computer("Mike");
		Computer computer2 = new Computer("Bill (Partner)");
		Computer computer3 = new Computer("Lisa");
		final Player player = new Player("You");

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

        // bid zone 1 -- lower left corner
        int bidZone1X = Card.getWidth() + 10;
        int bidZone1Y = (this.getHeight()/2) - 37;

        // bid zone 2 -- upper left corner
        int bidZone2X = this.getWidth()/2 - 50;
        int bidZone2Y = Card.getHeight() + 10;

        // bid zone 3 -- upper right corner
        int bidZone3X = this.getWidth() - Card.getWidth() - 110;
        int bidZone3Y = (this.getHeight()/2) - 37;

        // bid zone 4 -- lower right corner
        int bidZone4X = (this.getWidth()/2) - 50;
        int bidZone4Y = this.getHeight() - Card.getHeight() - 85;

        computer1.setScoreZone(bidZone1X, bidZone1Y);
        computer2.setScoreZone(bidZone2X, bidZone2Y);
        computer3.setScoreZone(bidZone3X, bidZone3Y);
        player.setScoreZone(bidZone4X, bidZone4Y);


		//Add players to array of players
		players.clear();
		players.add(player);
		players.add(computer1);
		players.add(computer2);
		players.add(computer3);
	}

	private boolean bidLock() {
		for(Player p : players) {
			if(p.getBid() == 0) {
				return true;
			}
		}
		return false;
	}

	public void initGameThread() {
		System.out.println("Starting thread");
		gameThread = new Thread(this);
		gameThread.start();
	}

	public void setupRound() {
		System.out.println("Starting set up");
		spadesBroken = false;
		deck = new Deck();
		deck.shuffle();
		deck.setPosition((this.getWidth()/2) - Card.getWidth()/2, (this.getHeight()/2) - Card.getHeight()/2);
		boolean lock = false;
		while(!lock) {
			lock = deck.checkPositionReady();
		}
		while(players.getFirst().getName() != "You") {
			Player temp = players.removeFirst();
			players.add(temp);
		}
		for(Player p : players) {
			p.reset();
			p.getHand().clear();
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
		for(Player p : players) {
			lock = true;
			while(lock) {
				lock = p.waitForCards();
			}
		}
		//Sort hands
		for(Player p : players) {
			p.sortHand();
			System.out.println(p.getHand());
			p.calcAndApplyOffsets();
		}
		for(Player p : players) {
			p.getBidInput(this);
		}
		System.out.println("Finished set up");
	}
	public void playSpades() {
		while(!reset && this.team1TotalScore < 500 && this.team2TotalScore < 500) {
		setupRound();
		// while score < 500, continue to play

		boolean bidLock = true;
		while(bidLock && !save && !load && !reset) {
			bidLock = bidLock();
		}
		if(save) {
			saveAndQuit();
		}
		// for-loop control a round, one round has 13 plays
		for(int i = 0; i < 13; i++) {
			if(reset) { break; }
			Card highest = null;
			Suit suit = null;
			for(Player p : players) {
				Card playedCard = p.playCard(highest, suit, spadesBroken, this);
				if(playedCard == null) {break;}
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
			if(!reset) {

				System.out.println("Winner is: " + highest.getOwner().getName());

				highest.getOwner().incrementTricks();

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
		if(!reset) {
			for(Player p : players) {
				if(p.getTricks() > p.getBid()) {
					p.setBags(p.getTricks() - p.getBid());
				}
			}
		// *** CALCULATE SCORE *****************************************************
			int team1CombinedBid = players.getFirst().getBid() + players.getFirst().getPartner().getBid();
			int team2CombinedBid = players.getLast().getBid() + players.getLast().getPartner().getBid();

			int team1CombinedTricks = players.getFirst().getTricks() + players.getFirst().getPartner().getTricks();
			int team2CombinedTricks = players.getLast().getTricks() + players.getLast().getPartner().getTricks();

			int team1CombinedBags = team1CombinedTricks - team1CombinedBid;
			int team2CombinedBags = team2CombinedTricks - team2CombinedBid;

			if(team1CombinedBags <= 0) {
				team1CombinedBags = 0;
			}
			team1TotalBags += team1CombinedBags;
			players.getFirst().setTotalBags(players.getFirst().getTotalBags()+ team1TotalBags);
			players.getFirst().getPartner().setTotalBags(players.getFirst().getPartner().getTotalBags() + team1TotalBags);
			players.getFirst().setBags(0);
			players.getFirst().getPartner().setBags(0);

			if(team2CombinedBags <= 0) {
				team2CombinedBags = 0;
			}
			team2TotalBags += team2CombinedBags;
			players.getLast().setTotalBags(players.getLast().getTotalBags() + team2TotalBags);
			players.getLast().getPartner().setTotalBags(players.getLast().getPartner().getTotalBags() + team2TotalBags);
			players.getLast().setBags(0);
			players.getLast().getPartner().setBags(0);


		int team1BidScore;
		int team1BagScore = 0;
		int team1PointsThisRound;

		int team2BidScore;
		int team2BagScore = 0;
		int team2PointsThisRound;

		if(team1CombinedBid > team1CombinedTricks) {
			team1BidScore = -team1CombinedBid * 10;
		} else {
			team1BidScore = team1CombinedBid * 10;
		}

		if(team1CombinedBags > 0) {
			if(team1TotalBags < 10) {
				team1BagScore = team1CombinedBags;
			} else {
				team1BagScore = -100;
			}
		}
		team1PointsThisRound = team1BidScore + team1BagScore;
		team1TotalScore += team1PointsThisRound;

		if(team2CombinedBid > team2CombinedTricks) {
			team2BidScore = -team2CombinedBid * 10;
		} else {
			team2BidScore = team2CombinedBid * 10;
		}

		if(team2CombinedBags > 0) {
			if(team2TotalBags < 10) {
				team2BagScore = team2CombinedBags;
			} else {
				team2BagScore = -100;
			}
		}
		team2PointsThisRound = team2BidScore + team2BagScore;
		team2TotalScore += team2PointsThisRound;


		// *** DISPLAY SCORE AND PROMPT FOR NEXT ROUND OR DISPLAY WINNER ***********
		// at the end of a round, total the score and print to a JDialog box

		// NOTE: ONLY 1 LABEL PER DIALOG BOX -- NEEDS TO CHANGE...
		Window parentWindow = SwingUtilities.windowForComponent(this);
		Frame parentFrame = null;
		if(parentWindow instanceof Frame){
			parentFrame = (Frame) parentWindow;
		}

		final String[] columnNames = {"", players.getFirst().getName() + " and " + players.getFirst().getPartner().getName(),
				players.getLast().getName() + " and " + players.getLast().getPartner().getName()};

		String[][] data = {
		{ "Combined Bid", String.valueOf(team1CombinedBid), String.valueOf(team2CombinedBid)},
		{ "Tricks Taken", String.valueOf(team1CombinedTricks), String.valueOf(team2CombinedTricks)},
		{ "Bags", String.valueOf(team1CombinedBags), String.valueOf(team2CombinedBags)},
		{ "Total Bags", String.valueOf(team1TotalBags), String.valueOf(team2TotalBags)},
		{ "Bid Score", String.valueOf(team1BidScore), String.valueOf(team2BidScore)},
		{ "Bag Score", String.valueOf(team1BagScore), String.valueOf(team2BagScore)},
		{ "Points this round", String.valueOf(team1PointsThisRound), String.valueOf(team2PointsThisRound)},
		{ "Total points", String.valueOf(team1TotalScore), String.valueOf(team2TotalScore) }};

		ScoreFrame scores = new ScoreFrame(columnNames, data);
		scores.display();

		} else {
			System.out.println("Killing thread");
			gameThread.stop();
			gameThread = null;
			this.newGame();
		}
		reset = false;
		}
		EndFrame eFrame = new EndFrame(this, players);
		eFrame.display();
		boolean newGameLock = false;
		while(!newGameLock) {
			newGameLock = reset;
		}
		this.newGame();
	}

	public void loadGame() {
		InputStream is = null;
		try {
			File file2 = new File("SpadesScores.dat");

			is = new FileInputStream(file2);
			is = new BufferedInputStream(is);

			team1TotalBags = is.read();
			System.out.println(team1TotalBags);
			team1TotalScore = is.read();
			System.out.println(team1TotalScore);
			team2TotalBags = is.read();
			team2TotalScore = is.read();
			is.close();
		} catch (FileNotFoundException e) {
			System.out.println("Could not find the file. Please try again.");
			return;
		} catch (IOException e) {
			System.out.println("Could not read the file. Please try again.");
			return;
		}
	}

	public static void loadAndStart() {
		load = true;
		reset = true;
	}

	public void saveAndQuit() {
		OutputStream os;
		try {
			File file = new File("SpadesPlayers.dat");
			File file2 = new File("SpadesScores.dat");
			os = new FileOutputStream(file);
			os = new BufferedOutputStream(os);
			os = new ObjectOutputStream(os);
			for(Player player : players) {
				((ObjectOutputStream) os).writeObject(player);
			}
			os.close();
			os = new FileOutputStream(file2);
			os = new BufferedOutputStream(os);
			os.write(this.team1TotalBags);
			os.write(this.team1TotalScore);
			os.write(this.team2TotalBags);
			os.write(this.team2TotalScore);
			os.close();
		} catch (FileNotFoundException e) {
			System.out.println("Could not find the file. Not found.");
			return;
		} catch (IOException e) {
			System.out.println("Could not write to file. Please try gain." + e);
			return;
		}
		System.exit(0);
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

	public boolean getExitStatus() {
		return reset;
	}

	public static void resetGame() {
		reset = true;
	}

	public static void saveGame() {
		save = true;
	}

	public void newGame(){
		reset = false;
		if(!load) {
			team1TotalScore = 0;
			team2TotalScore = 0;
			team1TotalBags = 0;
			team2TotalBags = 0;
			initGame();
		} else {
			System.out.println("Loading game!");
			loadGame();
		}
		load = false;
		initGameThread();
		playSpades();
	}
}