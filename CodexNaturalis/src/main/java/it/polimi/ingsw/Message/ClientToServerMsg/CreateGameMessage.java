package it.polimi.ingsw.Message.ClientToServerMsg;

import it.polimi.ingsw.Networking.Listeners.SocketListener;

import java.io.Serializable;

public class CreateGameMessage extends JoinGameMessage implements Serializable {

    public CreateGameMessage(String nickname) {
        super(nickname, 0);
        super.isMessageForMain();
    }
}
