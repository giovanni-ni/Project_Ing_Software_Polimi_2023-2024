package it.polimi.ingsw.model;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Board of the player which places his/her card
 * it is initialized with an initial Card, it is unique for each player in the game.
 * @author: Gong, Huang, Ni, Gu
 */
public class Board implements Serializable {

	private BiMap<Card,Coordinate> cardCoordinate;

    private Map<Elements, Integer> counterOfElements;

	private ArrayList<Integer> exists;

	public Board(){

	}

	/**
	 * Constructor of the board of each player at the start of the game
	 * @author: Gong
	 * @param i:Initial card which would be place at the central position of the board;
	 		isFront: boolean which represent the face of the card
	 * @exception: WrongCentralElementException: the centralElement is not a natural element
	 */
	public Board(InitialCard i) {
		/*no Exception handle*/
		Map<Elements, Integer> counter = new HashMap<>();
		setCounterOfElements(counter);
		/*put the initial card*/
		i.setSide();
		addAllElements(i);
		BiMap<Card,Coordinate> cardCoordinate = HashBiMap.create();
		Coordinate startXY = new Coordinate(0,0);
		cardCoordinate.put(i,startXY);
		setCardCoordinate(cardCoordinate);
		ArrayList<Integer> exists = new ArrayList<>();
		setExists(exists);
		this.exists.add(i.getCode());
    }


	/**
	 * Add the card on the board and count the new elements showed up and deletes the covered ones.
	 * This method is used only if the check method is called first and returns true
	 * @author: Gong
	 * @param input: the card which would be put in the board;front: boolean which indicates the face of the card decided by de player; x:the coordinate x of the position desired; y:coordinate y desired
	 * @exception: NegativeCoordinateException: when the value of x or y or both is negative; HighCoordinateException: the value is too high for the board
	 */
	public boolean addCard(ResourceCard input,int x, int y) {
		/* no Exception handle*/
		if (!input.checkRequirements(this)){
			return false;
		}
		if (input.getIsFront()){
			addAllElements(input);
		}else{
			addElement(input.getKingdom());
		}
		deleteCoveredElements(x,y);
		cardCoordinate.put(input, new Coordinate(x,y));
		exists.add(input.getCode());
		return true;
	}


	/**
	 * Check about the coordinates and the coordinate, if there are all free empty corners it returns true, otherwise false
	 * This method is used by the player class to check the availability of the board
	 * @author: Gong
	 * @return: returns a boolean, if there are all free empty corners, and it is a free slot it returns true, otherwise false
	 * @param x:the coordinate x of the position desired; y:coordinate y desired
	 * @exception: NegativeCoordinateException: when the value of x or y or both is negative; HighCoordinateException: the value is too high for the board
	 */
	public boolean check(int x, int y) {
		/* no Exception handle*/
		if (numCardsAbout(x,y)==0){
			return false;
		}
		if (checkCorner(x,y,CornerPosition.UPRIGHT) && !isCardCoordinate(x,y))
			if (checkCorner(x,y,CornerPosition.UPLEFT))
				if (checkCorner(x,y,CornerPosition.DOWNRIGHT))
                    return checkCorner(x, y,CornerPosition.DOWNLEFT);
		return false;
	}


	/**
	 * Check selected side of the coordinate , if there is a free empty corner it returns true, otherwise false
	 * This method is used by the check method to check the availability of a side of the coordinate
	 * @author: Gong
	 * @return  returns a boolean, if there is a free empty corners
	 * @param x the coordinate x of the position desired;
	 * @param y coordinate y desired
	 * @exception: NegativeCoordinateException: when the value of x or y or both is negative; HighCoordinateException: the value is too high for the board
	 */


