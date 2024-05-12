package it.polimi.ingsw.Networking.rmi;

import it.polimi.ingsw.Networking.remoteInterface.VirtualServer;
import it.polimi.ingsw.Networking.remoteInterface.VirtualView;
import it.polimi.ingsw.controller.SingleMatchController;
import it.polimi.ingsw.model.Coordinate;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class RMIServer implements VirtualServer {

    private SingleMatchController controller;

    final List<VirtualView> clients = new ArrayList<>();

    final BlockingQueue<Integer> updates = new ArrayBlockingQueue<>(100);

    public RMIServer(SingleMatchController singleMatchController) {
        this.controller=singleMatchController;
    }

    public static void main(String []args) throws IOException {
        final String serverName = "AdderServer";
        final int i=0;
        VirtualServer server = new RMIServer(new SingleMatchController(i));
        VirtualServer stub= (VirtualServer) UnicastRemoteObject.exportObject(server, 0);
        Registry registry= LocateRegistry.createRegistry(1234);

        registry.rebind (serverName, stub);
        System.out.println("server bound");

    }
    @Override
    public void connect(VirtualView client) throws RemoteException {
        synchronized (this.clients){
            this.clients.add(client);
        }
    }
    @Override
    public void getACard(String nickname, boolean isGold, int whichCard) {

    }

    @Override
    public void playACard(String nickname, int indexCardOnHand, Coordinate coo, boolean isFront) {

    }
}
