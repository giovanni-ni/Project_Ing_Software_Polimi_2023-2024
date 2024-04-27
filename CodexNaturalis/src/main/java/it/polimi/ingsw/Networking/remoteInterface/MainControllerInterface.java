package it.polimi.ingsw.Networking.remoteInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MainControllerInterface extends Remote {
    GameControllerInterface createGame(/*GameListener lis*/String nickname)throws RemoteException;

    GameControllerInterface joinFirstAvailableGame(/*GameListener lis*/String nickname) throws RemoteException;

    GameControllerInterface joinGame(/*GameListener lis*/String nickname,int idGame) throws RemoteException;

    GameControllerInterface reconnect(/*GameListener lis*/String nickname,int idGame) throws RemoteException;

    GameControllerInterface leaveGame(/*GameListener lis*/String nickname,int idGame) throws RemoteException;

}
