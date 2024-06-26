package it.polimi.ingsw.Networking.Listeners;

import it.polimi.ingsw.Message.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;
// todo javadoc
public interface Listener extends Remote {

    void update(Message msg) throws RemoteException;

    void setNickname(String nickname) throws RemoteException;

    String getNickname() throws RemoteException;

    Integer getGameID() throws RemoteException;

    void setGameID(Integer gameID) throws RemoteException;
}
