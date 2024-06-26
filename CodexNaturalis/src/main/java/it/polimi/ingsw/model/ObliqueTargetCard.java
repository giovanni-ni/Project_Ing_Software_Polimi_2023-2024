package it.polimi.ingsw.model;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents an ObliqueTargetCard in the game, which extends {@link TargetCard}.
 * An ObliqueTargetCard has properties such as a required element and a corner position.
 */
public class ObliqueTargetCard extends TargetCard implements Serializable {

    // Attributes

    /**
     * The element required for the card's goal.
     * Uses {@link Elements} to define the required element.
     */
    private Elements elemRequired;

    /**
     * The corner position associated with the card.
     * Uses {@link CornerPosition} to specify the corner position.
     */
    private CornerPosition cornerPosition;

    // Constructors

    /**
     * Constructs a new ObliqueTargetCard with default values.
     */
    public ObliqueTargetCard() {

    }

    /**
     * Constructs a new ObliqueTargetCard with the specified properties.
     *
     * @param idCard         the unique identifier of the card
     * @param basePoint      the base points of the card
     * @param ifCommon       flag indicating if the card is common
     * @param elemRequired   the element required for the card's goal
     * @param cornerPosition the corner position associated with the card
     */
    public ObliqueTargetCard(int idCard,int basePoint,boolean ifCommon,Elements elemRequired, CornerPosition cornerPosition) {
        super(idCard,basePoint,ifCommon);
        this.elemRequired = elemRequired;
        this.cornerPosition = cornerPosition;
    }

    // Methods

    /**
     * Gets the element required for the card's goal.
     *
     * @return the required element
     */
    public Elements getElemRequired() {
        return elemRequired;
    }

    /**
     * Sets the element required for the card's goal.
     *
     * @param elemRequired the required element to set
     */
    public void setElemRequired(Elements elemRequired) {
        this.elemRequired = elemRequired;
    }

    /**
     * Gets the corner position associated with the card.
     *
     * @return the corner position
     */
    public CornerPosition getcornerPosition() {
        return cornerPosition;
    }

    /**
     * Sets the corner position associated with the card.
     *
     * @param cornerPosition the corner position to set
     */
    public void setcornerPosition(CornerPosition cornerPosition) {
        this.cornerPosition = cornerPosition;
    }

    /**
     * Counts the points scored by the ObliqueTargetCard based on the specified board.
     * Points are calculated as the base points of the card multiplied by the number of oblique sets achieved.
     *
     * @param board the board to count points on
     * @return the total points scored by the card
     * @see Board
     */
    @Override
    public int countPoint(Board board) {
        return super.getbasePoint() * checkGoal(board);
    }

    /**
     * Checks the goal of the ObliqueTargetCard based on the specified board.
     * This method calculates how many complete oblique sets of the required element can be found on the board.
     *
     * @param board the board to check
     * @return the number of complete oblique sets of the required element found on the board
     * @see Board
     */
    @Override
    public int checkGoal(Board board){
        BiMap<Card,Coordinate> copy = HashBiMap.create();
        for (Card c : board.getCardCoordinate().keySet()){
            if (c.getKingdom()==elemRequired){
                copy.put(c,board.getCardCoordinate().get(c));
            }
        }
        BiMap<Card,Coordinate> copy1 = HashBiMap.create();
        copy1.putAll(copy);
        Coordinate xy1=new Coordinate(0,0);
        Coordinate xy2=new Coordinate(0,0);
        int n = 0;
        for (Card c :copy1.keySet()){
            if (copy.containsKey(c)) {
                int cardX = copy.get(c).getX();
                int cardY = copy.get(c).getY();
                if (cornerPosition == CornerPosition.UPLEFT) {
                    xy1 = new Coordinate(cardX + 1, cardY - 1);
                    xy2 = new Coordinate(cardX - 1, cardY + 1);
                } else if (cornerPosition == CornerPosition.UPRIGHT) {
                    xy1 = new Coordinate(cardX + 1, cardY + 1);
                    xy2 = new Coordinate(cardX - 1, cardY - 1);
                }
                if (copy.containsValue(xy1) && copy.containsValue(xy2)) {
                    n++;
                    copy.inverse().remove(xy1);
                    copy.inverse().remove(xy2);
                    copy.inverse().remove((new Coordinate(cardX, cardY)));
                }
            }
        }

        return n;
    }
}
