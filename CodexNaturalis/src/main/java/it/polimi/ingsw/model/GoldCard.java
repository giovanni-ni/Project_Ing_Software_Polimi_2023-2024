package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.Map;

public class GoldCard extends ResourceCard implements Serializable {

	private int nReqElement;

	private Elements secondElement;

	private TypeBonus type;


	/**
	 * Constructor of goldCard with the upper class
	 */
	public GoldCard() {
		super();
	}


	/**
	 * Constructor of goldCard with the upper class
	 * @param isFront, the boolean of the back and front status
	 * @param code, the code of the card
	 * @param corners, the map of the corners and the respective element
	 */
	public GoldCard(int code, boolean isFront, Map<CornerPosition, Elements> corners) {
		super(code, isFront, corners);
	}

	/**
	 * Constructor of goldCard with the upper class
	 * @param isFront, the boolean of the back and front status
	 * @param code, the code of the card
	 * @param corners, the map of the corners and the respective element
	 * @param kingdom, the main kingdom Element that the card has
	 * @param basePoint, the integer point that the gold card gives
	 * @param nReqElement, the integer that represents the number of the main elements required
	 * @param secondElem, if it is different from Empty and Hide, it represents the second element required to put the card front
	 * @param bonus, the bonus type that the card can give
	 */
	public GoldCard(int code, boolean isFront, Map<CornerPosition, Elements> corners, Elements kingdom, int basePoint, int nReqElement, Elements secondElem, TypeBonus bonus) {
		super(code, isFront, corners, kingdom, basePoint);
		this.nReqElement = nReqElement;
		this.secondElement = secondElem;
		this.type = bonus;
	}

	/**
	 * Getter of the main kingdom required number
	 * @return : the integer of the main kingdom element that the gold card need to be put wut front size
	 */
	public int getNreqElement() {
		return nReqElement;
	}

	/**
	 * Setter of the main kingdom required number
	 * @param nreqElement : the integer of the main kingdom element that the gold card need to be put with front side
	 */
	public void setNreqElement(int nreqElement) {
		this.nReqElement = nreqElement;
	}

	/**
	 * Getter of the second element required number
	 * @return : the second element that the gold card need to be put with front side
	 */
	public Elements getSecondElement() {
		return secondElement;
	}

	/**
	 * Getter of the type of the gold card bonus
	 * @return : TypeBonus which represents the gold card type of bonus
	 */
	public TypeBonus getType() {
		return type;
	}

	/**
	 * Setter of the type of the gold card bonus
	 * @param type : TypeBonus which represents the gold card type of bonus
	 */
	public void setType(TypeBonus type) {
		this.type = type;
	}

	/**
	 * Setter of the second element required number
	 * @param secondElement : the second element that the gold card need to be put with front side
	 */
	public void setSecondElement(Elements secondElement) {
		this.secondElement = secondElement;
	}

	/**
	 * Check the requirement of the gold card from the elements in the counter of the player board
	 * This method is an override of the resource card method which return always true
	 * @param board : the board of the player that the card would be set
	 * @return : the boolean of the result of the check
	 */
	@Override
	public boolean checkRequirements(Board board) {

		if (this.secondElement!=Elements.HIDE && this.secondElement!=Elements.EMPTY) {
			if (!board.getCounterOfElements().containsKey(this.secondElement) || board.getCounterOfElements().get(this.secondElement) == 0) {
				return false;
			}
		}

        return   board.getCounterOfElements().containsKey(this.getKingdom()) && this.nReqElement <=board.getCounterOfElements().get(super.getKingdom()) ;
    }

	/**
	 * Count the number of the bonus success of the gold card
	 * This method check the bonus type in BonusType Enumeration
	 * @param board : the board that contains the information to check if the goal of the gold card is satisfied or not
	 * @return : the count of bonus success of the gold card
	 */
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
	/**
	 * Calculate the number of the point that the gold card gives
	 * This method check the bonus type in BonusType Enumeration
	 * @param board : the board that contains the information to check if the goal of the gold card is satisfied or not
	 * @return : the total point of the gold card
	 */
	public int getGoalPoint(Board board){
		int time=goalCount(board);
		return time*getBasePoint();
	}

}
