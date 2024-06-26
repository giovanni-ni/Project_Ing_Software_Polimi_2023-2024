package it.polimi.ingsw.Message.ClientToServerMsg;

/**
 * Represents a message sent by a client to reconnect to a game server.
 * Extends {@link GenericClientMessage}.
 */
public class ReconnectRequestMsg extends GenericClientMessage{

    /**
     * Constructs a {@code ReconnectMessage} with the specified nickname and game ID.
     *
     * @param nickname the nickname of the client reconnecting to the game
     * @param id   the ID of the game to which the client wants to reconnect
     */
    public ReconnectRequestMsg(String nickname, int id) {
        super(id, nickname);
        super.setMainControllerMessage(true);
    }

}
