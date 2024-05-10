package it.polimi.ingsw.Networking.socket;

import java.io.IOException;

public class mainServer{
    public static void main(String[] args) throws IOException {
        Server server= new Server();
        server.start(1234);
    }
}
