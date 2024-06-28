package it.polimi.ingsw.Networking.Listeners;

import it.polimi.ingsw.Message.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * Listener implementation for handling socket-based communication.
 * This class manages the output stream to send messages over a socket connection.
 */
public class SocketListener implements Listener {

    private transient final ObjectOutputStream out;

    private String nickname;

    private Integer GameID ;
    /**
     * Constructs a SocketListener with the given ObjectOutputStream.
     *
     * @param outputStream the ObjectOutputStream for writing messages
     */
    public SocketListener(ObjectOutputStream outputStream) {
        this.out = outputStream;
        GameID = -1;
        nickname = null;
    }
    /**
     * Updates the listener with a new message from the server.
     *
     * @param msg the message received from the server
     */
    @Override
    public void update(Message msg) {
        try {
            out.writeObject(msg);
            finishSending();
        } catch (IOException e) {

        }

    }
    /**
     * Sets the nickname of the listener.
     *
     * @param nickname the nickname to set
     */
    @Override
    public void setNickname(String nickname) {
        this.nickname =nickname;
    }
    private void finishSending() throws IOException {
        out.flush();
        out.reset();
    }
    /**
     * Helper method to finish sending data over the output stream.
     *
     */
    public OutputStream getOut() {
        return out;
    }
    /**
     * Retrieves the output stream associated with this listener.
     *
     * @return the output stream
     */
    @Override
    public String getNickname() {
        return nickname;
    }
    /**
     * Retrieves the game ID associated with the listener.
     *
     * @return the game ID
     */

    @Override
    public Integer getGameID() {
        return GameID;
    }
    /**
     * Sets the game ID for the listener.
     *
     * @param gameID the game ID to set
     */
    @Override
    public void setGameID(Integer gameID) {
        GameID = gameID;
    }
}
