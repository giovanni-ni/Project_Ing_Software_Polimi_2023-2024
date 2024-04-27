package it.polimi.ingsw.Networking.remoteInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameControllerInterface extends Remote {
    boolean playerIsReadyToStart(String nickname)throws RemoteException;

    //
    boolean isThisMyTurn(String nick) throws RemoteException;

    void disconnectPlayer(String nickname/*,GameListener listOfClient*/)throws RemoteException;

    void sentMessage(/*Message msg*/) throws RemoteException;

    int getGameId()throws RemoteException;

    int getNumOnlinePlayers()throws RemoteException;

    void leave(/*GameListener lis,*/String nickname)throws RemoteException;
}
