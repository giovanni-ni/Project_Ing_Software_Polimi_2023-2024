package it.polimi.ingsw.module;

import java.util.Map;

public class GoldCard extends ResourceCard {

	private int n_reqElement;

	private Elements secondElement;

	private TypeBonus type;

	public GoldCard() {
		super();
	}

	public GoldCard(int code, boolean isFront) {
		super(code, isFront);
	}

	public GoldCard(int code, boolean isFront, Elements kingdom, int basePoint, int nReqElement, Elements secondElem, TypeBonus bonus) {
		super(code, isFront, kingdom, basePoint);
		this.n_reqElement = nReqElement;
		this.secondElement = secondElem;
		this.type = bonus;
	}


	public int getN_reqElement() {
		return n_reqElement;
	}

	public void setN_reqElement(int n_reqElement) {
		this.n_reqElement = n_reqElement;
	}

	public Elements getSecondElement() {
		return secondElement;
	}

	public TypeBonus getType() {
		return type;
	}

	public void setType(TypeBonus type) {
		this.type = type;
	}

	public void setSecondElement(Elements secondElement) {
		this.secondElement = secondElement;
	}

	public boolean checkRequirements(Board board) {

		if(board.getCounterOfElements().get(this.secondElement) == null ) {
			return false;
		}

		if(this.n_reqElement < board.getCounterOfElements().get(super.getKingdom())) {
			return false;
		}

		return true;
	}

}
