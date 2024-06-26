package it.polimi.ingsw.Networking.socket;

import it.polimi.ingsw.controller.AllMatchesController;
import it.polimi.ingsw.controller.SingleMatchController;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread {

    private ServerSocket serverSocket;

    private List<ClientHandler> handler;

    protected AllMatchesController controllers;

    public void start(int port) throws IOException {
        try {
            serverSocket = new ServerSocket(port);
            handler = new ArrayList<>();
            controllers = AllMatchesController.getInstance();
            this.start();
            System.out.println("Server Socket ready");
            //loadMatchFromFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {

                handler.add(new ClientHandler(serverSocket.accept(), this));
                System.out.println("new connection accepted");
                handler.getLast().start();
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

    /*public void loadMatchFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("salvataggio.ser"))) {
            this.controllers = new AllMatchesController((ArrayList<SingleMatchController>) ois.readObject());
            System.out.println(controllers.getControllerbyId(0).getMatch().getPlayers().get(0).getNickname());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }*/
}