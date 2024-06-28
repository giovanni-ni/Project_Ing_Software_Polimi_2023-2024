package it.polimi.ingsw.Networking.rmi;

import it.polimi.ingsw.Message.ClientToServerMsg.GenericClientMessage;
import it.polimi.ingsw.Message.ServerToClientMsg.ActionSuccessMsg;
import it.polimi.ingsw.Networking.Listeners.Listener;
import it.polimi.ingsw.Networking.remoteInterface.VirtualServer;
import it.polimi.ingsw.controller.AllMatchesController;
import it.polimi.ingsw.model.Match;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * The RMIServer class implements the VirtualServer interface and serves as the RMI server.
 * It manages client connections and forwards client messages to the main controller.
 */
public class RMIServer implements VirtualServer {

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    public boolean clientAlive = false;
    private final AllMatchesController mainController;
    final List<Listener> clients = new ArrayList<>();

    final BlockingQueue<Integer> updates = new ArrayBlockingQueue<>(100);
    /**
     * Constructs an RMIServer with the given AllMatchesController.
     *
     * @param mainController the main controller that manages all matches
     * @throws IOException if there is a problem with IO operations
     */
    public RMIServer(AllMatchesController mainController ) throws IOException {
        this.mainController=mainController;
    }
    @Override
    public void receiveHeartbeat() throws RemoteException {
        clientAlive = true;
    }
    /**
     * Connects a client to the server and sends an initial success message.
     *
     * @param client the client to connect
     * @throws IOException if there is a problem with IO operations
     */
    @Override
    public void connect(Listener client) throws IOException {
        synchronized (this.clients){
            client.update(new ActionSuccessMsg(new Match(0)));
            this.clients.add(client);
            scheduler.scheduleAtFixedRate(() -> {
                if (clientAlive) {
                    try {
                        System.out.println(client.getNickname()+"is alive");
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                    clientAlive = false; // Reset the flag
                } else {
                    try {
                        System.out.println(client.getNickname()+"is disconnected");
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }
            }, 0, 5, TimeUnit.SECONDS); // Check client status every 5 seconds
        }
    }
    /**
     * Adds a client message to the main controller's processing queue.
     *
     * @param msg    the message from the client
     * @param client the client sending the message
     * @throws RemoteException if there is a problem with remote method invocation
     */
    @Override
    public void addInQueue(GenericClientMessage msg, Listener client) throws RemoteException {
        mainController.addInQueue(msg,client);
    }
}
