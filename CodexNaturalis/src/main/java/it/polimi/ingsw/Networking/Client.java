package it.polimi.ingsw.Networking;

import it.polimi.ingsw.Message.ClientToServerMsg.GenericClientMessage;

import java.rmi.RemoteException;

public interface Client {
    public void messageToServer(GenericClientMessage msg)throws RemoteException;
}
