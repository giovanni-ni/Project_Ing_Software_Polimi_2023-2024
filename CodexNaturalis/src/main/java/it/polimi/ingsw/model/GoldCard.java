package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.Map;

/**
 * Represents a GoldCard in the game, which extends {@link ResourceCard}.
 * A GoldCard has additional properties and methods to determine its requirements and points in the game.
 */
public class GoldCard extends ResourceCard implements Serializable {

	// Attributes

	/**
	 * The number of required elements for this GoldCard.
	 */
	private int nReqElement;

	/**
	 * The second element associated with this GoldCard.
	 * Uses {@link Elements} to define the type of element.
	 */
	private Elements secondElement;

	/**
	 * The type of bonus associated with this GoldCard.
	 * Uses {@link TypeBonus} to define the bonus type.
	 */
	private TypeBonus type;

	// Constructors

	/**
	 * Constructs a new GoldCard with default values.
	 */
	public GoldCard() {
		super();
	}

	/**
	 * Constructs a new GoldCard with the specified code, orientation, and corner elements.
	 *
	 * @param code    the unique code of the card
	 * @param isFront the orientation of the card
	 * @param corners the elements at the corners of the card
	 */
	public GoldCard(int code, boolean isFront, Map<CornerPosition, Elements> corners) {
		super(code, isFront, corners);
	}

	/**
	 * Constructs a new GoldCard with the specified properties.
	 *
	 * @param code        the unique code of the card
	 * @param isFront     the orientation of the card
	 * @param corners     the elements at the corners of the card
	 * @param kingdom     the kingdom element associated with the card
	 * @param basePoint   the base points of the card
	 * @param nReqElement the number of required elements for the card
	 * @param secondElem  the second element associated with the card
	 * @param bonus       the type of bonus associated with the card
	 */
	public GoldCard(int code, boolean isFront, Map<CornerPosition, Elements> corners, Elements kingdom, int basePoint, int nReqElement, Elements secondElem, TypeBonus bonus) {
		super(code, isFront, corners, kingdom, basePoint);
		this.nReqElement = nReqElement;
		this.secondElement = secondElem;
		this.type = bonus;
	}


	// Methods

	/**
	 * Gets the number of required elements for this GoldCard.
	 *
	 * @return the number of required elements
	 */
	public int getNreqElement() {
		return nReqElement;
	}

	/**
	 * Sets the number of required elements for this GoldCard.
	 *
	 * @param nreqElement the number of required elements to set
	 */
	public void setNreqElement(int nreqElement) {
		this.nReqElement = nreqElement;
	}

	/**
	 * Gets the second element associated with this GoldCard.
	 *
	 * @return the second element
	 */
	public Elements getSecondElement() {
		return secondElement;
	}

	/**
	 * Sets the second element associated with this GoldCard.
	 *
	 * @param secondElement the second element to set
	 */
	public void setSecondElement(Elements secondElement) {
		this.secondElement = secondElement;
	}

	/**
	 * Gets the type of bonus associated with this GoldCard.
	 *
	 * @return the type of bonus
	 */
	public TypeBonus getType() {
		return type;
	}

	/**
	 * Sets the type of bonus associated with this GoldCard.
	 *
	 * @param type the type of bonus to set
	 */
	public void setType(TypeBonus type) {
		this.type = type;
	}

	/**
	 * Checks if the requirements for this GoldCard are met on the given board.
	 *
	 * @param board the board to check {@link Board}
	 * @return true if the requirements are met, false otherwise
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
	 * Calculates the goal count based on the type of bonus and the board state.
	 *
	 * @param board the board to evaluate {@link Board}
	 * @return the calculated goal count
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
	 * Calculates the goal points based on the goal count and the base points.
	 *
	 * @param board the board to evaluate {@link Board}
	 * @return the calculated goal points
	 */
	public int getGoalPoint(Board board){
		int time=goalCount(board);
		return time*getBasePoint();
	}

}
