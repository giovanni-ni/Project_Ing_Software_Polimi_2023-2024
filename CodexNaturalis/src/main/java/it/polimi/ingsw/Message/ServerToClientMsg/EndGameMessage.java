package it.polimi.ingsw.Message.ServerToClientMsg;

import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.ViewModel;

/**
 * Represents a message sent from the server to the client indicating the end of the game.
 * It includes the final state of the game as a {@link ViewModel} object.
 */
public class EndGameMessage extends GenericServerMessage {

    private final ViewModel model;

    /**
     * Constructs an EndGameMessage with the provided Match object.
     * Initializes the ViewModel based on the provided Match.
     *
     * @param match the Match object representing the final state of the game
     */
    public EndGameMessage(Match match) {
        this.model = new ViewModel(match);
    }

    /**
     * Gets the ViewModel object containing the final state of the game.
     *
     * @return the ViewModel object
     */
    public ViewModel getModel() {
        return this.model;
    }
}
