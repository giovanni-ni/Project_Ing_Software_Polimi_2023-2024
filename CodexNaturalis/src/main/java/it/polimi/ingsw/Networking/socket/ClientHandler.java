package it.polimi.ingsw.Networking.socket;

import it.polimi.ingsw.Message.ClientToServerMsg.CrashMsg;
import it.polimi.ingsw.Message.ClientToServerMsg.CreateGameMessage;
import it.polimi.ingsw.Message.ClientToServerMsg.GenericClientMessage;
import it.polimi.ingsw.Message.ServerToClientMsg.ActionSuccessMsg;
import it.polimi.ingsw.Networking.Listeners.SocketListener;
import it.polimi.ingsw.Networking.Listeners.SocketListener;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Player;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The ClientHandler class handles communication between the server and a connected client.
 * It reads messages from the client, processes them, and sends responses back.
 */
public class ClientHandler extends Thread {

    private final Socket clientSocket;

    private ObjectInputStream inputStream;

    private ObjectOutputStream outputStream;

    private String nickname;

    private final BlockingQueue<GenericClientMessage> processingQueue = new LinkedBlockingQueue<>();


    private final SocketListener gameListener;
    private final Server server;
    /**
     * Constructs a ClientHandler for a given client socket and server.
     *
     * @param soc    the client socket
     * @param server the server to which the client is connected
     * @throws IOException if an I/O error occurs when creating the input and output streams
     */
    public ClientHandler(Socket soc, Server server) throws IOException {
        this.clientSocket = soc;
        this.inputStream = new ObjectInputStream(soc.getInputStream());
        this.outputStream = new ObjectOutputStream(soc.getOutputStream());
        this.gameListener=new SocketListener(outputStream);
        this.server = server;
        //this.start();


    }

    /**
     * Interrupts the client handler thread.
     */
    public void interruptThread() {
        this.interrupt();
    }

    /**
     * Runs the client handler thread.
     * Reads messages from the client and processes them.
     */
    @Override
    public void run() {
        try {
            gameListener.update(new ActionSuccessMsg(new Match(0)));
            GenericClientMessage temp;
            while (!this.isInterrupted()) {
                try {
                    temp = (GenericClientMessage) inputStream.readObject();
                    System.out.println("ricevuto messaggio" + temp);
                    server.controllers.addInQueue(temp,gameListener);

                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("ClientSocket cannot communicate no more with the client");
                    server.controllers.addInQueue(new CrashMsg(gameListener.getNickname()),gameListener);
                    return;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}