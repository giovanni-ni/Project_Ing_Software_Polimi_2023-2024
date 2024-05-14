package it.polimi.ingsw.Networking.rmi;

import it.polimi.ingsw.Message.Message;
import it.polimi.ingsw.Networking.Listeners.GameListener;
import it.polimi.ingsw.Networking.Listeners.Listener;
import it.polimi.ingsw.Networking.remoteInterface.VirtualServer;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RMIClient extends UnicastRemoteObject implements Listener {

    final List<String> playersNickname= new ArrayList<>();

    private String nickname;

    private Integer gameId;
    final VirtualServer server;
    public RMIClient(VirtualServer server) throws RemoteException {
        this.server=server;
    }
    private void run() throws RemoteException{
        this.server.connect(this);
        this.runCli();
    }

    private void runCli() throws RemoteException{
        }

    public static void main(String [] args) throws RemoteException, NotBoundException{

        final String serverName = "AdderServer";

        Registry registry = LocateRegistry.getRegistry("localhost", 1234);
        VirtualServer server = (VirtualServer) registry.lookup(serverName);
        new RMIClient(server).run();
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
}
