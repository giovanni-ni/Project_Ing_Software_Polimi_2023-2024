package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.Map;
// todo javadoc
public class ResourceCard extends Card implements Serializable {

	private Elements kingdom;

	private int basePoint;

	public ResourceCard(){
		super();
	}

	public ResourceCard(int code, boolean isFront, Map<CornerPosition, Elements> corners) {
		super(code, isFront, corners);
	}

	public ResourceCard(int code, boolean isFront, Map<CornerPosition, Elements> corners, Elements elem, int basePoint) {
		super(code, isFront,corners);
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

	public boolean checkRequirements(Board board) {
		return true;
	}

}
