package it.polimi.ingsw.Message.ClientToServerMsg;

/**
 * Represents a message sent from a client to set a target card choice.
 */
public class SetTargetCardMessage extends GenericClientMessage {

    private int choice;

    /**
     * Constructs a SetTargetCardMessage with the specified game ID, nickname, and choice.
     *
     * @param gameID the ID of the game
     * @param nickname the nickname of the player sending the message
     * @param choice the choice of target card
     */
    public SetTargetCardMessage(int gameID, String nickname, int choice) {
        super(gameID, nickname);
        this.choice = choice;
    }

    /**
     * Constructs a SetTargetCardMessage with the specified choice.
     * This constructor is typically used when creating a message instance without specific game ID and nickname.
     *
     * @param choice the choice of target card
     */
    public SetTargetCardMessage(int choice) {
        this.choice = choice;
    }

    /**
     * Gets the choice of target card.
     *
     * @return the choice of target card
     */
    public int getChoice() {
        return choice;
    }
}
