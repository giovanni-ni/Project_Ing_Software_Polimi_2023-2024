package it.polimi.ingsw.Message.ServerToClientMsg;

import it.polimi.ingsw.Message.Message;

public class newPlayerInMsg implements Message {
    private String NicknameNewPlayer;

    public newPlayerInMsg(String nicknameNewPlayer) {
        NicknameNewPlayer = nicknameNewPlayer;
    }
}
