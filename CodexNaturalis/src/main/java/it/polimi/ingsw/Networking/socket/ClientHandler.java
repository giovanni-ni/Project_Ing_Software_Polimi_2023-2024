package it.polimi.ingsw.Networking.socket;

import it.polimi.ingsw.Message.ClientToServerMsg.CreateGameMessage;
import it.polimi.ingsw.Message.ClientToServerMsg.GenericClientMessage;
import it.polimi.ingsw.Message.ClientToServerMsg.JoinFirstMessage;
import it.polimi.ingsw.Networking.Listeners.GameListener;
import it.polimi.ingsw.model.Player;

import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ClientHandler extends Thread {

    private final Socket clientSocket;

    private ObjectInputStream inputStream;

    private ObjectOutputStream outputStream;

    private String nickname;

    private final BlockingQueue<GenericClientMessage> processingQueue = new LinkedBlockingQueue<>();

    private GameListener gameListener;

    private Server server;
    public ClientHandler(Socket soc, Server server) throws IOException {
        this.clientSocket = soc;
        this.inputStream = new ObjectInputStream(soc.getInputStream());
        this.outputStream = new ObjectOutputStream(soc.getOutputStream());
        this.gameListener=new GameListener(outputStream);
        this.server = server;
        this.start();

    }

    public void interruptThread() {
        this.interrupt();
    }

    private void runGameLogic()  {
        GenericClientMessage temp;
        try{
            while (!this.isInterrupted()) {

                temp = processingQueue.take();

                this.server.controllers.addInQueue(temp, gameListener);

            }
        //} catch (RemoteException e) {
            //throw new RuntimeException(e);
        } catch (InterruptedException ignored) {}

    }

    @Override
    public void run() {
        Thread th = new Thread(this::runGameLogic);
        th.start();

        try {
            GenericClientMessage temp;
            while (!this.isInterrupted()) {
                try {
                    temp = (GenericClientMessage) inputStream.readObject();

                    processingQueue.add(temp);

                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("ClientSocket cannot communicate no more with the client");
                    return;
                }
            }
        } finally {
            th.interrupt();
        }
    }
}