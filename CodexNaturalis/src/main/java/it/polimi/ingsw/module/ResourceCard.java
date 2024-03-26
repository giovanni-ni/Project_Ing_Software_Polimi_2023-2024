package it.polimi.ingsw.module;

public class ResourceCard extends Card {

	private Elements kingdom;

	private int basePoint;

	public ResourceCard(){
		super();
	}

	public ResourceCard(int code, boolean isFront) {
		super(code, isFront);
	}

	public ResourceCard(int code, boolean isFront, Elements elem, int basePoint) {
		super(code, isFront);
		this.kingdom = elem;
		this.basePoint = basePoint;
	}

	public Elements getKingdom() {
		return this.kingdom;
	}
	public void setKingdom(Elements kingdom) {
		this.kingdom = kingdom;
	}

	public int getBasePoint() { /*added for board */
		return this.basePoint;
	}
	public void setBasePoint(int basePoint) {
		this.basePoint = basePoint;
	}

	public int countPoint() {
		return 0;
	}

}
