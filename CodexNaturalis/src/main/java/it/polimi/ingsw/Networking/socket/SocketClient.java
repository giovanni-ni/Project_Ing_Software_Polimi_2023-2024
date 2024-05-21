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

public class SocketClient extends Thread implements Client {
    private Socket socket;
    private static ObjectInputStream inputStream;
    private static ObjectOutputStream outputStream;
    private String serverAddress;
    private int serverPort;
    private String nickname;
    private int GameId;
    private Ui ui;

    public SocketClient(String address, int port, Ui ui) throws IOException {
        this.serverAddress = address;
        this.serverPort = port;
        try{
            socket = new Socket(serverAddress, serverPort);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
            this.ui =ui;
            this.start();
        }catch (IOException e){
            ui.handleMessage(new ActionNotRecognize("Connection failed"));
        }

    }

    public void run() {
        boolean Server_ok=true;
        while (Server_ok) {
            try {
                GenericServerMessage msg = (GenericServerMessage) inputStream.readObject();
                print(msg);
                ui.handleMessage(msg);
            } catch (ClassNotFoundException e) {
                print("ClassNotFound");
            } catch (IOException e) {
                print("IOException 服务器炸了");
                Server_ok=false;
            }catch (NullPointerException e){
                print("Connection has a problem, network is not reading");
            }
        }
    }

    public void stopConnection() throws IOException {
        inputStream.close();
        outputStream.close();
        socket.close();
    }
    @Override
    public void messageToServer(GenericClientMessage message) {
        try {
            outputStream.writeObject(message);
            finishSending();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void finishSending() throws IOException {
        outputStream.flush();
        outputStream.reset();
    }



}

