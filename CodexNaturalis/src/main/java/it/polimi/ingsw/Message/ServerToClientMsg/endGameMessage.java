package it.polimi.ingsw.Message.ServerToClientMsg;

import it.polimi.ingsw.Message.Message;
import it.polimi.ingsw.model.Match;

public class endGameMessage implements Message {
    private final Match model;

    public endGameMessage(Match match) {
        this.model=match;
    }
}
