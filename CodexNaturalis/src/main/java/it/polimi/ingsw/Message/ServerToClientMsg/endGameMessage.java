package it.polimi.ingsw.Message.ServerToClientMsg;

import it.polimi.ingsw.Message.Message;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.ViewModel;

public class endGameMessage extends GenericServerMessage implements Message {
    private final ViewModel model;

    public endGameMessage(Match match) {
        this.model=new ViewModel(match);
    }

    public ViewModel getModel() {
        return this.model;
    }
}
