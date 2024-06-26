package it.polimi.ingsw.Message.ClientToServerMsg;

import it.polimi.ingsw.Networking.Listeners.SocketListener;

/**
 * Represents a message sent from a client to join the first available game.
 */
public class JoinFirstMessage extends JoinGameMessage {

    /**
     * Constructs a JoinFirstMessage with the specified nickname.
     * This message is intended for the main controller.
     *
     * @param nickname the nickname of the player sending the message
     */
    public JoinFirstMessage(String nickname) {
        super(nickname, 0);
        super.isMessageForMain();
    }
}
