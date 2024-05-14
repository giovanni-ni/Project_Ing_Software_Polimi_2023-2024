package it.polimi.ingsw.view.TextualInterfaceUnit;

import it.polimi.ingsw.Message.ClientToServerMsg.GenericClientMessage;

public interface Client {
    public void messageToServer(GenericClientMessage msg);
}
