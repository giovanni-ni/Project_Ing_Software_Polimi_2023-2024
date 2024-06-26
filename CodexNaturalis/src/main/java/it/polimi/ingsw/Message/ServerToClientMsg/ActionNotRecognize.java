package it.polimi.ingsw.Message.ServerToClientMsg;

/**
 * Represents a message sent from the server to the client indicating that an action was not recognized.
 */
public class ActionNotRecognize extends GenericServerMessage {

    private String description;

    /**
     * Constructs an ActionNotRecognize message with the specified description.
     *
     * @param description the description of the unrecognized action
     */
    public ActionNotRecognize(String description) {
        this.description = description;
    }

    /**
     * Constructs an empty ActionNotRecognize message.
     * This constructor is typically used when creating a message instance without specific parameters.
     */
    public ActionNotRecognize() {
    }

    /**
     * Gets the description of the unrecognized action.
     *
     * @return the description of the unrecognized action
     */
    public String getDescription() {
        return this.description;
    }
}
