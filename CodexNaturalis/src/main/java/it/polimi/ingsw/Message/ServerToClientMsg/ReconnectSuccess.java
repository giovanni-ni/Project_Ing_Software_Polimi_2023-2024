package it.polimi.ingsw.Message.ServerToClientMsg;

import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.ViewModel;

public class ReconnectSuccess extends GenericServerMessage{

    private final ViewModel model;

    public ReconnectSuccess(Match match) {
        this.model = new ViewModel(match);
    }

    public ViewModel getModel() {
        return model;
    }
}
