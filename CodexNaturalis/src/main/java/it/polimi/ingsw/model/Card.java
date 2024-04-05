package it.polimi.ingsw.model;


import java.util.Map;

public class Card {

	private int code;
	private boolean isFront;
	public Map<CornerPosition, Elements> corners;

	public Card() {

	}

	public Card(int code, boolean isFront, Map<CornerPosition, Elements> corners) {
		this.code = code;
		this.isFront = isFront;
		this.corners = corners;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public boolean getIsFront() {
		return isFront;
	}

	public void setFront(boolean front) {
		isFront = front;
	}

	public Map<CornerPosition, Elements> getCorners() {
		return corners;
	}

	public void setCorners(Map<CornerPosition, Elements> corners) {
		this.corners = corners;
	}

	public boolean isGoldCard(Card c){ /*those booleans would help if some code need to check the requirements that only gold card has*/
		return c instanceof GoldCard;
	} //*watch enum CardType

	public boolean isResourceCard(Card c){
		return c instanceof ResourceCard;
	}

	public boolean isInitialCard(Card c){
		return c instanceof InitialCard;
	}


}
