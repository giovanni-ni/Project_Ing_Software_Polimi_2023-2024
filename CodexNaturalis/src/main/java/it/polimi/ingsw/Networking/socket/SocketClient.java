package it.polimi.ingsw.Networking.socket;


import it.polimi.ingsw.Message.ClientToServerMsg.*;
import it.polimi.ingsw.Message.ServerToClientMsg.*;
import it.polimi.ingsw.model.PlayerStatus;
import it.polimi.ingsw.Networking.Client;
import it.polimi.ingsw.view.TextualInterfaceUnit.Tui;


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


    public SocketClient(String address, int port) throws IOException {
        this.serverAddress = address;
        this.serverPort = port;
        socket = new Socket(serverAddress, serverPort);
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        inputStream = new ObjectInputStream(socket.getInputStream());
        this.start();

    }

    public void run() {
        boolean Server_ok=true;
        while (Server_ok) {
            try {
                GenericServerMessage msg = (GenericServerMessage) inputStream.readObject();
                print(msg);
                handleMessage(msg);
            } catch (ClassNotFoundException e) {
                print("ClassNotFound");
            } catch (IOException e) {
                print("IOException 服务器炸了");
                Server_ok=false;
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

    public void handleMessage(GenericServerMessage msg) throws InterruptedException {
        if(msg instanceof joinSuccessMsg) {
            Tui.status = PlayerStatus.MatchStart;
            Tui.myMatch = ((joinSuccessMsg) msg).getModel();
            Tui.myPlayer = ((joinSuccessMsg) msg).getModel().getPlayers().getLast();
            print(Tui.myMatch.idMatch);

        } else if(msg instanceof joinFailMsg) {
            print("join fail because" + ((joinFailMsg) msg).getDescription());
        }else if(msg instanceof ServerChatMessage) {
            print("New chat Message:");
            showNewChatMessage(msg);
            //store the chat for historical chat view
            Tui.chat.add((ServerChatMessage)msg);
        } else if(msg instanceof newPlayerInMsg) {
            print("new player is in" + ((newPlayerInMsg) msg).getNicknameNewPlayer());
        } else if(msg instanceof gameStartMsg) {
            //print("the game is starting.. 3.. 2.. 1..");
            //print("numero di giocatori del ultimo model"+((gameStartMsg) msg).getModel().getPlayers().size());
            Tui.myMatch = ((gameStartMsg) msg).getModel();
            Tui.myPlayer = ((gameStartMsg) msg).getModel().getPlayerByNickname(Tui.myPlayer.nickname);
            Tui.status = PlayerStatus.Preparing;
            //print("game status change" );

        } else if(msg instanceof playCardSuccess) {
            Tui.myMatch = ((playCardSuccess) msg).getModel();
            Tui.myPlayer = ((playCardSuccess) msg).getModel().getPlayerByNickname(Tui.myPlayer.nickname);
            Tui.hasChange = 1;
            Tui.hasPlayed = true;
            if(((playCardSuccess) msg).getModel().getCurrentPlayer().getNickname().equals(Tui.myPlayer.nickname)) {
                Tui.status = PlayerStatus.Draw;
            }

        } else if(msg instanceof drawCardSuccess) {
            Tui.status = PlayerStatus.GamePlay;
            Tui.myMatch = ((drawCardSuccess) msg).getModel();
            Tui.myPlayer = ((drawCardSuccess) msg).getModel().getPlayerByNickname(Tui.myPlayer.nickname);
        }
        else if(msg instanceof ActionSuccessMsg) {
            Tui.myMatch = ((ActionSuccessMsg) msg).getModel();
            Tui.myPlayer = ((ActionSuccessMsg) msg).getModel().getPlayerByNickname(Tui.myPlayer.nickname);
        } else if(msg instanceof ActionNotRecognize) {
            Tui.printMessage(((ActionNotRecognize) msg).getDescription());
        } else if(msg instanceof NowIsYourRoundMsg) {
            Tui.printMessage(((NowIsYourRoundMsg) msg).getDescription());
        } else if(msg instanceof LastRoundMessage) {
            Tui.printMessage("ATTENTION !! it's the last round");
        } else if(msg instanceof endGameMessage) {
            Tui.status = PlayerStatus.END;
            Tui.myMatch = ((endGameMessage) msg).getModel();
            Tui.myPlayer = ((endGameMessage) msg).getModel().getPlayerByNickname(Tui.myPlayer.nickname);
        }

        /*if(msg instanceof ActionSuccessMsg /*|| msg instanceof drawCardSuccess || msg instanceof endGameMessage || msg instanceof gameStartMsg || msg instanceof joinSuccessMsg || msg instanceof playCardSuccess) {
            Tui.myMatch = ((ActionSuccessMsg) msg).getModel();

        }*/

    }


}
