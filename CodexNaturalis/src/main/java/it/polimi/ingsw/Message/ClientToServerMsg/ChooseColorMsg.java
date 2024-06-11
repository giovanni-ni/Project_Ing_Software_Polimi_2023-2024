package it.polimi.ingsw.Message.ClientToServerMsg;

import it.polimi.ingsw.model.PlayerColor;

public class ChooseColorMsg extends GenericClientMessage{
    PlayerColor color;

    ChooseColorMsg(String nickName, int gameID, PlayerColor color){
        super(gameID,nickName);
        this.color = color;
    }
    ChooseColorMsg(PlayerColor color){
        this.color = color;
    }

    public PlayerColor getColor(){
        return color;
    }
}
