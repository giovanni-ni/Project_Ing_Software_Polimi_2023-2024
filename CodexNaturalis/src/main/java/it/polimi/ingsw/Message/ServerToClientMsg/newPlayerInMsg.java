package it.polimi.ingsw.Message.ServerToClientMsg;

import it.polimi.ingsw.Message.Message;

import java.io.Serializable;

public class newPlayerInMsg extends GenericServerMessage implements Message, Serializable {
    private String NicknameNewPlayer;

    public newPlayerInMsg(String nicknameNewPlayer) {
        NicknameNewPlayer = nicknameNewPlayer;
    }

    public String getNicknameNewPlayer() {
        return this.NicknameNewPlayer;
    }
}
