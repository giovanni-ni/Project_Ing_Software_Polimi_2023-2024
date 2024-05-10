package it.polimi.ingsw.Networking.socket;


import it.polimi.ingsw.Message.ClientToServerMsg.*;
import it.polimi.ingsw.Message.Message;
import it.polimi.ingsw.Message.ServerToClientMsg.GenericServerMessage;
import it.polimi.ingsw.view.flow.CommonClientActions;


import java.io.*;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Client extends Thread implements CommonClientActions {
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private String serverAddress;
    private int serverPort;
    private String nickname;

    public Client(String address, int port) throws IOException {
        this.serverAddress = address;
        this.serverPort = port;
        socket = new Socket(serverAddress, serverPort);
        this.start();

    }

    public void run() {
        while (true) {
            try {
                GenericServerMessage msg = (GenericServerMessage) inputStream.readObject();
                /****vediamo*****/

            } catch (IOException | ClassNotFoundException e) {
                System.out.println("error during running");
            }
        }
    }

    public void stopConnection() throws IOException {
        inputStream.close();
        outputStream.close();
        socket.close();
    }


    @Override
    public void createGame(String nickname) throws IOException, InterruptedException, NotBoundException {
        this.nickname = nickname;
        outputStream.writeObject(new CreateGameMessage(nickname));
        finishSending();
    }

    @Override
    public void joinFirstAvailble(String nickname) throws IOException, InterruptedException, NotBoundException {
        this.nickname = nickname;
        outputStream.writeObject(new JoinFirstMessage(nickname));
        finishSending();
    }

    @Override
    public void joinGame(String nickname, int idGame) throws IOException, InterruptedException, NotBoundException {
        this.nickname = nickname;
        outputStream.writeObject(new JoinGameMessage(nickname, idGame));
        finishSending();
    }

    @Override
    public void reconnect(String nickname, int idGame) throws IOException, InterruptedException, NotBoundException {
        this.nickname = nickname;
        outputStream.writeObject(new ReconnectMessage(nickname, idGame));
        finishSending();
    }

    @Override
    public void leave(String nickname, int idGame) throws IOException, NotBoundException {
        outputStream.writeObject(new LeaveMessage(nickname, idGame));
        finishSending();
        nickname=null;
    }

    @Override
    public void setAsReady() throws IOException {
        outputStream.writeObject(new SetReadyMessage(nickname));
        finishSending();
    }

    @Override
    public boolean isMyTurn() throws RemoteException {
        return false;
    }

    @Override
    public void playCard(int cardIndex, boolean front, int x, int y) throws IOException {
        outputStream.writeObject(new playCardMessage(cardIndex, front, x, y));
        finishSending();
    }

    @Override
    public void drawCard(boolean isGolddeck, int number) throws IOException {
        outputStream.writeObject(new drawCardMessage(isGolddeck, number));
        finishSending();
    }

    @Override
    public void sendMessage(Message msg) throws IOException {
        outputStream.writeObject(new NewChatMessageMessage(msg));
        finishSending();
    }

    private void finishSending() throws IOException {
        outputStream.flush();
        outputStream.reset();
    }
}

