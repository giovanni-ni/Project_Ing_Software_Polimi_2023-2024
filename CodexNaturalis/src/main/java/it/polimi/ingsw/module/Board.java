package it.polimi.ingsw.module;
import java.util.Map;


/**
 * Board of the player which places his/her card
 * it is initialized with an initial Card, it is unique for each player in the game.
 * @author: Gong, Huang, Ni, Gu
 */
public class Board {

	private Card[][] myCardBoard; /* what happens if the index is a negative number*/

    private Map<Elements, Integer> counterOfElements;

	/**
	 * Constructor of the board of each player at the start of the game
	 * @author: Gong
	 * @param: i:Initial card which would be place at the central position of the board;
	 		isFront: boolean which represent the face of the card
	 * @exception: WrongCentralElementException: the centralElement is not a natural element
	 */
	public Board(InitialCard i) {
		/*no Exception handle*/
		int maxsize= TypeOfCard.RESOURCECARD.numOfCards+TypeOfCard.GOLDCARD.numOfCards;
		initializeMyCardBoard(this.myCardBoard,maxsize);
		/*put the initial card*/
		if (i.isFront()){
			for (int j = 0; j < 3; j++) {
				Elements centralElement = i.getCentralElements()[j];
				addElement(centralElement);
			}
		} else {
			for(Elements element : Elements.values()){
				addElement(element);
			}


		}
        if (myCardBoard != null) {
            myCardBoard[maxsize][maxsize]=i;
        }


    }


	/**
	 * Add the card on the board and count the new elements showed up and deletes the covered ones.
	 * This method is used only if the check method is called first and returns true
	 * @author: Gong
	 * @param: input: the card which would be put in the board;front: boolean which indicates the face of the card decided by de player; x:the coordinate x of the position desired; y:coordinate y desired
	 * @exception: NegativeCoordinateException: when the value of x or y or both is negative; HighCoordinateException: the value is too high for the board
	 */
	public void addCard(ResourceCard input,int x, int y) {
		/* no Exception handle*/
		if (input.isFront()){
			addAllCornersElements(input);
		}else{
			addElement(input.getKingdom());
		}
		deleteCoveredElements(x,y);

	}


