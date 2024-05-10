package it.polimi.ingsw.Message.ServerToClientMsg;

import it.polimi.ingsw.model.Match;

public class gameStartMsg extends ActionSuccessMsg {
    public gameStartMsg(Match match) {
        super(match);
    }
}
