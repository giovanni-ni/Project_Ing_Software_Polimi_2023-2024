package it.polimi.ingsw.Message.ServerToClientMsg;

import it.polimi.ingsw.model.Match;

public class joinSuccessMsg extends ActionSuccessMsg {
    public joinSuccessMsg(Match model) {
        super(model);
    }
}
