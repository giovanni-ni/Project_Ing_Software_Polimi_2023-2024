package it.polimi.ingsw.model;

public class GoldCard extends ResourceCard {

	private int nReqElement;

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
		this.nReqElement = nReqElement;
		this.secondElement = secondElem;
		this.type = bonus;
	}


	public int getN_reqElement() {
		return nReqElement;
	}

	public void setN_reqElement(int n_reqElement) {
		this.nReqElement = n_reqElement;
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

		if(!board.getCounterOfElements().containsKey(this.secondElement) || board.getCounterOfElements().get(this.secondElement)==0 ) {
			return false;
		}

		if(this.nReqElement > board.getCounterOfElements().get(super.getKingdom())) {
			return false;
		}

		return true;
	}
	public int goalCount(Board board){
		int count=0;
		Integer[] coordinate= board.getCoordinate().get(super.getCode());
        switch (type) {
            case HIDECORNER -> {
				count = board.numCardsAbout(coordinate[0],coordinate[1]);
            }
            case COUNTELEMENT_I -> {
				if (board.getCounterOfElements().containsKey(Elements.INK))
					count = board.getCounterOfElements().get(Elements.INK);
            }
            case COUNTELEMENT_F -> {
				if (board.getCounterOfElements().containsKey(Elements.FEATHER))
					count = board.getCounterOfElements().get(Elements.FEATHER);
            }
            case COUNTELEMENT_P -> {
				if (board.getCounterOfElements().containsKey(Elements.FEATHER))
					count = board.getCounterOfElements().get(Elements.PARCHMENT);
            }
            case DIRECTPOINT -> {
				count=1;
            }
        }
		return count;
	}
	public int getGoalPoint(Board board){
		int time=goalCount(board);
		return time*getBasePoint();
	}

}
