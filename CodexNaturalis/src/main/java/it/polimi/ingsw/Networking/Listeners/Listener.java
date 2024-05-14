package it.polimi.ingsw.Networking.Listeners;

import it.polimi.ingsw.Message.Message;

import java.rmi.Remote;

public interface Listener extends Remote {

    void update(Message msg);

    void setNickname(String nickname);

    String getNickname();

    Integer getGameID();

    void setGameID(Integer gameID);
}
