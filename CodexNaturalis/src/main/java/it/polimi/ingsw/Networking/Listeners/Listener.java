package it.polimi.ingsw.Networking.Listeners;

import it.polimi.ingsw.Message.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface for listeners that receive updates from the server.
 * Implementations of this interface can receive messages and handle player information.
 */
public interface Listener extends Remote {
    /**
     * Method to handle incoming messages from the server.
     *
     * @param msg the message received from the server
     * @throws RemoteException if there is an error with the remote method invocation
     */
    void update(Message msg) throws RemoteException;
    /**
     * Sets the nickname of the listener.
     *
     * @param nickname the nickname to set
     * @throws RemoteException if there is an error with the remote method invocation
     */
    void setNickname(String nickname) throws RemoteException;

    /**
     * Retrieves the nickname of the listener.
     *
     * @return the nickname of the listener
     * @throws RemoteException if there is an error with the remote method invocation
     */
    String getNickname() throws RemoteException;
    /**
     * Retrieves the game ID associated with the listener.
     *
     * @return the game ID
     * @throws RemoteException if there is an error with the remote method invocation
     */
    Integer getGameID() throws RemoteException;

    /**
     * Sets the game ID for the listener.
     *
     * @param gameID the game ID to set
     * @throws RemoteException if there is an error with the remote method invocation
     */
    void setGameID(Integer gameID) throws RemoteException;
}
