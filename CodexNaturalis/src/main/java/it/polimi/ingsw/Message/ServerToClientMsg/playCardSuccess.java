package it.polimi.ingsw.Message.ServerToClientMsg;

import it.polimi.ingsw.model.Match;

/**
 * Represents a message sent to clients indicating that playing a card was successful.
 * Extends {@link ActionSuccessMsg}.
 */
public class playCardSuccess extends ActionSuccessMsg {

    /**
     * Constructs a playCardSuccess message based on the provided match model.
     * @param model The match model representing the current game state.
     */
    public playCardSuccess(Match model) {
        super(model);
    }
}
