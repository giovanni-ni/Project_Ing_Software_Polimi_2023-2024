package it.polimi.ingsw.Message.ClientToServerMsg;

/**
 * Represents a message sent from a client to indicate readiness to start the game.
 */
public class SetReadyMessage extends GenericClientMessage {

    /**
     * Constructs a SetReadyMessage with the specified game ID and nickname.
     *
     * @param id the ID of the game
     * @param nickname the nickname of the player sending the message
     */
    public SetReadyMessage(int id, String nickname) {
        super(id, nickname);
    }

    /**
     * Constructs a SetReadyMessage.
     * This constructor is typically used when creating a message instance without specific parameters.
     */
    public SetReadyMessage() {
    }
}
