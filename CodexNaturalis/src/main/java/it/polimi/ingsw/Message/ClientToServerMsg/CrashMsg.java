package it.polimi.ingsw.Message.ClientToServerMsg;

import it.polimi.ingsw.Message.Message;
import it.polimi.ingsw.Message.ServerToClientMsg.GenericServerMessage;

/**
 * Represents a message indicating that the client disconnected from the game.
 * Extends {@link GenericServerMessage}.
 * Implements {@link Message}.
 */
public class CrashMsg extends GenericClientMessage {

    /**
     * Constructs a Crash Message.
     * @param nickNameDisconnect String that indicates the client with disconnection source.
     */
    public CrashMsg(String nickNameDisconnect) {
        super(nickNameDisconnect);
        isMessageForMain();
    }

    public String getNickNameDisconnect() {
        return super.getNickname();
    }
}
