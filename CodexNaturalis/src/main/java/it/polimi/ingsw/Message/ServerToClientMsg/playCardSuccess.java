package it.polimi.ingsw.Message.ServerToClientMsg;

import it.polimi.ingsw.Message.Message;
import it.polimi.ingsw.model.Match;

public class playCardSuccess extends ActionSuccessMsg {

    public playCardSuccess(Match model) {
        super(model);
    }
}
