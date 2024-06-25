package it.polimi.ingsw.Networking.rmi;

import it.polimi.ingsw.Message.ClientToServerMsg.GenericClientMessage;
import it.polimi.ingsw.Message.Message;
import it.polimi.ingsw.Message.ServerToClientMsg.*;
import it.polimi.ingsw.Networking.Listeners.Listener;
import it.polimi.ingsw.Networking.remoteInterface.VirtualServer;
import it.polimi.ingsw.model.PlayerStatus;
import it.polimi.ingsw.Networking.Client;
import it.polimi.ingsw.view.TextualInterfaceUnit.Tui;
import it.polimi.ingsw.view.Ui;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.view.TextualInterfaceUnit.Print.print;
import static it.polimi.ingsw.view.TextualInterfaceUnit.Print.showNewChatMessage;
// todo javadoc
public class RMIClient extends UnicastRemoteObject implements Listener, Client {
    private String nickname;

    private Integer gameId;
    private VirtualServer server;
    private Ui ui;

    public RMIClient(String ip, int port, Ui ui) throws RemoteException {
        try{
            this.ui = ui;
            String serverName = "AdderServer";
            Registry registry = LocateRegistry.getRegistry(ip, port);
            this.server = (VirtualServer) registry.lookup(serverName);
            this.server.connect(this);
        }catch (Exception e){
            ui.handleMessage(new ActionNotRecognize("Connection failed"));
        }

    }

    @Override
    public void update(Message msg) throws RemoteException{
        ui.handleMessage((GenericServerMessage) msg);
    }

    @Override
    public void setNickname(String nickname) throws RemoteException{
        this.nickname=nickname;
    }

    @Override
    public String getNickname() throws RemoteException {
        return this.nickname;
    }

    @Override
    public Integer getGameID() throws RemoteException {
        return this.gameId;
    }

    @Override
    public void setGameID(Integer gameID) throws RemoteException {
        this.gameId=gameID;
    }

    @Override
    public void messageToServer(GenericClientMessage msg) throws RemoteException {
        server.addInQueue(msg, this );
    }
}
