package it.polimi.ingsw.Message.ClientToServerMsg;

/**
 * Message sent from the client to the server to draw a card.
 */
public class DrawCardMessage extends GenericClientMessage {
    private boolean deck;
    private int numberindex;

    /**
     * Constructs a DrawCardMessage with the specified nickname, game ID, deck type, and card index.
     *
     * @param nickname the nickname of the player drawing the card
     * @param gameId the ID of the game
     * @param deck the type of deck (true if drawing from a specific deck, false otherwise)
     * @param number the index number of the card to draw
     */
    public DrawCardMessage(String nickname, int gameId, boolean deck, int number) {
        super(gameId, nickname);
        this.deck = deck;
        this.numberindex = number;
        super.isMessageNotForMain();
    }

    /**
     * Gets the type of deck.
     *
     * @return true if drawing from a specific deck, false otherwise
     */
    public boolean getDeck() {
        return deck;
    }

    /**
     * Gets the index number of the card to draw.
     *
     * @return the index number of the card to draw
     */
    public int getNumberindex() {
        return numberindex;
    }
}