	public boolean checkCorner(int x, int y, CornerPosition corner) {
		/* no Exception handle*/
        switch (corner) {
            case UPLEFT -> {
				if (!isCardCoordinate(x-1,y+1))
					return true;
				return Elements.HIDE != cardCoordinate.inverse().get(new Coordinate(x-1,y+1)).getCorners().get(CornerPosition.DOWNRIGHT);
            }
            case UPRIGHT -> {
				if (!isCardCoordinate(x+1,y+1))
					return true;
				return Elements.HIDE != cardCoordinate.inverse().get(new Coordinate(x+1,y+1)).getCorners().get(CornerPosition.DOWNLEFT);
            }
            case DOWNLEFT -> {
				if (!isCardCoordinate(x-1,y-1))
					return true;
				return Elements.HIDE != cardCoordinate.inverse().get(new Coordinate(x-1,y-1)).getCorners().get(CornerPosition.UPRIGHT);
            }

        }
		if (!isCardCoordinate(x+1,y-1))
			return true;
		return Elements.HIDE != cardCoordinate.inverse().get(new Coordinate(x+1,y-1)).getCorners().get(CornerPosition.UPLEFT);

    }



	/**
	 * method used for adding the number of elements present in the board of the player
	 * @author: Gong
	 * @param element: the element which number has to be increased;value: the value of the element that has to be increased or decreased
	 * @exception: NegativeValueOfElements: when after changing the value of the count of elements it became negative
	 */
	public void addElement(Elements element, Integer value){

		/* no Exception handle*/
		if (element != Elements.HIDE && element!=Elements.EMPTY) {

			if (counterOfElements.containsKey(element)) {
				value += counterOfElements.get(element);
			}
			counterOfElements.put(element,value);
		}
	}


	/**
	 * increase the number of the element of 1
	 * @author: Gong
	 * @param element: the element which number has to be increased of 1;
	 * @exception: NegativeValueOfElements: when after changing the value of the count of elements it became negative
	 */
	public void addElement(Elements element){
		/* no Exception handle*/
		Integer value=1;
		if (element != Elements.HIDE && element!=Elements.EMPTY) {

			if (counterOfElements.containsKey(element)) {
				value += counterOfElements.get(element);
			}
			counterOfElements.put(element,value);
		}
	}


	/**
	 * Add all the elements present in corners of the card selected
	 * This method is used by the method addCard, remember that a new card placed also causes elements covered so is suggested to call also the method deleteCoveredElements
	 * @author: Gong
	 * @return: void
	 * @param card:card with all elements that would be added in the counter of elements
	 * @exception: nothing for now....
	 */
	public void addAllElements(Card card){
		/* no Exception handle*/

		for (CornerPosition corner : CornerPosition.values()) {
			addCornerElement(card, corner);
		}
		if(card.isInitialCard() && card.getIsFront()){
			InitialCard card1 = (InitialCard) card;
			for (Elements e : card1.getCentralElements()){
				addElement(e);
			}
		}

	}


	/**
	 * Add  the upright element present in that corner of the card selected
	 * This method is used by the method addCorners,
	 * it can also be called to add only an element of the corner indicated
	 * Remember that a new card placed also causes elements covered so is suggested to call also the method deleteCoveredElements
	 * @author: Gong
	 * @return: void
	 * @param card with the corner selected and its element that would be added in the counter of elements
	 * @exception: nothing for now....
	 */
	public void addCornerElement(Card card, CornerPosition corner){
		/* no Exception handle*/
        switch (corner) {
            case UPLEFT -> {
				Elements element= card.getCorners().get(CornerPosition.UPLEFT);
				addElement(element);
            }
            case UPRIGHT -> {
				Elements element= card.getCorners().get(CornerPosition.UPRIGHT);
				addElement(element);
            }
            case DOWNLEFT -> {
				Elements element= card.getCorners().get(CornerPosition.DOWNLEFT);
				addElement(element);
            }
            default -> {
                Elements element = card.getCorners().get(CornerPosition.DOWNRIGHT);
                addElement(element);
            }
        }

	}







	/**
	 * Delete from the counter all the covered element in that coordinate there is a card placed
	 * This method is used normally after the method addCorners and add Card.
	 * @author: Gong
	 * @return: void
	 * @param x:the coordinate x of the position desired; y:coordinate y desired
	 * @exception: NegativeCoordinateException: when the value of x or y or both is negative; HighCoordinateException: the value is too high for the board
	 */
	public void deleteCoveredElements(int x, int y){
		for (CornerPosition corner : CornerPosition.values()) {
			deleteCoveredByCorner(x,y,corner);
		}
	}


