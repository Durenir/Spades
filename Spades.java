package Project;

import java.util.Deque;
import java.util.LinkedList;

public class Spades {

	public static void main(String[] args) {
//		Computer computer1 = new Computer("Computer 1");
//		Computer computer2 = new Computer("Computer 2");
//		Computer computer3 = new Computer("Computer 3");
//		Player player = new Player("Player");
//		Deck deck = new Deck();
//		boolean spadesBroken = false;
//		deck.shuffle();
//		computer1.setPartner(computer3);
//		computer3.setPartner(computer1);
//		player.setPartner(computer2);
//		computer2.setPartner(player);

//		Deque<Player> players = new LinkedList<Player>();
//		players.add(player);
//		players.add(computer1);
//		players.add(computer2);
//		players.add(computer3);
		while(!deck.isEmpty()) {
			for(Player p : players) {
				p.addCard(deck.drawCard());
			}
		}
		for(Player p : players) {
			p.sortHand();
			System.out.println(p.getHand());
		}


		for(int i = 0; i < 13; i++) {
			Card highest = null;
			Suit suit = null;
			for(Player p : players) {
				Card playedCard = p.playCard(highest, suit, spadesBroken);
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
			}
			System.out.println("Winner is: " + highest.getOwner().getName());
			//set tricks
			//	function also adds to totalTricks
			//set bags
			//	function also adds to totalBags
			//set points
			//	function also adds to totalPoints
			//Set team points, tricks, and bags in this class. TODO create variables for them.
			while(players.getFirst() != highest.getOwner()) {
				Player temp = players.removeFirst();
				players.add(temp);
			}
			for(Player p : players) {
				System.out.println(p.getName());
			}
		}
	}
}
