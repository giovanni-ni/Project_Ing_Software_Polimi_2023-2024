package it.polimi.ingsw.model;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.io.Serializable;

/**
 * Represents a PositionGoalTarget in the game, which extends {@link TargetCard}.
 * A PositionGoalTarget has properties such as two required elements and a corner position.
 */
public class PositionGoalTarget extends TargetCard implements Serializable {

    // Attributes

    /**
     * The first element required for the card's goal.
     * Uses {@link Elements} to define the first required element.
     */
    Elements firstElement;

    /**
     * The second element required for the card's goal.
     * Uses {@link Elements} to define the second required element.
     */
    Elements secondElement;

    /**
     * The corner position associated with the card.
     * Uses {@link CornerPosition} to specify the corner position.
     */
    CornerPosition cp;

    // Constructors

    /**
     * Constructs a new PositionGoalTarget with default values.
     */
    public PositionGoalTarget() {

    }

    /**
     * Constructs a new PositionGoalTarget with the specified properties.
     *
     * @param idCard         the unique identifier of the card
     * @param basePoint      the base points of the card
     * @param ifCommon       flag indicating if the card is common
     * @param firstElement   the first element required for the card's goal
     * @param secondElement  the second element required for the card's goal
     * @param cp             the corner position associated with the card
     */
    public PositionGoalTarget(int idCard,int basePoint,boolean ifCommon,Elements firstElement,Elements secondElement, CornerPosition cp) {
        super(idCard,basePoint,ifCommon);
        this.firstElement = firstElement;
        this.secondElement = secondElement;
        this.cp = cp;
    }

    // Methods

    /**
     * Gets the first element required for the card's goal.
     *
     * @return the first required element
     */
    public Elements getFirstElement() {
        return firstElement;
    }

    /**
     * Sets the first element required for the card's goal.
     *
     * @param firstElement the first required element to set
     */
    public void setFirstElement(Elements firstElement) {
        this.firstElement = firstElement;
    }

    /**
     * Gets the second element required for the card's goal.
     *
     * @return the second required element
     */
    public Elements getSecondElement() {
        return secondElement;
    }

    /**
     * Sets the second element required for the card's goal.
     *
     * @param secondElement the second required element to set
     */
    public void setSecondElement(Elements secondElement) {
        this.secondElement = secondElement;
    }

    /**
     * Gets the corner position associated with the card.
     *
     * @return the corner position
     */
    public CornerPosition getCp() {
        return cp;
    }

    /**
     * Sets the corner position associated with the card.
     *
     * @param cp the corner position to set
     */
    public void setCp(CornerPosition cp) {
        this.cp = cp;
    }


    /**
     * Checks the goal of the PositionGoalTarget based on the specified board.
     * This method calculates how many complete sets of two elements can be formed in a specific corner position on the board.
     *
     * @param board the board to check
     * @return the number of complete sets of required elements found on the board
     * @see Board
     */
    @Override
    public int checkGoal(Board board) {
        BiMap<Card,Coordinate> copyFirst = HashBiMap.create();
        for (Card c : board.getCardCoordinate().keySet()){
            if (c.getKingdom()==firstElement){
                copyFirst.put(c,board.getCardCoordinate().get(c));
            }
        }

        BiMap<Card,Coordinate> copySecond= HashBiMap.create();
        for (Card c : board.getCardCoordinate().keySet()){
            if (c.getKingdom()==secondElement){
                copySecond.put(c,board.getCardCoordinate().get(c));
            }
        }
        BiMap<Card,Coordinate> copy1 = HashBiMap.create();
        copy1.putAll(copySecond);

        Coordinate xy1=new Coordinate(0,0);
        Coordinate xy2=new Coordinate(0,0);
        int n = 0;
        for (Card c :copy1.keySet()){
            if (copySecond.containsKey(c)) {
                int cardX = copySecond.get(c).getX();
                int cardY = copySecond.get(c).getY();
                switch (cp) {
                    case UPLEFT -> {
                        xy1 = new Coordinate(cardX - 1, cardY + 1);
                        xy2 = new Coordinate(cardX - 1, cardY + 3);

                    }
                    case DOWNRIGHT -> {
                        xy1 = new Coordinate(cardX + 1, cardY - 1);
                        xy2 = new Coordinate(cardX + 1, cardY - 3);


                    }
                    case DOWNLEFT-> {
                        xy1 = new Coordinate(cardX - 1, cardY - 1);
                        xy2 = new Coordinate(cardX - 1, cardY - 3);

                    }
                    case UPRIGHT -> {
                        xy1 = new Coordinate(cardX + 1, cardY + 1);
                        xy2 = new Coordinate(cardX + 1, cardY + 3);

                    }
                }
                if (copyFirst.containsValue(xy1) && copyFirst.containsValue(xy2)) {
                    n++;
                    copyFirst.inverse().remove(xy1);
                    copyFirst.inverse().remove(xy2);
                    copySecond.inverse().remove((new Coordinate(cardX, cardY)));
                }
            }


        }

        return n;
    }

    /**
     * Counts the points scored by the PositionGoalTarget based on the specified board.
     * Points are calculated as the base points of the card multiplied by the number of complete sets achieved.
     *
     * @param board the board to count points on
     * @return the total points scored by the card
     * @see Board
     */
    @Override
    public int countPoint(Board board) {
        return super.getbasePoint() * checkGoal(board);
    }
}
