package it.polimi.ingsw.Message.ServerToClientMsg;

import it.polimi.ingsw.Message.Message;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.ViewModel;

import java.io.Serializable;

public class newPlayerInMsg extends ActionSuccessMsg implements Message, Serializable {


    public newPlayerInMsg(Match model) {
        super(model);

    }

    public String getNicknameNewPlayer() {
        return getModel().getPlayers().getLast().getNickname();
    }


}
