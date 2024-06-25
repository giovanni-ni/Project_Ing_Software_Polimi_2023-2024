package it.polimi.ingsw.Message.ClientToServerMsg;

import it.polimi.ingsw.Networking.Listeners.SocketListener;

import java.io.Serializable;

/**
 * Message sent from the client to the server to create a new game.
 */
public class CreateGameMessage extends JoinGameMessage implements Serializable {
    private int limitPly = 0;

    /**
     * Constructs a CreateGameMessage with the specified nickname.
     *
     * @param nickname the nickname of the player creating the game
     */
    public CreateGameMessage(String nickname) {
        super(nickname, 0);
        super.isMessageForMain();
    }

    /**
     * Constructs a CreateGameMessage with the specified nickname and player limit.
     *
     * @param nickname the nickname of the player creating the game
     * @param limitPly the limit on the number of players for the game, must be between 2 and 4
     */
    public CreateGameMessage(String nickname, int limitPly) {
        super(nickname, 0);
        if (limitPly >= 2 && limitPly <= 4)
            this.limitPly = limitPly;
        super.isMessageForMain();
    }

    /**
     * Gets the limit on the number of players for the game.
     *
     * @return the limit on the number of players for the game
     */
    public int getLimitPly() {
        return limitPly;
    }
}
