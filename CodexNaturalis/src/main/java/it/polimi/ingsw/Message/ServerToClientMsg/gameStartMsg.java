package it.polimi.ingsw.Message.ServerToClientMsg;

import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.ViewModel;

public class gameStartMsg extends ActionSuccessMsg {
    private final ViewModel viewModel;


    public gameStartMsg(Match match) {
        super(match);
        this.viewModel = new ViewModel(match);
    }
}
