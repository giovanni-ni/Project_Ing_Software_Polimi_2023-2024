package it.polimi.ingsw.Message.ServerToClientMsg;

import it.polimi.ingsw.Message.Message;
import it.polimi.ingsw.model.Match;

public class drawCardSuccess extends ActionSuccessMsg {
    public drawCardSuccess(Match match) {
        super(match);
    }

}
