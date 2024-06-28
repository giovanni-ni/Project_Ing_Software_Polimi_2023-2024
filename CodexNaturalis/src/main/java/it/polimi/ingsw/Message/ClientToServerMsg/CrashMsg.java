package it.polimi.ingsw.Message.ClientToServerMsg;

import it.polimi.ingsw.Message.Message;
import it.polimi.ingsw.Message.ServerToClientMsg.GenericServerMessage;
import it.polimi.ingsw.Networking.Listeners.Listener;

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

    /**
     * Constructs a Crash Message.
     * @param nickNameDisconnect String that indicates the client with disconnection source.
     * @param gameId Int that indicates the client's match id.
     */
    public CrashMsg(String nickNameDisconnect, int gameId, Listener listener) {
        super(gameId,nickNameDisconnect);
        isMessageForMain();
        super.setListener(listener);
    }

    public String getNickNameDisconnect() {
        return super.getNickname();
    }


}
