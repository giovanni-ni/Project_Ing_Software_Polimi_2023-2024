package it.polimi.ingsw.Networking;

import it.polimi.ingsw.Message.ClientToServerMsg.GenericClientMessage;

import java.rmi.RemoteException;
/**
 * The Client interface defines the contract for client-side communication
 * with the server. Implementations of this interface will handle sending
 * messages to the server.
 */
public interface Client {
    /**
     * Sends a message to the server.
     *
     * @param msg the message to be sent to the server
     * @throws RemoteException if there is a problem with the remote method invocation
     */
    public void messageToServer(GenericClientMessage msg)throws RemoteException;
}
