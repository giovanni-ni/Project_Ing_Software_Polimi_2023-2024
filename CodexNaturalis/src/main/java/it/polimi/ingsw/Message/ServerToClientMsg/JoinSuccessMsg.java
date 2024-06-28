package it.polimi.ingsw.Message.ServerToClientMsg;

import it.polimi.ingsw.model.Match;

/**
 * Represents a message indicating successful joining of a game.
 * Extends {@link ActionSuccessMsg}.
 */
public class JoinSuccessMsg extends ActionSuccessMsg {

    /**
     * Constructs a JoinSuccessMsg with the given match model.
     * @param model The match model representing the current game state.
     */
    public JoinSuccessMsg(Match model) {
        super(model);
    }
}
