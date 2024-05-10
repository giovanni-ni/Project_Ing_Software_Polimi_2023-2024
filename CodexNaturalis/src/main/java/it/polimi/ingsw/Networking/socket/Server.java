package it.polimi.ingsw.Networking.socket;

import it.polimi.ingsw.controller.AllMatchesController;
import it.polimi.ingsw.controller.SingleMatchController;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread{

    private ServerSocket serverSocket;

    private List<ClientHandler> handler;

    protected AllMatchesController controllers;

    public void start(int port) throws IOException {
        try {
            serverSocket = new ServerSocket(port);
            handler = new ArrayList<>();
            this.start();
            System.out.println("Server Socket ready");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            while (!Thread.interrupted()) {
                handler.add(new ClientHandler(serverSocket.accept(),this));
                handler.get(handler.size() - 1).start();
                System.out.println("new connection accepted");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void stopConnection() {
        if (handler != null)
            for (ClientHandler c : handler) {
                c.interruptThread();
            }
        this.interrupt();
    }
}