	/**
	 * Check about the coordinates and the coordinate, if there are all free empty corners it returns true, otherwise false
	 * This method is used by the player class to check the availability of the board
	 * @author: Gong
	 * @return: returns a boolean, if there are all free empty corners, and it is a free slot it returns true, otherwise false
	 * @param: x:the coordinate x of the position desired; y:coordinate y desired
	 * @exception: NegativeCoordinateException: when the value of x or y or both is negative; HighCoordinateException: the value is too high for the board
	 */
	public boolean check(int x, int y) {
		/* no Exception handle*/

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
	 * @return: returns a boolean, if there is a free empty corners
	 * @param: x:the coordinate x of the position desired; y:coordinate y desired
	 * @exception: NegativeCoordinateException: when the value of x or y or both is negative; HighCoordinateException: the value is too high for the board
	 */
	private boolean checkCorner(int x, int y, CornerPosition corner) {
		/* no Exception handle*/
        switch (corner) {
            case UPLEFT -> {
				if (!isCardCoordinate(x-1,y+1))
					return true;
				return Elements.HIDE != myCardBoard[x - 1][y + 1].corners.get(CornerPosition.DOWNRIGHT);
            }
            case UPRIGHT -> {
				if (!isCardCoordinate(x+1,y+1))
					return true;
				return Elements.HIDE != myCardBoard[x + 1][y + 1].corners.get(CornerPosition.DOWNLEFT);
            }
            case DOWNLEFT -> {
				if (!isCardCoordinate(x-1,y-1))
					return true;
				return Elements.HIDE != myCardBoard[x - 1][y - 1].corners.get(CornerPosition.UPRIGHT);
            }
            case DOWNRIGHT -> {
				if (!isCardCoordinate(x+1,y-1))
					return true;
				return Elements.HIDE != myCardBoard[x + 1][y - 1].corners.get(CornerPosition.UPLEFT);
            }
        }
        return false;
    }



	/**
	 * method used for adding the number of elements present in the board of the player
	 * @author: Gong
	 * @param: element: the element which number has to be increased;value: the value of the element that has to be increased or decreased
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
	 * @param: element: the element which number has to be increased of 1;
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
	 * @param: Card:card with all elements that would be added in the counter of elements
	 * @exception: nothing for now....
	 */
	public void addAllCornersElements(Card card){
		/* no Exception handle*/

		for (CornerPosition corner : CornerPosition.values()) {
			addCornerElement(card, corner);
		}

	}


	/**
	 * Add  the upright element present in that corner of the card selected
	 * This method is used by the method addCorners,
	 * it can also be called to add only an element of the corner indicated
	 * Remember that a new card placed also causes elements covered so is suggested to call also the method deleteCoveredElements
	 * @author: Gong
	 * @return: void
	 * @param: Card:card with the corner selected and its element that would be added in the counter of elements
	 * @exception: nothing for now....
	 */
	public void addCornerElement(Card card, CornerPosition corner){
		/* no Exception handle*/
        switch (corner) {
            case UPLEFT -> {
				Elements element= card.corners.get(CornerPosition.UPLEFT);
				addElement(element);
            }
            case UPRIGHT -> {
				Elements element= card.corners.get(CornerPosition.UPRIGHT);
				addElement(element);
            }
            case DOWNLEFT -> {
				Elements element= card.corners.get(CornerPosition.DOWNLEFT);
				addElement(element);
            }
            case DOWNRIGHT -> {
				Elements element= card.corners.get(CornerPosition.DOWNRIGHT);
				addElement(element);
            }
        }

	}







	/**
	 * Delete from the counter all the covered element in that coordinate there is a card placed
	 * This method is used normally after the method addCorners and add Card.
	 * @author: Gong
	 * @return: void
	 * @param: x:the coordinate x of the position desired; y:coordinate y desired
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
	 * @param: x:the coordinate x of the position desired; y:coordinate y desired
	 * @exception: NegativeCoordinateException: when the value of x or y or both is negative; HighCoordinateException: the value is too high for the board
	 */
	public void deleteCoveredByCorner(int x, int y,CornerPosition corner){
        switch (corner) {
            case UPLEFT -> {
				if (isCardCoordinate( x-1, y+1)){
					Elements element=myCardBoard[x - 1][y + 1].corners.get(CornerPosition.DOWNRIGHT);
					addElement(element,-1);
				}
            }
            case UPRIGHT -> {
				if (isCardCoordinate( x+1, y+1)){
					Elements element=myCardBoard[x + 1][y + 1].corners.get(CornerPosition.DOWNLEFT);
					addElement(element,-1);
				}
            }
            case DOWNLEFT -> {
				if (isCardCoordinate( x-1, y-1)){
					Elements element=myCardBoard[x - 1][y - 1].corners.get(CornerPosition.UPRIGHT);
					addElement(element,-1);
				}
            }
            case DOWNRIGHT -> {
				if (isCardCoordinate( x+1, y-1)){
					Elements element=myCardBoard[x + 1][y - 1].corners.get(CornerPosition.UPLEFT);
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
	 * @param: x:the coordinate x of the position desired; y:coordinate y desired
	 * @exception: NegativeCoordinateException: when the value of x or y or both is negative;
	 * HighCoordinateException: the value is too high for the board;CountZeroException:impossible case which in the card has no cards about
	 */

	public int numCardsAbout(int x, int y){
		int count=0;

		if (isCardCoordinate(x+1,y+1))
			count++;
		if (isCardCoordinate(x+1,y-11))
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
	 * @param: x:the coordinate x of the position desired; y:coordinate y desired
	 * @exception: NegativeCoordinateException: when the value of x or y or both is negative; HighCoordinateException: the value is too high for the board
	 */
	public boolean isCardCoordinate(int x, int y){
		return myCardBoard[x][y]!=null;
	}


	public void setMyCardBoard(Card[][] myCardBoard) {

		this.myCardBoard = myCardBoard;
	}

	/**
	 * Initializing it with all null
	 * @author: Gong
	 * @param: assign a board  and its max size
	 * @exception: MaxSizeNegativeException:maxsize is a negative number
	 */
	private void initializeMyCardBoard(Card[][] myCardBoard, int maxsize){
		/*initialize myCardBoard*/
		for (int j = 0; j < maxsize *2 +1; j++) {
			for (int k = 0; k < maxsize *2 +1; k++) {
				if (myCardBoard != null) {
					myCardBoard[j][k]=null;
				}
			}
		}
	}

	public void setCounterOfElements(Map<Elements, Integer> counterOfElements) {
		this.counterOfElements = counterOfElements;

	}

	public Card[][] getMyCardBoard() {
		return myCardBoard;
	}

	public Card getCardInBoard(Card[][] cardboard, int x, int y){
		Card c;
		c=cardboard[x][y];
        return c;
    }
	public Map<Elements, Integer> getCounterOfElements() {
		return counterOfElements;
	}
}
