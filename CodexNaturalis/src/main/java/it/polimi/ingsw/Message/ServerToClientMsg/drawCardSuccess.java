package it.polimi.ingsw.Message.ServerToClientMsg;

import it.polimi.ingsw.model.Match;

/**
 * Represents a message sent from the server to the client indicating that a card draw action was successful.
 * Extends {@link ActionSuccessMsg} and inherits the ViewModel containing the current state of the game.
 */
public class drawCardSuccess extends ActionSuccessMsg {

    /**
     * Constructs a drawCardSuccess message with the provided Match object.
     *
     * @param match the Match object representing the current state of the game
     */
    public drawCardSuccess(Match match) {
        super(match);
    }

}
