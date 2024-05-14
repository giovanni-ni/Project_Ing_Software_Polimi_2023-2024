package it.polimi.ingsw.Networking.remoteInterface;

import it.polimi.ingsw.Message.ClientToServerMsg.GenericClientMessage;
import it.polimi.ingsw.Networking.Listeners.Listener;
import it.polimi.ingsw.model.Coordinate;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VirtualServer extends Remote {
    void connect(Listener client) throws RemoteException;

    void addInQueue(GenericClientMessage msg, Listener client) throws  RemoteException;
}
