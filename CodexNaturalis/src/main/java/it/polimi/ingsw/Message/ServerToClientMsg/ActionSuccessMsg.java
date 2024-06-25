package it.polimi.ingsw.Message.ServerToClientMsg;

import it.polimi.ingsw.model.*;

/**
 * Represents a message sent from the server to the client indicating that an action was successfully executed.
 * It includes the current state of the game as a {@link ViewModel} object.
 */
public class ActionSuccessMsg extends GenericServerMessage {

    private final ViewModel model;

    /**
     * Constructs an ActionSuccessMsg with the provided Match object.
     * Initializes the ViewModel based on the provided Match.
     *
     * @param match the Match object representing the current state of the game
     */
    public ActionSuccessMsg(Match match) {
        this.model = new ViewModel(match);
    }

    /**
     * Gets the ViewModel object containing the current state of the game.
     *
     * @return the ViewModel object
     */
    public ViewModel getModel() {
        return model;
    }

}
