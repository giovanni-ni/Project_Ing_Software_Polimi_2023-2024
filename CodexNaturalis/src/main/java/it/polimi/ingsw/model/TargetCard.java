package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents an abstract TargetCard in the game.
 * A TargetCard has properties such as ID, base points, and a flag indicating if it's a common target card.
 * Subclasses must implement the methods to check goals and count points.
 */
public abstract class TargetCard implements Serializable {

	// Attributes

	/**
	 * The unique identifier of the card.
	 */
	private int idCard;

	/**
	 * The base points of the card.
	 */
	private int basePoint;

	/**
	 * Flag indicating if the card is a common card.
	 */
	private boolean ifCommon;

	// Constructors

	/**
	 * Constructs a new TargetCard with default values.
	 */
	public TargetCard() {

	}

	/**
	 * Constructs a new TargetCard with the specified properties.
	 *
	 * @param idCard    the unique identifier of the card
	 * @param basePoint the base points of the card
	 * @param ifCommon  flag indicating if the card is a common card
	 */
	public TargetCard(int idCard, int basePoint, boolean ifCommon) {
		this.idCard = idCard;
		this.basePoint = basePoint;
		this.ifCommon = ifCommon;
	}

	// Methods

	/**
	 * Gets the base points of this TargetCard.
	 *
	 * @return the base points
	 */
	public int getbasePoint() {
		return this.basePoint;
	}


	/**
	 * Sets the base points of this TargetCard.
	 *
	 * @param basePoint the base points to set
	 */
	public void setbasePoint(int basePoint) {
		this.basePoint = basePoint;
	}

	/**
	 * Gets the unique identifier of this TargetCard.
	 *
	 * @return the unique identifier
	 */
	public int getIdCard() {
		return idCard;
	}

	/**
	 * Sets the unique identifier of this TargetCard.
	 *
	 * @param idCard the unique identifier to set
	 */
	public void setIdCard(int idCard) {
		this.idCard = idCard;
	}

	/**
	 * Checks if this TargetCard is a common card.
	 *
	 * @return true if the card is common, false otherwise
	 */
	public boolean isIfCommon() {
		return ifCommon;
	}

	/**
	 * Sets the flag indicating if this TargetCard is a common card.
	 *
	 * @param ifCommon the flag to set
	 */
	public void setIfCommon(boolean ifCommon) {
		this.ifCommon = ifCommon;
	}

	/**
	 * Abstract method to check the goal of the card on the specified board.
	 * Subclasses must implement this method.
	 *
	 * @param board the board to check
	 * @return an integer representing the goal check result
	 * @see Board
	 */
	public abstract int checkGoal(Board board);

	/**
	 * Abstract method to count the points of the card on the specified board.
	 * Subclasses must implement this method.
	 *
	 * @param board the board to count points on
	 * @return an integer representing the points counted
	 * @see Board
	 */
	public abstract int countPoint(Board board);
}
