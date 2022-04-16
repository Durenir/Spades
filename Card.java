package Project;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Card {

	private Suit suit;
	private Face face;
	private int value;
	private Player owner;
	private BufferedImage image;
	private BufferedImage backImage;
	private int[] position;
	private int[] destination;
	private int[] rotation;
	private boolean faceUp;
	private int animSpeed = 10;
	private static final int width = 75;
	private static final int height = 109;
	private Rectangle rec;


	public Card(Suit suit, Face face, String fileName) {
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
		position = new int[2];
		destination = new int[2];
		rotation = new int[2];
		try {
			image = ImageIO.read(getClass().getResource("Cards/" + fileName));
			backImage = ImageIO.read(getClass().getResource("Cards/Image52.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void update() {
		//update positions
		if(position[0] < destination[0]) {
			//move along x axis by given speed. position[0]++;
			position[0] += animSpeed;
			if(position[0] > destination[0]) {
				position[0] = destination[0];
			}
		}
		if(position[0] > destination[0]) {
			//move along x axis by given speed. position[0]--;
			position[0] -= animSpeed;
			if(position[0] < destination[0]) {
				position[0] = destination[0];
			}
		}
		if(position[1] < destination[1]) {
			//move along y axis by given speed. position[1]++;
			position[1] += animSpeed;
			if(position[1] > destination[1]) {
				position[1] = destination[1];
			}
		}
		if(position[1] > destination[1]) {
			//move along y axis by given speed. position[1]--;
			position[1] -= animSpeed;
			if(position[1] < destination[1]) {
				position[1] = destination[1];
			}
		}
	}

	public void draw(Graphics2D g2) {
		//Image, x position, y position, width, height,
		if(faceUp) {
			g2.drawImage(image, position[0], position[1], getWidth(), getHeight(), null);
		} else {
			g2.drawImage(backImage, position[0], position[1], getWidth(), getHeight(), null);
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

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public int[] getPosition() {
		return position;
	}

	public void setPosition(int[] postion) {
		this.position = postion;
	}

	public void setPosition(int x, int y) {
		this.position[0] = x;
		this.position[1] = y;
	}

	public int[] getRotation() {
		return rotation;
	}

	public void setRotation(int[] rotation) {
		this.rotation = rotation;
	}

	public int[] getDestination() {
		return destination;
	}

	public void setDestination(int[] destination) {
		this.destination = destination;
	}

	public void setDestination(int x, int y) {
		this.destination[0] = x;
		this.destination[1] = y;
	}

	public boolean isFaceUp() {
		return faceUp;
	}

	public void setFaceUp(boolean faceUp) {
		this.faceUp = faceUp;
	}

	public int getAnimSpeed() {
		return animSpeed;
	}

	public void setAnimSpeed(int animSpeed) {
		this.animSpeed = animSpeed;
	}

	public static int getWidth() {
		return width;
	}

	public static int getHeight() {
		return height;
	}

	public Rectangle getRec() {
		return rec;
	}

	public void setRec(Rectangle rec) {
		this.rec = rec;
	}

	public String toString() {
		return(getFace().toString() + " of " + getSuit().toString() +
				" has a value of " + getValue());
	}
}
