package it.polimi.ingsw.Networking.socket;


import it.polimi.ingsw.Message.ClientToServerMsg.*;
import it.polimi.ingsw.Message.ServerToClientMsg.*;
import it.polimi.ingsw.model.PlayerStatus;
import it.polimi.ingsw.view.CommonClientActions;
import it.polimi.ingsw.view.TextualInterfaceUnit.Tui;


import java.io.*;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Client extends Thread implements CommonClientActions {
    private Socket socket;
    private static ObjectInputStream inputStream;
    private static ObjectOutputStream outputStream;
    private String serverAddress;
    private int serverPort;
    private String nickname;
    private int GameId;


    public Client(String address, int port) throws IOException {
        this.serverAddress = address;
        this.serverPort = port;
        socket = new Socket(serverAddress, serverPort);
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        inputStream = new ObjectInputStream(socket.getInputStream());
        this.start();

    }

    public void run() {
        while (true) {
            try {
                GenericServerMessage msg = (GenericServerMessage) inputStream.readObject();
                System.out.println(msg);
                handleMessage(msg);


            } catch (ClassNotFoundException e) {
                System.out.println("ClassNotFound");
            } catch (IOException e) {
                System.out.println("IOException");
            }
        }
    }

    public void stopConnection() throws IOException {
        inputStream.close();
        outputStream.close();
        socket.close();
    }

    public static void messageToServer(GenericClientMessage message) {
        try {
            outputStream.writeObject(message);
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
        outputStream.writeObject(new drawCardMessage(nickname,GameId,isGolddeck, number));
        finishSending();
    }

    @Override
    public void sendMessage(String msg) throws IOException {
        outputStream.writeObject(new NewChatMessageMessage(msg));
        finishSending();
    }

    private void finishSending() throws IOException {
        outputStream.flush();
        outputStream.reset();
    }

    public void handleMessage(GenericServerMessage msg) {
        if(msg instanceof joinSuccessMsg) {
            Tui.status = PlayerStatus.MatchStart;
            Tui.myMatch = ((joinSuccessMsg) msg).getModel();
            System.out.println(Tui.myMatch.idMatch);
        }
    }
}

