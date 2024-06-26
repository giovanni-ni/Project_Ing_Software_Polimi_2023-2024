package it.polimi.ingsw.Message.ServerToClientMsg;

import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.ViewModel;

/**
 * Represents a message sent by the server to notify the client that reconnection was successful.
 * Extends {@link GenericServerMessage}.
 */
public class ReconnectSuccess extends GenericServerMessage{

    private final ViewModel model;

    /**
     * Constructs a {@code ReconnectSuccess} message with the provided match details.
     *
     * @param match the match object containing the state of the game for reconnection
     */
    public ReconnectSuccess(Match match) {
        this.model = new ViewModel(match);
    }

    /**
     * Retrieves the view model containing the current state of the game after reconnection.
     *
     * @return the view model representing the game state
     */
    public ViewModel getModel() {
        return model;
    }
}
