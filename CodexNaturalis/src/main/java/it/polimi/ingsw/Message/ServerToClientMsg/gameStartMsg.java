package it.polimi.ingsw.Message.ServerToClientMsg;

import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.ViewModel;

/**
 * Represents a message sent from the server to the client indicating the start of a game.
 * Extends {@link ActionSuccessMsg} and inherits the ViewModel containing the initial state of the game.
 */
public class gameStartMsg extends ActionSuccessMsg {

    private final ViewModel viewModel;

    /**
     * Constructs a gameStartMsg with the provided Match object.
     * Initializes the ViewModel based on the provided Match.
     *
     * @param match the Match object representing the initial state of the game
     */
    public gameStartMsg(Match match) {
        super(match);
        this.viewModel = new ViewModel(match);
    }

    /**
     * Gets the ViewModel object containing the initial state of the game.
     *
     * @return the ViewModel object
     */
    public ViewModel getViewModel() {
        return viewModel;
    }
}
