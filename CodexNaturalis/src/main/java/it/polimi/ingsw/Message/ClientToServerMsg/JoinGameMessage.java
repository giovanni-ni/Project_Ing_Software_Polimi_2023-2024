package it.polimi.ingsw.Message.ClientToServerMsg;

import it.polimi.ingsw.Networking.Listeners.SocketListener;

/**
 * Represents a message sent from a client to join a specific game.
 */
public class JoinGameMessage extends GenericClientMessage {

    /**
     * Constructs a JoinGameMessage with the specified nickname and game ID.
     * This message is intended for the main controller.
     *
     * @param nickname the nickname of the player sending the message
     * @param idGame the ID of the game to join
     */
    public JoinGameMessage(String nickname, int idGame) {
        super(idGame, nickname);
        super.isMessageForMain();
    }
}
