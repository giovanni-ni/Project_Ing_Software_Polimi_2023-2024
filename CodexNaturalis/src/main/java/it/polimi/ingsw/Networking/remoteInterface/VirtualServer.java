package it.polimi.ingsw.Networking.remoteInterface;

import it.polimi.ingsw.Message.ClientToServerMsg.GenericClientMessage;
import it.polimi.ingsw.Networking.Listeners.Listener;
import it.polimi.ingsw.model.Coordinate;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
// todo javadoc
public interface VirtualServer extends Remote {
    void connect(Listener client) throws IOException;

    void addInQueue(GenericClientMessage msg, Listener client) throws  RemoteException;
}
