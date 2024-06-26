package it.polimi.ingsw.view;

import it.polimi.ingsw.Message.Message;
import it.polimi.ingsw.Message.ServerToClientMsg.GenericServerMessage;

import java.io.IOException;
import java.rmi.RemoteException;

/**
 * Interface representing the user interface (UI) for handling messages from a generic server.
 * Implementations of this interface can process server messages received by the UI.
 */
public interface Ui {

   /**
    * Handles a generic server message received by the UI.
    *
    * @param msg the GenericServerMessage to be handled {@link GenericServerMessage}
    */
   public void handleMessage(GenericServerMessage msg);

}


