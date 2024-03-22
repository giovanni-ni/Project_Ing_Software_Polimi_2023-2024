package it.polimi.ingsw.module;


import java.util.Map;

public class Card {

	private int Code;

	private boolean isFront;

	public Map<CornerPosition, Elements> corners;


	public boolean isGoldCard(){ /*those booleans would help if some code need to check the requirements that only gold card has*/
		return false;
	} //*watch enum CardType

	public boolean isResourceCard(){
		return false;
	}

	public boolean isInitialCard(){
		return false;
	}

	public boolean isFront() {
		return isFront;
	}
}
