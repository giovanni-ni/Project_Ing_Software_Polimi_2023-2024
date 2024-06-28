package it.polimi.ingsw.Networking.remoteInterface;

import it.polimi.ingsw.Message.ClientToServerMsg.GenericClientMessage;
import it.polimi.ingsw.Networking.Listeners.Listener;
import it.polimi.ingsw.model.Coordinate;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
/**
 * Remote interface for the server in a distributed system using RMI.
 * This interface defines methods for client-server interaction.
 */
public interface VirtualServer extends Remote {

    void receiveHeartbeat() throws RemoteException;

    /**
     * Establishes a connection with the server for a client.
     *
     * @param client the listener object representing the client
     * @throws IOException if there is an I/O error during the connection process
     * @throws RemoteException if there is a communication-related exception
     */
    void connect(Listener client) throws IOException;

    /**
     * Adds a generic client message to the processing queue on the server.
     *
     * @param msg    the generic client message to be processed
     * @param client the listener object representing the client
     * @throws RemoteException if there is a communication-related exception
     */
    void addInQueue(GenericClientMessage msg, Listener client) throws  RemoteException;
}
