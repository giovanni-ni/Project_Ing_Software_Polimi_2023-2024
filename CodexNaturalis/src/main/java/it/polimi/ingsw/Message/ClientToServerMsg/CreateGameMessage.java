package it.polimi.ingsw.Message.ClientToServerMsg;

import it.polimi.ingsw.Networking.Listeners.GameListener;

public class CreateGameMessage extends JoinGameMessage{

    public CreateGameMessage(String nickname) {
        super(nickname, 0);
        super.isMessageForMain();
    }
}
