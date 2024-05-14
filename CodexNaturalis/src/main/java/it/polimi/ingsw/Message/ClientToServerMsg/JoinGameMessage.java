package it.polimi.ingsw.Message.ClientToServerMsg;

import it.polimi.ingsw.Networking.Listeners.SocketListener;

public class JoinGameMessage extends GenericClientMessage{

    public JoinGameMessage(String nickname, int idGame) {
        super(idGame,nickname);
        super.isMessageForMain();
    }

}
