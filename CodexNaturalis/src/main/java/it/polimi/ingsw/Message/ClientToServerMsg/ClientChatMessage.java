package it.polimi.ingsw.Message.ClientToServerMsg;

import it.polimi.ingsw.Message.Message;

/**
 * Message sent from the client to the server containing a chat message.
 */
public class ClientChatMessage extends GenericClientMessage {
    private String fromPlayer;
    private String toPlayer;
    private boolean isForAll;
    private String chatMsg;

    /**
     * Constructs a ClientChatMessage with the specified game ID, nickname, recipient, and message content.
     *
     * @param idGame the ID of the game
     * @param nickName the nickname of the player sending the message
     * @param isForAll whether the message is intended for all players
     * @param toPlayer the nickname of the recipient player
     * @param chatMsg the content of the chat message
     */
    public ClientChatMessage(int idGame, String nickName, boolean isForAll, String toPlayer, String chatMsg) {
        super(idGame, nickName);
        this.fromPlayer = nickName;
        this.toPlayer = toPlayer;
        this.isForAll = isForAll;
        this.chatMsg = chatMsg;
        isMessageNotForMain();
    }

    /**
     * Gets the nickname of the player sending the message.
     *
     * @return the nickname of the player sending the message
     */
    public String getFromPlayer() {
        return super.getNickname();
    }

    /**
     * Gets the nickname of the recipient player.
     *
     * @return the nickname of the recipient player
     */
    public String getToPlayer() {
        return toPlayer;
    }

    /**
     * Checks if the message is intended for all players.
     *
     * @return true if the message is for all players, false otherwise
     */
    public boolean isForAll() {
        return isForAll;
    }

    /**
     * Gets the content of the chat message.
     *
     * @return the content of the chat message
     */
    public String getChatMsg() {
        return chatMsg;
    }
}
