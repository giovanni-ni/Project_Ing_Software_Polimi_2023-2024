package it.polimi.ingsw.Message.ClientToServerMsg;

import it.polimi.ingsw.Networking.Listeners.SocketListener;

public class JoinFirstMessage extends JoinGameMessage{

    public JoinFirstMessage(String nickname) {
        super(nickname,0);
        super.isMessageForMain();
    }
}
