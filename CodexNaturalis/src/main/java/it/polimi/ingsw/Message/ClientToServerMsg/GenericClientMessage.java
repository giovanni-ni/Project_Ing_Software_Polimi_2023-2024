package it.polimi.ingsw.Message.ClientToServerMsg;

public class GenericClientMessage {
    int gameID;

    String nickname;

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
}
