package it.polimi.ingsw.Message.ServerToClientMsg;

import it.polimi.ingsw.Message.ClientToServerMsg.ClientChatMessage;

/**
 * Represents a message sent from the server to clients containing a chat message.
 * Extends {@link GenericServerMessage}.
 */
public class ServerChatMessage extends GenericServerMessage {
    private String fromPlayer;
    private boolean isForAll;
    private String chatMsg;

    /**
     * Constructs a ServerChatMessage from a ClientChatMessage.
     * @param clientChatMessage The ClientChatMessage to convert into a server message.
     */
    public ServerChatMessage(ClientChatMessage clientChatMessage) {
        this.chatMsg = clientChatMessage.getChatMsg();
        this.isForAll = clientChatMessage.isForAll();
        this.fromPlayer = clientChatMessage.getFromPlayer();
    }

    /**
     * Retrieves the nickname of the player who sent the chat message.
     * @return The nickname of the player who sent the chat message.
     */
    public String getFromPlayer() {
        return fromPlayer;
    }

    /**
     * Checks if the chat message is intended for all players.
     * @return True if the chat message is for all players, false otherwise.
     */
    public boolean isForAll() {
        return isForAll;
    }

    /**
     * Retrieves the content of the chat message.
     * @return The content of the chat message.
     */
    public String getChatMsg() {
        return chatMsg;
    }
}
