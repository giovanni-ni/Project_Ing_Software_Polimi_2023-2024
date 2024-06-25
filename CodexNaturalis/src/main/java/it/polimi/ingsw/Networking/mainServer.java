package it.polimi.ingsw.Networking;

import it.polimi.ingsw.Networking.remoteInterface.VirtualServer;
import it.polimi.ingsw.Networking.rmi.RMIServer;
import it.polimi.ingsw.Networking.socket.Server;
import it.polimi.ingsw.controller.AllMatchesController;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
// todo javadoc
public class mainServer{
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Server Socketserver = new Server();
        Socketserver.start(DefaultPort.SOCKETPORT.getNumber());

        final String serverName = "AdderServer";
        VirtualServer server = new RMIServer(AllMatchesController.getInstance());
        VirtualServer stub= (VirtualServer) UnicastRemoteObject.exportObject(server, DefaultPort.RMIPORT.getNumber());
        Registry registry= LocateRegistry.createRegistry(DefaultPort.RMIPORT.getNumber());
        registry.rebind (serverName, stub);
        System.out.println("server bound");
    }
}
