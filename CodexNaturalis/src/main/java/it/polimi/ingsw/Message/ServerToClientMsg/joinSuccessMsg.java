package it.polimi.ingsw.Message.ServerToClientMsg;

import it.polimi.ingsw.model.Match;

/**
 * Represents a message indicating successful joining of a game.
 * Extends {@link ActionSuccessMsg}.
 */
public class joinSuccessMsg extends ActionSuccessMsg {

    /**
     * Constructs a joinSuccessMsg with the given match model.
     * @param model The match model representing the current game state.
     */
    public joinSuccessMsg(Match model) {
        super(model);
    }
}
