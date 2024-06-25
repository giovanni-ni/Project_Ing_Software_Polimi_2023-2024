package it.polimi.ingsw.Message.ClientToServerMsg;

import it.polimi.ingsw.model.Coordinate;

/**
 * Represents a message sent from a client to play a card on the game board.
 */
public class playCardMessage extends GenericClientMessage {
    private boolean front;
    private Coordinate coo;
    private int indexOfCardOnHand;

    /**
     * Constructs a playCardMessage with the specified parameters.
     *
     * @param name the nickname of the player sending the message
     * @param indexOfCardOnHand the index of the card in the player's hand
     * @param front indicates whether the card is played on the front or back side of the board
     * @param x the x-coordinate on the board where the card will be played
     * @param y the y-coordinate on the board where the card will be played
     */
    public playCardMessage(String name, int indexOfCardOnHand, boolean front, int x, int y) {
        super(name);
        super.isMessageNotForMain();

        this.indexOfCardOnHand = indexOfCardOnHand;
        coo = new Coordinate(x, y);
        this.front = front;
    }

    /**
     * Gets the coordinates where the card will be played.
     *
     * @return the coordinates
     */
    public Coordinate getCoo() {
        return coo;
    }

    /**
     * Sets the coordinates where the card will be played.
     *
     * @param coo the coordinates
     */
    public void setCoo(Coordinate coo) {
        this.coo = coo;
    }

    /**
     * Gets the index of the card in the player's hand.
     *
     * @return the index of the card
     */
    public int getIndexOfCardOnHand() {
        return indexOfCardOnHand;
    }

    /**
     * Sets the index of the card in the player's hand.
     *
     * @param indexOfCardOnHand the index of the card
     */
    public void setIndexOfCardOnHand(int indexOfCardOnHand) {
        this.indexOfCardOnHand = indexOfCardOnHand;
    }

    /**
     * Checks if the card is played on the front side of the board.
     *
     * @return true if played on the front side, false otherwise
     */
    public boolean isFront() {
        return front;
    }

    /**
     * Sets whether the card is played on the front side of the board.
     *
     * @param front true if played on the front side, false otherwise
     */
    public void setFront(boolean front) {
        this.front = front;
    }
}
