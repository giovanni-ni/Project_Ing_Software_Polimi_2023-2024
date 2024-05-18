package it.polimi.ingsw.Message.ServerToClientMsg;

import it.polimi.ingsw.model.*;

import static it.polimi.ingsw.view.TextualInterfaceUnit.Print.print;

public class ActionSuccessMsg extends GenericServerMessage{

    private final ViewModel model;

    public ActionSuccessMsg(Match match) {
        this.model = new ViewModel(match);
    }

    public ViewModel getModel() {
        return model;
    }

}
