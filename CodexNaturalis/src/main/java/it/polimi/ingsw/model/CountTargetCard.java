package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a CountTargetCard in the game, which extends {@link TargetCard}.
 * A CountTargetCard has additional properties such as required elements for its goal.
 */
public class CountTargetCard extends TargetCard implements Serializable {

    // Attributes

    /**
     * The array of elements required for the card's goal.
     * Uses {@link Elements} to define the required elements.
     */
    Elements[] elemRequired;

    // Constructors

    /**
     * Constructs a new CountTargetCard with default values.
     */
    public CountTargetCard(){

    }

    /**
     * Constructs a new CountTargetCard with the specified properties.
     *
     * @param idCard      the unique identifier of the card
     * @param basePoint   the base points of the card
     * @param ifCommon    flag indicating if the card is common
     * @param elemRequired the elements required for the card's goal
     */
    public CountTargetCard(int idCard,int basePoint,boolean ifCommon, Elements[] elemRequired) {
        super(idCard,basePoint,ifCommon);

        this.elemRequired = elemRequired;
    }

    // Methods

    /**
     * Gets the array of elements required for the card's goal.
     *
     * @return the array of required elements
     */
    public Elements[] getElemRequired() {
        return elemRequired;
    }

    /**
     * Sets the array of elements required for the card's goal.
     *
     * @param elemRequired the array of required elements to set
     */
    public void setElemRequired(Elements[] elemRequired) {
        this.elemRequired = elemRequired;
    }

    /**
     * Checks the goal of the CountTargetCard based on the specified board.
     * This method calculates how many complete sets of required elements can be formed on the board.
     *
     * @param board the board to check
     * @return the number of complete sets of required elements possible on the board
     * @see Board
     */
    public int checkGoal(Board board) {

        int n = Integer.MAX_VALUE;

        Map<Elements, Integer> elementCounts = new HashMap<>();
        for (Elements element : this.elemRequired) {
            elementCounts.put(element, elementCounts.getOrDefault(element, 0) + 1);
        }

        for (Map.Entry<Elements, Integer> entry : elementCounts.entrySet()) {
            Elements element = entry.getKey();
            int countInMap = board.getCounterOfElements().containsKey(element) ? board.getCounterOfElements().get(element) : 0;
            int countInArray = entry.getValue();
            int setsPossible = countInMap / countInArray;
            n = Math.min(n, setsPossible);
        }

        return n;
    }

    /**
     * Counts the points scored by the CountTargetCard based on the specified board.
     * Points are calculated as the base points of the card multiplied by the number of complete sets achieved.
     *
     * @param board the board to count points on
     * @return the total points scored by the card
     * @see Board
     */
    public int countPoint(Board board) {
        return super.getbasePoint() * checkGoal(board);
    }
}
