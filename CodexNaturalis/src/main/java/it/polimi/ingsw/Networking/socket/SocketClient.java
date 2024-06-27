package it.polimi.ingsw.Networking.socket;


import it.polimi.ingsw.Message.ClientToServerMsg.*;
import it.polimi.ingsw.Message.ServerToClientMsg.*;
import it.polimi.ingsw.model.PlayerStatus;
import it.polimi.ingsw.Networking.Client;
import it.polimi.ingsw.view.TextualInterfaceUnit.Tui;
import it.polimi.ingsw.view.Ui;


import java.io.*;
import java.net.Socket;

import static it.polimi.ingsw.view.TextualInterfaceUnit.Print.print;
import static it.polimi.ingsw.view.TextualInterfaceUnit.Print.showNewChatMessage;
/**
 * The SocketClient class handles the client-side socket communication with the server.
 * It manages sending messages to and receiving messages from the server.
 */
public class SocketClient extends Thread implements Client {
    private Socket socket;
    private static ObjectInputStream inputStream;
    private static ObjectOutputStream outputStream;
    private String serverAddress;
    private int serverPort;
    private String nickname;
    private int GameId;
    private Ui ui;
    /**
     * Constructs a SocketClient that connects to the specified server address and port.
     *
     * @param address the IP address of the server
     * @param port the port number on which the server is listening
     * @param ui the user interface object to handle messages and interactions
     * @throws IOException if an I/O error occurs when creating the socket
     */
    public SocketClient(String address, int port, Ui ui) throws IOException {
        this.serverAddress = address;
        this.serverPort = port;
        try{
            this.ui =ui;
            socket = new Socket(serverAddress, serverPort);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
            this.start();
        }catch (IOException | RuntimeException e ){
            ui.handleMessage(new ActionNotRecognize("Connection failed"));
        }

    }
    /**
     * Runs the client thread.
     * Continuously listens for messages from the server and handles them.
     */
    public void run() {
        boolean Server_ok=true;
        while (Server_ok) {
            try {
                GenericServerMessage msg = (GenericServerMessage) inputStream.readObject();
                ui.handleMessage(msg);
            } catch (ClassNotFoundException e) {
                print("ClassNotFound");
            } catch (IOException e) {
                print("IOException Server Connection failed");
                Server_ok=false;
            }catch (NullPointerException e){
                print("Connection has a problem, network is not reading");
                e.printStackTrace();
            }
        }
    }

    /**
     * Stops the connection by closing the input and output streams and the socket.
     *
     * @throws IOException if an I/O error occurs when closing the socket
     */

    public void stopConnection() throws IOException {
        inputStream.close();
        outputStream.close();
        socket.close();
    }
    /**
     * Sends a message to the server.
     *
     * @param message the message to be sent to the server
     */
    @Override
    public void messageToServer(GenericClientMessage message) {
        try {
            outputStream.writeObject(message);
            finishSending();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Flushes and resets the output stream after sending a message.
     *
     * @throws IOException if an I/O error occurs when flushing or resetting the stream
     */
    private void finishSending() throws IOException {
        outputStream.flush();
        outputStream.reset();
    }



}

