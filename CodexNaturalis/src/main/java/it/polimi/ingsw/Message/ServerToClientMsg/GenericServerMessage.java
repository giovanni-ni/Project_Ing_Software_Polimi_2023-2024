package it.polimi.ingsw.Message.ServerToClientMsg;

import it.polimi.ingsw.Message.Message;

import java.io.Serializable;

/**
 * Represents a generic message sent from the server to the client.
 * This class implements the {@link Message} interface and is Serializable.
 */
public class GenericServerMessage implements Message, Serializable {

    /**
     * Constructs a GenericServerMessage.
     * This constructor is empty as the class is intended as a generic placeholder.
     */
    public GenericServerMessage() {
        // Empty constructor
    }

}
