package it.polimi.ingsw.Message.ClientToServerMsg;

import it.polimi.ingsw.Message.Message;

public class GenericClientMessage implements Message {
    private int gameID;
    private boolean isMainControllerMessage;
    private String nickname;

    public GenericClientMessage(){

    }

    public GenericClientMessage(int gameID, String nickname) {
        this.gameID = gameID;
        this.nickname = nickname;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void isMessageForMain(){
        isMainControllerMessage=true;

    }
    public void isMessageNotForMain(){
        isMainControllerMessage=false;

    }

    public boolean isMainControllerMessage() {
        return isMainControllerMessage;
    }
}
