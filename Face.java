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
