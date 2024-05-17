package it.polimi.ingsw.Message.ServerToClientMsg;

import it.polimi.ingsw.Message.ClientToServerMsg.ClientChatMessage;
import it.polimi.ingsw.Message.Message;

public class ServerChatMessage extends GenericServerMessage {
    private String fromPlayer;
    boolean isForAll;
    private String chatMsg;

    public ServerChatMessage(ClientChatMessage clientChatMessage) {
        this.chatMsg=clientChatMessage.getChatMsg();
        this.isForAll=clientChatMessage.isForAll();
        this.fromPlayer=clientChatMessage.getFromPlayer();
    }

    public String getFromPlayer() {
        return fromPlayer;
    }

    public boolean isForAll() {
        return isForAll;
    }

    public String getChatMsg() {
        return chatMsg;
    }
}
