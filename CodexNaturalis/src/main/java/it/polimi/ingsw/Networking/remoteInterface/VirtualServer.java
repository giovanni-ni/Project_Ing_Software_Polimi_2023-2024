package it.polimi.ingsw.Networking.remoteInterface;

import it.polimi.ingsw.model.Coordinate;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VirtualServer extends Remote {
    void connect(VirtualView client) throws RemoteException;

    void getACard(String nickname, boolean isGold, int whichCard);

    void playACard(String nickname, int indexCardOnHand, Coordinate coo, boolean isFront);

}
