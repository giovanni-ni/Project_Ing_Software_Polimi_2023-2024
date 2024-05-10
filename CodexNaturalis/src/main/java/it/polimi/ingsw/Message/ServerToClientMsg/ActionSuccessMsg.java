package it.polimi.ingsw.Message.ServerToClientMsg;

import it.polimi.ingsw.model.Match;

public class ActionSuccessMsg extends GenericServerMessage{
    private final Match model;

    public ActionSuccessMsg(Match match) {
        this.model=match;
    }
}
