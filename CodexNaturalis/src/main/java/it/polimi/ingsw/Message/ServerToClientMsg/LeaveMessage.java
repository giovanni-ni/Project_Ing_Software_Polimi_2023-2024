package it.polimi.ingsw.Message.ServerToClientMsg;


import it.polimi.ingsw.Message.Message;

/**
 * Represents a message indicating that the current game has an error caused by a disconnection.
 * Extends {@link GenericServerMessage}.
 * Implements {@link Message}.
 */
public class LeaveMessage extends GenericServerMessage {
    private String leftPlayer;

    /**
     * Constructs a LeaveMessage with a leftPlayer String that indicates the disconnection source.
     * @param leftPlayer String that indicates the disconnection source.
     */
    public LeaveMessage(String leftPlayer) {
        this.leftPlayer = leftPlayer;
    }


    /**
     * Retrieves the String that indicates the disconnection source.
     * @return String that indicates the disconnection source.
     */
    public String getLeftPlayer() {
        return leftPlayer;
    }
}
