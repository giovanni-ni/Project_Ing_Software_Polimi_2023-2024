package it.polimi.ingsw.Networking.socket;

import it.polimi.ingsw.controller.AllMatchesController;
import it.polimi.ingsw.controller.SingleMatchController;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

/**
 * The Server class handles incoming client connections and manages the lifecycle of the server.
 * It accepts client connections and assigns a ClientHandler to each connected client.
 */
public class Server extends Thread {

    private ServerSocket serverSocket;

    private List<ClientHandler> handler;

    protected AllMatchesController controllers;

    /**
     * Starts the server on the specified port.
     *
     * @param port the port number on which the server listens for incoming connections
     * @throws IOException if an I/O error occurs when opening the socket
     */
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
    /**
     * Runs the server thread.
     * Accepts new client connections and assigns a ClientHandler to each.
     */
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
    /**
     * Stops the server and interrupts all client handler threads.
     */
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