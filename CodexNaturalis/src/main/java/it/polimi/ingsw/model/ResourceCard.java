package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.Map;

/**
 * Represents a ResourceCard in the game, which extends {@link Card}.
 * A ResourceCard has additional properties such as kingdom and base points.
 */
public class ResourceCard extends Card implements Serializable {

	// Attributes

	/**
	 * The kingdom element associated with this ResourceCard.
	 * Uses {@link Elements} to define the type of kingdom.
	 */
	private Elements kingdom;

	/**
	 * The base points of this ResourceCard.
	 */
	private int basePoint;

	// Constructors

	/**
	 * Constructs a new ResourceCard with default values.
	 */
	public ResourceCard(){
		super();
	}

	/**
	 * Constructs a new ResourceCard with the specified code, orientation, and corner elements.
	 *
	 * @param code    the unique code of the card
	 * @param isFront the orientation of the card
	 * @param corners the elements at the corners of the card
	 */
	public ResourceCard(int code, boolean isFront, Map<CornerPosition, Elements> corners) {
		super(code, isFront, corners);
	}

	/**
	 * Constructs a new ResourceCard with the specified properties.
	 *
	 * @param code       the unique code of the card
	 * @param isFront    the orientation of the card
	 * @param corners    the elements at the corners of the card
	 * @param elem       the kingdom element associated with the card
	 * @param basePoint  the base points of the card
	 */
	public ResourceCard(int code, boolean isFront, Map<CornerPosition, Elements> corners, Elements elem, int basePoint) {
		super(code, isFront,corners);
		this.kingdom = elem;
		this.basePoint = basePoint;
	}

	// Methods

	/**
	 * Gets the kingdom element associated with this ResourceCard.
	 *
	 * @return the kingdom element
	 */
	public Elements getKingdom() {
		return this.kingdom;
	}

	/**
	 * Sets the kingdom element associated with this ResourceCard.
	 *
	 * @param kingdom the kingdom element to set
	 */
	public void setKingdom(Elements kingdom) {
		this.kingdom = kingdom;
	}

	/**
	 * Gets the base points of this ResourceCard.
	 *
	 * @return the base points
	 */
	public int getBasePoint() { /*added for board */
		return this.basePoint;
	}

	/**
	 * Sets the base points of this ResourceCard.
	 *
	 * @param basePoint the base points to set
	 */
	public void setBasePoint(int basePoint) {
		this.basePoint = basePoint;
	}


	/**
	 * Checks if the requirements for this ResourceCard are met on the given board.
	 * By default, this method returns true. Subclasses can override this method to implement specific requirements.
	 *
	 * @param board the board to check
	 * @return true if the requirements are met, false otherwise
	 * @see Board
	 */
	public boolean checkRequirements(Board board) {
		return true;
	}

}
