package it.polimi.ingsw.Networking.socket;


import it.polimi.ingsw.Message.ClientToServerMsg.*;
import it.polimi.ingsw.Message.ServerToClientMsg.*;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayerStatus;
import it.polimi.ingsw.model.exeptions.EndGameExeption;
import it.polimi.ingsw.view.CommonClientActions;
import it.polimi.ingsw.view.TextualInterfaceUnit.Tui;


import java.io.*;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static it.polimi.ingsw.view.TextualInterfaceUnit.Print.print;

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
                print(msg);
                handleMessage(msg);


            } catch (ClassNotFoundException e) {
                print("ClassNotFound");
            } catch (IOException e) {
                print("IOException 服务器炸了");
                interrupt();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
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

    public void handleMessage(GenericServerMessage msg) throws InterruptedException {
        if(msg instanceof joinSuccessMsg) {
            Tui.status = PlayerStatus.MatchStart;
            Tui.myMatch = ((joinSuccessMsg) msg).getModel();
            Tui.myPlayer = ((joinSuccessMsg) msg).getModel().getPlayers().getLast();
            print(Tui.myMatch.idMatch);

        } else if(msg instanceof joinFailMsg) {
            print("join fail because" + ((joinFailMsg) msg).getDescription());
        } else if(msg instanceof newPlayerInMsg) {
            print("new player is in");
        } else if(msg instanceof gameStartMsg) {
            print("the game is starting.. 3.. 2.. 1..");
            print("numero di giocatori del ultimo model"+((gameStartMsg) msg).getModel().getPlayers().size());
            Tui.myMatch = ((gameStartMsg) msg).getModel();

            Tui.status = PlayerStatus.GamePlay;
            print("game status change" );

        }

        /*if(msg instanceof ActionSuccessMsg /*|| msg instanceof drawCardSuccess || msg instanceof endGameMessage || msg instanceof gameStartMsg || msg instanceof joinSuccessMsg || msg instanceof playCardSuccess) {
            Tui.myMatch = ((ActionSuccessMsg) msg).getModel();

        }*/

    }
}

