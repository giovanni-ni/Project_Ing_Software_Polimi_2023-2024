package it.polimi.ingsw.Message.ServerToClientMsg;

import it.polimi.ingsw.model.Match;

/**
 * Represents a message sent to clients indicating that playing a card was successful.
 * Extends {@link ActionSuccessMsg}.
 */
public class PlayCardSuccess extends ActionSuccessMsg {

    /**
     * Constructs a PlayCardSuccess message based on the provided match model.
     * @param model The match model representing the current game state.
     */
    public PlayCardSuccess(Match model) {
        super(model);
    }
}
