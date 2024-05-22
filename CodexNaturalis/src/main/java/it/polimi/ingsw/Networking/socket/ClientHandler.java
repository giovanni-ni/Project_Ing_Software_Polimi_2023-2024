package it.polimi.ingsw.Networking.socket;

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

public class ClientHandler extends Thread {

    private final Socket clientSocket;

    private ObjectInputStream inputStream;

    private ObjectOutputStream outputStream;

    private String nickname;

    private final BlockingQueue<GenericClientMessage> processingQueue = new LinkedBlockingQueue<>();


    private final SocketListener gameListener;
    private final Server server;
    public ClientHandler(Socket soc, Server server) throws IOException {
        this.clientSocket = soc;
        this.inputStream = new ObjectInputStream(soc.getInputStream());
        this.outputStream = new ObjectOutputStream(soc.getOutputStream());
        this.gameListener=new SocketListener(outputStream);
        this.server = server;
        //this.start();


    }

    public void interruptThread() {
        this.interrupt();
    }


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
                    return;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}