package it.polimi.ingsw.Networking.socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class mainServer{
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Server server = new Server();
        server.start(1234);

    }
}
