package it.polimi.ingsw.Networking;

import it.polimi.ingsw.Networking.remoteInterface.VirtualServer;
import it.polimi.ingsw.Networking.rmi.RMIServer;
import it.polimi.ingsw.Networking.socket.Server;
import it.polimi.ingsw.controller.AllMatchesController;

import java.io.*;
import java.net.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Enumeration;

/**
 * Main class for starting the server application.
 */
public class MainServer {
    /**
     * Main method to start the server.
     *
     * @param args command line arguments (not used)
     * @throws IOException if there is an error with I/O operations
     * @throws ClassNotFoundException if a class is not found during deserialization
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Server Socketserver = new Server();
        Socketserver.start(DefaultPort.SOCKETPORT.getNumber());

        final String serverName = "AdderServer";
        VirtualServer server = new RMIServer(AllMatchesController.getInstance());
        VirtualServer stub= (VirtualServer) UnicastRemoteObject.exportObject(server, DefaultPort.RMIPORT.getNumber());
        Registry registry= LocateRegistry.createRegistry(DefaultPort.RMIPORT.getNumber());
        registry.rebind (serverName, stub);
        System.out.println("server bound");
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    if (!address.isLoopbackAddress() && address instanceof Inet4Address) {
                        System.out.println("Local IP Address: " + address.getHostAddress());
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
    }
}
