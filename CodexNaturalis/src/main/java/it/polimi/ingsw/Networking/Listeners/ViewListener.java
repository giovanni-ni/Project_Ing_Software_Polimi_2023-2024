package it.polimi.ingsw.Networking.Listeners;

import it.polimi.ingsw.Message.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;

/**
 * Listener implementation for handling communication with a view.
 * This class manages an ObjectOutputStream to send messages to a view.
 */
public class ViewListener implements Listener{

    private final ObjectOutputStream out;
    /**
     * Constructs a ViewListener with the given ObjectOutputStream.
     *
     * @param out the ObjectOutputStream for writing messages
     */
    public ViewListener(ObjectOutputStream out) {
        this.out = out;
    }
    /**
     * Updates the listener with a new message to be sent to the view.
     *
     * @param msg the message to send to the view
     */
    @Override
    public void update(Message msg) {
            try {
                out.writeObject(msg);
            } catch (IOException e) {

            }
    }

    /**
     * Sets the nickname of the listener (not used in this context).
     *
     * @param nickname the nickname to set
     */
    @Override
    public void setNickname(String nickname) {

    }
    /**
     * Retrieves the nickname of the listener (not used in this context).
     *
     * @return always returns null
     */

    @Override
    public String getNickname() {
        return null;
    }
    /**
     * Retrieves the game ID associated with the listener (not used in this context).
     *
     * @return always returns null
     */
    @Override
    public Integer getGameID() {
        return null;
    }
    /**
     * Sets the game ID for the listener (not used in this context).
     *
     * @param gameID the game ID to set
     */
    @Override
    public void setGameID(Integer gameID) {

    }
    /**
     * Handles the heartbeat signal from the client.
     *
     * This method is called to indicate that the client is still active and connected.
     * It is typically invoked periodically by the client to ensure it remains connected.
     *
     * @throws RemoteException if a communication-related exception occurs during the remote method call.
     */
    @Override
    public void heartBeat() throws RemoteException {

    }
}
