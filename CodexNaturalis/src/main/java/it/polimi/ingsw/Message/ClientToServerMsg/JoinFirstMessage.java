package it.polimi.ingsw.Message.ClientToServerMsg;

import it.polimi.ingsw.Networking.Listeners.GameListener;

public class JoinFirstMessage extends JoinGameMessage{

    public JoinFirstMessage(String nickname) {
        super(nickname,0);
        super.isMessageForMain();
    }
}