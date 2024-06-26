package it.polimi.ingsw.Message.ClientToServerMsg;

import it.polimi.ingsw.model.PlayerColor;

/**
 * Message sent from the client to the server to choose a player's color.
 */
public class ChooseColorMsg extends GenericClientMessage {
    private PlayerColor color;

    /**
     * Constructs a ChooseColorMsg with the specified nickname, game ID, and color.
     *
     * @param nickName the nickname of the player
     * @param gameID the ID of the game
     * @param color the color chosen by the player
     */
    public ChooseColorMsg(String nickName, int gameID, PlayerColor color) {
        super(gameID, nickName);
        this.color = color;
    }

    /**
     * Constructs a ChooseColorMsg with the specified color.
     *
     * @param color the color chosen by the player
     */
    public ChooseColorMsg(PlayerColor color) {
        this.color = color;
    }

    /**
     * Gets the color chosen by the player.
     *
     * @return the chosen color
     */
    public PlayerColor getColor() {
        return color;
    }
}