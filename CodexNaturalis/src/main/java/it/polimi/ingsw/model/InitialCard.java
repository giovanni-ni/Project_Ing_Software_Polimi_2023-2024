package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents an InitialCard in the game, which extends {@link Card}.
 * An InitialCard has additional properties such as central elements and corner elements for both front and back sides.
 */
public class InitialCard extends Card implements Serializable {

	// Attributes

	/**
	 * The central elements associated with this InitialCard.
	 * Uses {@link Elements} to define the type of elements.
	 */
	private ArrayList<Elements> centralElements;

	/**
	 * The elements at the corners of the card when facing front.
	 * Uses {@link CornerPosition} as keys and {@link Elements} as values.
	 */
	private Map<CornerPosition, Elements> cornersFront;

	/**
	 * The elements at the corners of the card when facing back.
	 * Uses {@link CornerPosition} as keys and {@link Elements} as values.
	 */
	private Map<CornerPosition,Elements> cornersBack;

	// Constructors

	/**
	 * Constructs a new InitialCard with default values.
	 */
	public InitialCard(){
		super();
	}


	/**
	 * Constructs a new InitialCard with the specified code and orientation.
	 *
	 * @param code    the unique code of the card
	 * @param isFront the orientation of the card
	 */
	public InitialCard(int code, boolean isFront) {
		super(code, isFront,null);
	}

	/**
	 * Constructs a new InitialCard with the specified properties.
	 *
	 * @param code        the unique code of the card
	 * @param isFront     the orientation of the card
	 * @param corners     the elements at the corners of the card
	 * @param elem        the central elements of the card
	 */
	public InitialCard(int code, boolean isFront, Map<CornerPosition, Elements> corners, ArrayList<Elements> elem) {
		super(code, isFront,corners);
		this.centralElements=elem;

	}

	/**
	 * Constructs a new InitialCard with the specified properties including front and back corner elements.
	 *
	 * @param code        the unique code of the card
	 * @param isFront     the orientation of the card
	 * @param corners     the elements at the corners of the card
	 * @param elem        the central elements of the card
	 * @param cornersFront the elements at the corners of the card when facing front
	 * @param cornersBack  the elements at the corners of the card when facing back
	 */
	public InitialCard(int code, boolean isFront, Map<CornerPosition, Elements> corners, ArrayList<Elements> elem,Map<CornerPosition, Elements> cornersFront,Map<CornerPosition, Elements> cornersBack) {
		super(code, isFront,corners);
		this.centralElements=elem;
		this.cornersFront=cornersFront;
		this.cornersBack=cornersBack;

	}


	// Methods

	/**
	 * Gets the elements at the corners of the card when facing front.
	 *
	 * @return a map of corner positions and their corresponding elements for the front side
	 */
	public Map<CornerPosition, Elements> getCornersFront() {
		return cornersFront;
	}

	/**
	 * Sets the elements at the corners of the card when facing front.
	 *
	 * @param cornersFront a map of corner positions and their corresponding elements to set for the front side
	 */
	public void setCornersFront(Map<CornerPosition, Elements> cornersFront) {
		this.cornersFront = cornersFront;
	}

	/**
	 * Gets the elements at the corners of the card when facing back.
	 *
	 * @return a map of corner positions and their corresponding elements for the back side
	 */
	public Map<CornerPosition, Elements> getCornersBack() {
		return cornersBack;
	}

	/**
	 * Sets the elements at the corners of the card when facing back.
	 *
	 * @param cornersBack a map of corner positions and their corresponding elements to set for the back side
	 */
	public void setCornersBack(Map<CornerPosition, Elements> cornersBack) {
		this.cornersBack = cornersBack;
	}

	/**
	 * Sets the corner elements based on the orientation of the card.
	 * If the card is facing front, sets the corners to {@link #cornersFront}.
	 * If the card is facing back, sets the corners to {@link #cornersBack}.
	 */
	public void setSide(){
		if (super.getIsFront()){
			corners = cornersFront;

		}else{
			corners=cornersBack;
		}
	}

	/**
	 * Gets the central elements associated with this InitialCard.
	 *
	 * @return the central elements
	 */
	public ArrayList<Elements> getCentralElements() {
		return centralElements;
	}

	/**
	 * Sets the central elements associated with this InitialCard.
	 *
	 * @param centralElements the central elements to set
	 */
	public void setCentralElements(ArrayList<Elements> centralElements) {
		this.centralElements = centralElements;
	}
}
