package it.polimi.ingsw.Message.ClientToServerMsg;

import it.polimi.ingsw.Message.Message;
import it.polimi.ingsw.Networking.Listeners.GameListener;

import java.io.Serializable;

public class GenericClientMessage implements Message, Serializable {
    private int gameID;
    private boolean isMainControllerMessage = false;
    private String nickname;
    private GameListener listener;


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
        isMainControllerMessage = false;

    }

    public boolean isMainControllerMessage() {
        return isMainControllerMessage;
    }

    public void setMainControllerMessage(boolean mainControllerMessage) {
        isMainControllerMessage = mainControllerMessage;
    }

    public GameListener getListener() {
        return listener;
    }

    public void setListener(GameListener listener) {
        this.listener=listener;

    }
}
