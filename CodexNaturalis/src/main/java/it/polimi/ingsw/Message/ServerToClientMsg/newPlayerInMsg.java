package it.polimi.ingsw.Message.ServerToClientMsg;

import it.polimi.ingsw.Message.Message;
import it.polimi.ingsw.model.Match;

import java.io.Serializable;

/**
 * Represents a message sent to clients when a new player joins the game.
 * Extends {@link ActionSuccessMsg}.
 * Implements {@link Message} and {@link java.io.Serializable}.
 */
public class newPlayerInMsg extends ActionSuccessMsg implements Message, Serializable {

    /**
     * Constructs a newPlayerInMsg object.
     * @param model The Match object representing the current state of the game.
     */
    public newPlayerInMsg(Match model) {
        super(model);
    }

    /**
     * Retrieves the nickname of the new player who just joined the game.
     * @return The nickname of the new player.
     */
    public String getNicknameNewPlayer() {
        return getModel().getPlayers().getLast().getNickname();
    }
}
