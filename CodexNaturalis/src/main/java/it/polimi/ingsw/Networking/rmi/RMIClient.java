package it.polimi.ingsw.Networking.rmi;

import it.polimi.ingsw.Message.ClientToServerMsg.GenericClientMessage;
import it.polimi.ingsw.Message.Message;
import it.polimi.ingsw.Message.ServerToClientMsg.*;
import it.polimi.ingsw.Networking.Listeners.Listener;
import it.polimi.ingsw.Networking.remoteInterface.VirtualServer;
import it.polimi.ingsw.Networking.Client;
import it.polimi.ingsw.view.Ui;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The RMIClient class is responsible for connecting to a remote RMI server and handling communication.
 */
public class RMIClient extends UnicastRemoteObject implements Listener, Client {
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private String nickname;
    private Integer gameId = -1;
    private VirtualServer server;
    private Ui ui;
    /**
     * Constructs an RMIClient that connects to a remote RMI server.
     *
     * @param ip   the IP address of the remote server
     * @param port the port number on which the remote server is listening
     * @param ui   the user interface object to handle messages and interactions
     * @throws RemoteException if there is a problem with the remote method invocation
     */
    public RMIClient(String ip, int port, Ui ui) throws RemoteException {
        try{
            this.ui = ui;
            String serverName = "AdderServer";
            Registry registry = LocateRegistry.getRegistry(ip, port);
            this.server = (VirtualServer) registry.lookup(serverName);
            this.server.connect(this);
            scheduler.scheduleAtFixedRate(() -> {
                try {
                    server.receiveHeartbeat();
                } catch (RemoteException e) {
                    ui.handleMessage(new LeaveMessage("Server"));
                    scheduler.shutdown();
                }
            }, 0, 1, TimeUnit.SECONDS); // Send heartbeat every 5 seconds
        }catch (Exception e){
            ui.handleMessage(new ActionNotRecognize("Connection failed"));
        }

    }
    /**
     * Updates the client with a message from the server.
     *
     * @param msg the message from the server
     * @throws RemoteException if there is a problem with the remote method invocation
     */
    @Override
    public void update(Message msg) throws RemoteException{
        ui.handleMessage((GenericServerMessage) msg);
    }
    /**
     * Sets the nickname of the player.
     *
     * @param nickname the player's nickname
     * @throws RemoteException if there is a problem with the remote method invocation
     */

    @Override
    public void setNickname(String nickname) throws RemoteException{
        this.nickname=nickname;
    }
    /**
     * Gets the nickname of the player.
     *
     * @return the player's nickname
     * @throws RemoteException if there is a problem with the remote method invocation
     */
    @Override
    public String getNickname() throws RemoteException {
        return this.nickname;
    }

    /**
     * Gets the game ID associated with the player.
     *
     * @return the game ID
     * @throws RemoteException if there is a problem with the remote method invocation
     */
    @Override
    public Integer getGameID() throws RemoteException {
        return this.gameId;
    }
    /**
     * Sets the game ID associated with the player.
     *
     * @param gameID the game ID
     * @throws RemoteException if there is a problem with the remote method invocation
     */
    @Override
    public void setGameID(Integer gameID) throws RemoteException {
        this.gameId=gameID;
    }
    /**
     * Sends a message to the server.
     *
     * @param msg the message to send to the server
     * @throws RemoteException if there is a problem with the remote method invocation
     */
    @Override
    public void messageToServer(GenericClientMessage msg) throws RemoteException {
        server.addInQueue(msg, this );
    }

    @Override
    public void heartBeat() throws RemoteException {
    }
}
