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

    @Override
    public void connect(Listener client) throws RemoteException {
        synchronized (this.clients){
            this.clients.add(client);
        }
    }
    @Override
    public void addInQueue(GenericClientMessage msg, Listener client) throws RemoteException {
        mainController.addInQueue(msg,client);
    }
}