	/**
	 * Delete from the  counter  element covered in the coordinate and selected corner where is a card  placed
	 * This method is used normally used in deleteCoveredElements or after the method addCorners and add Card if there is a single corner covered.
	 * @author: Gong
	 * @return: void
	 * @param x:the coordinate x of the position desired; y:coordinate y desired
	 * @exception: NegativeCoordinateException: when the value of x or y or both is negative; HighCoordinateException: the value is too high for the board
	 */
	public void deleteCoveredByCorner(int x, int y,CornerPosition corner){
        switch (corner) {
            case UPLEFT -> {
				if (isCardCoordinate( x-1, y+1)){
					Elements element=cardCoordinate.inverse().get(new Coordinate(x-1,y+1)).getCorners().get(CornerPosition.DOWNRIGHT);
					addElement(element,-1);
				}
			}
			case UPRIGHT -> {
				if (isCardCoordinate( x+1, y+1)){
					Elements element=cardCoordinate.inverse().get(new Coordinate(x+1,y+1)).getCorners().get(CornerPosition.DOWNLEFT);
					addElement(element,-1);
				}
			}
			case DOWNLEFT -> {
				if (isCardCoordinate( x-1, y-1)){
					Elements element=cardCoordinate.inverse().get(new Coordinate(x-1,y-1)).getCorners().get(CornerPosition.UPRIGHT);
					addElement(element,-1);
				}
			}
			default -> {
				if (isCardCoordinate( x+1, y-1)){
					Elements element=cardCoordinate.inverse().get(new Coordinate(x+1,y-1)).getCorners().get(CornerPosition.UPLEFT);
					addElement(element,-1);
				}
			}
		}

	}

	/**
	 * Count the number of cards about the coordinate
	 * Is implemented mainly to count the covered card by the gold card with the bonusType of HideCorners
	 * @author: Gong
	 * @return: int count: quantity of cards about the coordinate
	 * @param x:the coordinate x of the position desired; y:coordinate y desired
	 * @exception: NegativeCoordinateException: when the value of x or y or both is negative;
	 * HighCoordinateException: the value is too high for the board;CountZeroException:impossible case which in the card has no cards about
	 */

	public int numCardsAbout(int x, int y){
		int count=0;

		if (isCardCoordinate(x+1,y+1))
			count++;
		if (isCardCoordinate(x+1,y-1))
			count++;
		if (isCardCoordinate(x-1,y+1))
			count++;
		if (isCardCoordinate(x-1,y-1))
			count++;

		return count;
	}

	/**
	 * Check if there is a Card in coordinates given
	 * @author: Gong
	 * @return: boolean:true if there is a card, otherwise false
	 * @param x:the coordinate x of the position desired; y:coordinate y desired
	 * @exception: NegativeCoordinateException: when the value of x or y or both is negative; HighCoordinateException: the value is too high for the board
	 */
	public boolean isCardCoordinate(int x, int y){
		return cardCoordinate.containsValue(new Coordinate(x,y));
	}

	public void setCounterOfElements(Map<Elements, Integer> counterOfElements) {
		this.counterOfElements = counterOfElements;

	}

	public Map<Elements, Integer> getCounterOfElements() {
		return counterOfElements;
	}

	public ArrayList<Integer> getExists() {
		return exists;
	}

	public BiMap<Card, Coordinate> getCardCoordinate() {
		return cardCoordinate;
	}

	public void setCardCoordinate(BiMap<Card, Coordinate> cardCoordinate) {
		this.cardCoordinate = cardCoordinate;
	}

	public void setExists(ArrayList<Integer> exists) {
		this.exists = exists;
	}
	public Card getCardInBoard(int x, int y){
		Card card = cardCoordinate.inverse().get(new Coordinate(x,y));
		return card;
	}
	public Coordinate getCoordinate (Card card){
		Coordinate xy = cardCoordinate.get(card);
		return xy;
	}
	public Coordinate getCoordinate (int cardCode){
		Card theCard = new Card();
		for (Card card : cardCoordinate.keySet()){
			if (card.getCode()==cardCode){
				theCard=card;
			}
		}
		return cardCoordinate.get(theCard);

	}
}
