package it.polimi.ingsw.Networking.rmi;

import it.polimi.ingsw.Message.ClientToServerMsg.GenericClientMessage;
import it.polimi.ingsw.Message.Message;
import it.polimi.ingsw.Networking.Listeners.Listener;
import it.polimi.ingsw.Networking.remoteInterface.VirtualServer;
import it.polimi.ingsw.view.TextualInterfaceUnit.Client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class RMIClient extends UnicastRemoteObject implements Listener, Client {

    final List<String> playersNickname= new ArrayList<>();

    private String nickname;

    private Integer gameId;
    final VirtualServer server;
    public RMIClient(VirtualServer server) throws RemoteException {
        this.server=server;
    }
    public void run() throws RemoteException{
        this.server.connect(this);
    }

    @Override
    public void update(Message msg) {

    }

    @Override
    public void setNickname(String nickname) {
        this.nickname=nickname;
    }

    @Override
    public String getNickname() {
        return this.nickname;
    }

    @Override
    public Integer getGameID() {
        return this.gameId;
    }

    @Override
    public void setGameID(Integer gameID) {
        this.gameId=gameID;
    }

    @Override
    public void messageToServer(GenericClientMessage msg) {

    }
}
