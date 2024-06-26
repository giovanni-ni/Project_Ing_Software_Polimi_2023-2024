package it.polimi.ingsw.Message.ClientToServerMsg;

/**
 * Message sent from the client to the server to indicate a choice between the front or back of a card.
 */
public class FrontOrBackMessage extends GenericClientMessage {
    private boolean frontOrBack;

    /**
     * Constructs a FrontOrBackMessage with the specified game ID, nickname, and choice.
     *
     * @param gameID the ID of the game
     * @param nickname the nickname of the player making the choice
     * @param choice true for front, false for back
     */
    public FrontOrBackMessage(int gameID, String nickname, boolean choice) {
        super(gameID, nickname);
        this.frontOrBack = choice;
    }

    /**
     * Constructs a FrontOrBackMessage with the specified choice.
     *
     * @param choice true for front, false for back
     */
    public FrontOrBackMessage(boolean choice) {
        this.frontOrBack = choice;
    }

    /**
     * Gets the choice of front or back.
     *
     * @return true for front, false for back
     */
    public boolean getFrontOrBack() {
        return frontOrBack;
    }
}
