package it.polimi.ingsw.Message.ClientToServerMsg;

import it.polimi.ingsw.Networking.Listeners.SocketListener;

import java.io.Serializable;

public class CreateGameMessage extends JoinGameMessage implements Serializable {
    private int limitPly = 0;

    public CreateGameMessage(String nickname) {
        super(nickname, 0);
        super.isMessageForMain();
    }
    public CreateGameMessage(String nickname, int limitPly) {
        super(nickname, 0);
        if (limitPly>=2 && limitPly<=4)
            this.limitPly = limitPly;
        super.isMessageForMain();
    }

    public int getLimitPly() {
        return limitPly;
    }
}
