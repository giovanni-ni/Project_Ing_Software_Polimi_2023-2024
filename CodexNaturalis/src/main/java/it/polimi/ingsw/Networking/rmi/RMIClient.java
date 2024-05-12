package it.polimi.ingsw.Networking.rmi;

import it.polimi.ingsw.Networking.remoteInterface.VirtualServer;
import it.polimi.ingsw.Networking.remoteInterface.VirtualView;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RMIClient extends UnicastRemoteObject implements VirtualView {

    final List<String> playersNickname= new ArrayList<>();
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

    public void showUpdate(Integer number )throws RemoteException{

    }
}
