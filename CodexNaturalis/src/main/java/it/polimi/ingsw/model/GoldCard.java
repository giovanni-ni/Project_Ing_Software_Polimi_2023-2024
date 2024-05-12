package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.Map;

public class GoldCard extends ResourceCard implements Serializable {

	private int nReqElement;

	private Elements secondElement;

	private TypeBonus type;

	public GoldCard() {
		super();
	}

	public GoldCard(int code, boolean isFront, Map<CornerPosition, Elements> corners) {
		super(code, isFront, corners);
	}

	public GoldCard(int code, boolean isFront, Map<CornerPosition, Elements> corners, Elements kingdom, int basePoint, int nReqElement, Elements secondElem, TypeBonus bonus) {
		super(code, isFront, corners, kingdom, basePoint);
		this.nReqElement = nReqElement;
		this.secondElement = secondElem;
		this.type = bonus;
	}


	public int getNreqElement() {
		return nReqElement;
	}

	public void setNreqElement(int nreqElement) {
		this.nReqElement = nreqElement;
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

	@Override
	public boolean checkRequirements(Board board) {

		if (this.secondElement!=Elements.HIDE && this.secondElement!=Elements.EMPTY) {
			if (!board.getCounterOfElements().containsKey(this.secondElement) || board.getCounterOfElements().get(this.secondElement) == 0) {
				return false;
			}
		}

        return this.nReqElement <= board.getCounterOfElements().get(super.getKingdom()) && board.getCounterOfElements().containsKey(this.getKingdom());
    }
	public int goalCount(Board board){
		int count=0;
		Coordinate xy = board.getCoordinate(this);
        switch (type) {
            case HIDECORNER -> count = board.numCardsAbout(xy.getX(), xy.getY());
            case COUNTELEMENT_I -> {
				if (board.getCounterOfElements().containsKey(Elements.INK))
					count = board.getCounterOfElements().get(Elements.INK);
            }
            case COUNTELEMENT_F -> {
				if (board.getCounterOfElements().containsKey(Elements.FEATHER))
					count = board.getCounterOfElements().get(Elements.FEATHER);
            }
            case COUNTELEMENT_P -> {
				if (board.getCounterOfElements().containsKey(Elements.PARCHMENT))
					count = board.getCounterOfElements().get(Elements.PARCHMENT);
            }
            case DIRECTPOINT -> count=1;
        }
		return count;
	}
	public int getGoalPoint(Board board){
		int time=goalCount(board);
		return time*getBasePoint();
	}

}
