package it.polimi.ingsw.Message.ServerToClientMsg;

/**
 * Represents a message indicating a failed attempt to join a game.
 * Extends {@link GenericServerMessage}.
 */
public class JoinFailMsg extends GenericServerMessage {

    private final String description;

    /**
     * Constructs a JoinFailMsg with a description of the failure reason.
     * @param description The description of the failure reason.
     */
    public JoinFailMsg(String description) {
        this.description = description;
    }

    /**
     * Retrieves the description of the failure reason.
     * @return The description of the failure reason.
     */
    public String getDescription() {
        return description;
    }
}
