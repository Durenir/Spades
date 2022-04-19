package Project;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Deque;
import java.util.Enumeration;
import java.util.LinkedList;

import javax.swing.*;

public class SpadesPanel extends JPanel implements Runnable{

	Thread gameThread;
	int FPS = 60;
	Deque<Player> players = new LinkedList<Player>();
	Deck deck;
	boolean spadesBroken;
	public static boolean reset;

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
		Computer computer1 = new Computer("Computer 1");
		Computer computer2 = new Computer("Computer 2");
		Computer computer3 = new Computer("Computer 3");
		final Player player = new Player("Player");

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

		deck.setPosition((this.getWidth()/2) - Card.getWidth()/2, (this.getHeight()/2) - Card.getHeight()/2);
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

		// Enter bid -- only do this for a human player
		System.out.println("Enter bid...");

		/*https://stackoverflow.com/questions/10522121/instantiate-jdialog-from
		-jpanel*/

		Window parentWindow = SwingUtilities.windowForComponent(this);
		Frame parentFrame = null;
		if(parentWindow instanceof Frame){
			parentFrame = (Frame) parentWindow;
		}

		// populate this Dialog box iff there is a human player...
		// NOTE - the game will still execute if this nothing happens here...
		final JDialog dialog = new JDialog(parentFrame, "Bid Input");
		dialog.setLayout(new FlowLayout());

		dialog.setSize(700, 100);
		dialog.setResizable(false);
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);

		JRadioButton rb1, rb2, rb3, rb4, rb5, rb6, rb7, rb8, rb9, rb10, rb11,
						rb12, rb13;
		JButton setBid;

		rb1 = new JRadioButton("1");
		rb1.setBounds(100,5,100,30);

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
		dialog.add(setBid);
		dialog.revalidate();
		dialog.repaint();
		//TODO need to calc computer bids
		computer1.setBid(1);
		computer2.setBid(1);
		computer3.setBid(1);
		setBid.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			    for (Enumeration<AbstractButton> buttons = bg.getElements(); buttons.hasMoreElements();) {
			        AbstractButton button = buttons.nextElement();
			        if (button.isSelected()) {
			        	System.out.println(button.getText() + " was selected");
			        	player.setBid(Integer.parseInt(button.getText()));
			        	dialog.dispose();
			        }
			    }
			}
		});
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
		gameThread = new Thread(this);
		gameThread.start();
	}

	public void playSpades() {

		// initialize game variables
		initGame();
		// while score < 500, continue to play

		boolean bidLock = true;
		while(bidLock) {
			bidLock = bidLock();
			if(reset) {
				//TODO get the dialog component and close it.
			}
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
		// *** CALCULATE SCORE *****************************************************
		int team1TotalTricks =
						players.getFirst().getTricks() + players.getFirst().getPartner().getTricks();

		int team2TotalTricks =
						players.getLast().getTricks() + players.getLast().getPartner().getTricks();


		// *** DISPLAY SCORE AND PROMPT FOR NEXT ROUND OR DISPLAY WINNER ***********
		// at the end of a round, total the score and print to a JDialog box

		// NOTE: ONLY 1 LABEL PER DIALOG BOX -- NEEDS TO CHANGE...
		Window parentWindow = SwingUtilities.windowForComponent(this);
		Frame parentFrame = null;
		if(parentWindow instanceof Frame){
			parentFrame = (Frame) parentWindow;
		}


		final String[] columnNames = {"Player", "Bids", "Tricks", "Bags", "Points",
						"Total Points"};

		String[][] data = {
						{ "Player 1", "2", "3", "1", "4", "9" },
						{ "Player 2", "4", "3", "1", "5", "8" },
						{ "Player 3", "6", "9", "1", "4", "7" },
						{ "Player 4", "8", "1", "1", "5", "6" }};

		// not sure if this will persist between rounds
		ScoreFrame scores = new ScoreFrame(columnNames, data);
		scores.display();

//		JTable j = new JTable(data, columnNames);
//		JScrollPane sp = new JScrollPane(j);
//		parentFrame.add(sp);
//		parentFrame.pack();






//		assert parentFrame != null;
//		parentFrame.add(frame);


		} else {
			System.out.println("Quiting");
			gameThread.stop();
			gameThread = null;
			this.newGame();
		}
		reset = false;
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

	public void newGame(){
		reset = false;
		deck = new Deck();
		deck.shuffle();
		initGameThread();
		playSpades();
	}
}