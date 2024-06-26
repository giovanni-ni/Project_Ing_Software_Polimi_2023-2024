package it.polimi.ingsw.model;


import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a card in the game.
 * This class holds the card's code, orientation, and corner elements.
 * It provides methods to manipulate and access these attributes.
 * It also includes methods to check the type of card.
 */
public class Card implements Serializable {

	// Attributes

	/**
	 * The unique code of the card.
	 */
	private int code;

	/**
	 * Indicates whether the card is facing front.
	 */
	private boolean isFront;

	/**
	 * The elements present at the corners of the card.
	 * Uses {@link CornerPosition} as keys and {@link Elements} as values.
	 */
	public Map<CornerPosition, Elements> corners;

	// Constructors

	/**
	 * Constructs a new Card with default values.
	 */
	public Card() {

	}

	/**
	 * Constructs a new Card with the specified code.
	 *
	 * @param code the unique code of the card
	 */
	public Card(int code){
		this.code=code;
	}

	/**
	 * Constructs a new Card with the specified code, orientation, and corner elements.
	 *
	 * @param code    the unique code of the card
	 * @param isFront the orientation of the card
	 * @param corners the elements at the corners of the card
	 */
	public Card(int code, boolean isFront, Map<CornerPosition, Elements> corners) {
		this.code = code;
		this.isFront = isFront;
		this.corners = corners;
	}

	// Methods

	/**
	 * Gets the unique code of the card.
	 *
	 * @return the code of the card
	 */
	public int getCode() {
		return code;
	}


	/**
	 * Sets the unique code of the card.
	 *
	 * @param code the code to set
	 */
	public void setCode(int code) {
		this.code = code;
	}

	/**
	 * Gets the orientation of the card.
	 *
	 * @return true if the card is facing front, false otherwise
	 */
	public boolean getIsFront() {
		return isFront;
	}

	/**
	 * Sets the orientation of the card.
	 *
	 * @param front true to set the card facing front, false otherwise
	 */
	public void setFront(boolean front) {
		isFront = front;
	}

	/**
	 * Gets the elements at the corners of the card.
	 *
	 * @return a map of corner positions and their corresponding elements
	 */
	public Map<CornerPosition, Elements> getCorners() {
		return corners;
	}

	/**
	 * Sets the elements at the corners of the card.
	 *
	 * @param corners a map of corner positions and their corresponding elements to set
	 */
	public void setCorners(Map<CornerPosition, Elements> corners) {
		this.corners = corners;
	}

	/**
	 * Checks if the card is a {@link GoldCard}.
	 *
	 * @return true if the card is a GoldCard, false otherwise
	 */
	public boolean isGoldCard(){ /*those booleans would help if some code need to check the requirements that only gold card has*/
		return this instanceof GoldCard;
	} //*watch enum CardType

	/**
	 * Checks if the card is a {@link ResourceCard}.
	 *
	 * @return true if the card is a ResourceCard, false otherwise
	 */
	public boolean isResourceCard(){
		return this instanceof ResourceCard;
	}

	/**
	 * Checks if the card is an {@link InitialCard}.
	 *
	 * @return true if the card is an InitialCard, false otherwise
	 */
	public boolean isInitialCard(){
		return this instanceof InitialCard;
	}

	/**
	 * Gets the kingdom element associated with the card.
	 *
	 * @return the kingdom element, or null if none
	 */
	public Elements getKingdom() {
		return null;
	}
}
