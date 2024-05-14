package it.polimi.ingsw.Networking.rmi;

import it.polimi.ingsw.Message.ClientToServerMsg.GenericClientMessage;
import it.polimi.ingsw.Networking.Listeners.Listener;
import it.polimi.ingsw.Networking.remoteInterface.VirtualServer;
import it.polimi.ingsw.controller.AllMatchesController;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class RMIServer implements VirtualServer {

    private final AllMatchesController mainController;
    final List<Listener> clients = new ArrayList<>();

    final BlockingQueue<Integer> updates = new ArrayBlockingQueue<>(100);

    public RMIServer(AllMatchesController mainController ) throws IOException {
        this.mainController=mainController;
    }

    public static void main(String []args) throws IOException {
        final String serverName = "AdderServer";
        VirtualServer server = new RMIServer(AllMatchesController.getInstance());
        VirtualServer stub= (VirtualServer) UnicastRemoteObject.exportObject(server, 0);
        Registry registry= LocateRegistry.createRegistry(1234);

        registry.rebind (serverName, stub);
        System.out.println("server bound");
    }
    @Override
    public void connect(Listener client) throws RemoteException {
        synchronized (this.clients){
            this.clients.add(client);
        }
    }
    @Override
    public void addInQueue(GenericClientMessage msg, Listener client) {
        mainController.addInQueue(msg,client);
    }

}
