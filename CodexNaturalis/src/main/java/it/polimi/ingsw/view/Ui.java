package it.polimi.ingsw.view;

import it.polimi.ingsw.Message.Message;
import it.polimi.ingsw.Message.ServerToClientMsg.GenericServerMessage;

import java.io.IOException;
import java.rmi.RemoteException;

public interface Ui {

   public void handleMessage(Message msg);



}


