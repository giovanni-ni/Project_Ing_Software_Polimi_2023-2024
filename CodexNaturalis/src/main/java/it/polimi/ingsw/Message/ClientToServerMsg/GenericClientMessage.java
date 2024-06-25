package it.polimi.ingsw.Message.ClientToServerMsg;

import it.polimi.ingsw.Message.Message;
import it.polimi.ingsw.Networking.Listeners.SocketListener;
import it.polimi.ingsw.Networking.Listeners.Listener;

import java.io.Serializable;

/**
 * Represents a generic message sent from a client to the server.
 */
public class GenericClientMessage implements Message, Serializable {
    private int gameID;
    private boolean isMainControllerMessage = false;
    private String nickname;
    private Listener listener;

    /**
     * Constructs an empty GenericClientMessage.
     */
    public GenericClientMessage() {
    }

    /**
     * Constructs a GenericClientMessage with the specified nickname.
     *
     * @param nickname the nickname of the player sending the message
     */
    public GenericClientMessage(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Constructs a GenericClientMessage with the specified game ID and nickname.
     *
     * @param gameID the ID of the game
     * @param nickname the nickname of the player sending the message
     */
    public GenericClientMessage(int gameID, String nickname) {
        this.gameID = gameID;
        this.nickname = nickname;
    }

    /**
     * Gets the game ID.
     *
     * @return the game ID
     */
    public int getGameID() {
        return gameID;
    }

    /**
     * Sets the game ID.
     *
     * @param gameID the game ID
     */
    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    /**
     * Gets the nickname of the player sending the message.
     *
     * @return the nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Sets the nickname of the player sending the message.
     *
     * @param nickname the nickname
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Marks the message as intended for the main controller.
     */
    public void isMessageForMain() {
        isMainControllerMessage = true;
    }

    /**
     * Marks the message as not intended for the main controller.
     */
    public void isMessageNotForMain() {
        isMainControllerMessage = false;
    }

    /**
     * Checks if the message is for the main controller.
     *
     * @return true if the message is for the main controller, false otherwise
     */
    public boolean isMainControllerMessage() {
        return isMainControllerMessage;
    }

    /**
     * Sets whether the message is for the main controller.
     *
     * @param mainControllerMessage true if the message is for the main controller, false otherwise
     */
    public void setMainControllerMessage(boolean mainControllerMessage) {
        isMainControllerMessage = mainControllerMessage;
    }

    /**
     * Gets the listener associated with the message.
     *
     * @return the listener
     */
    public Listener getListener() {
        return listener;
    }

    /**
     * Sets the listener associated with the message.
     *
     * @param listener the listener
     */
    public void setListener(Listener listener) {
        this.listener = listener;
    }
}
