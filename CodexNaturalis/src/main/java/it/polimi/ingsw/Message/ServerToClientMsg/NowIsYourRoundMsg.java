package it.polimi.ingsw.Message.ServerToClientMsg;

/**
 * Represents a message sent to a client indicating that it's their turn to play.
 * Extends {@link GenericServerMessage}.
 */
public class NowIsYourRoundMsg extends GenericServerMessage {

    private String description;

    /**
     * Constructs a NowIsYourRoundMsg object with a default description indicating it's the player's turn.
     */
    public NowIsYourRoundMsg() {
        this.description = "it's your turn";
    }

    /**
     * Retrieves the description of the message.
     * @return The description indicating it's the player's turn.
     */
    public String getDescription() {
        return this.description;
    }
}
