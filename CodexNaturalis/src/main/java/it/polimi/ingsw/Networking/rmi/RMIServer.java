package it.polimi.ingsw.Networking.rmi;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import it.polimi.ingsw.Message.ClientToServerMsg.CrashMsg;
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
    final BiMap <Listener,CrashMsg> crashMsgBiMap = HashBiMap.create();
    private Listener crashedClient;

    final BlockingQueue<Integer> updates = new ArrayBlockingQueue<>(100);
    /**
     * Constructs an RMIServer with the given AllMatchesController.
     *
     * @param mainController the main controller that manages all matches
     * @throws IOException if there is a problem with IO operations
     */
    public RMIServer(AllMatchesController mainController ) throws IOException {
        this.mainController=mainController;
        String nickname =null;
        scheduler.scheduleAtFixedRate(() -> {
            for (Listener c : clients){
                try {
                    c.heartBeat();
                    crashMsgBiMap.put(c,new CrashMsg(c.getNickname(),c.getGameID(),c));

                } catch(RemoteException e){

                        CrashMsg msg = crashMsgBiMap.get(c);
                        mainController.addInQueue(msg,c);

                        crashedClient =c;
                }
            }
            clients.remove(crashedClient);
        }, 0, 1, TimeUnit.SECONDS);
    }

    @Override
    public void receiveHeartbeat() throws RemoteException {

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
            this.crashMsgBiMap.put(client,new CrashMsg(client.getNickname(),client.getGameID(),client));
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
