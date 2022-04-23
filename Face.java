/* TEAM: William Miller and Mike Stevens
   DATE: 4/22/2022
   ABOUT: enum class to model the "face" of each card, used for the Card
   object.
 */

public enum Face {
	ACE("Ace"), TWO("Two"), THREE("Three"), FOUR("Four"),
	FIVE("Five"), SIX("Six"), SEVEN("Seven"), EIGHT("Eight"),
	NINE("Nine"), TEN("Ten"), JACK("Jack"), QUEEN("Queen"),
	KING("King");
	
	private final String face;
	private static final Face[] faceList = Face.values();
	
	Face(final String face) {
		this.face = face;
	}
	
	public static Face getFace(int i) {
		return faceList[i];
	}
	
	@Override
	public String toString() {
		return face;
	}
}
