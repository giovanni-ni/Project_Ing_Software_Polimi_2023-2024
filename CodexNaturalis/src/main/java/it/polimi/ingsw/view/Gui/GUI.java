package it.polimi.ingsw.view.Gui;

import it.polimi.ingsw.Message.ClientToServerMsg.CreateGameMessage;
import it.polimi.ingsw.Message.ClientToServerMsg.GenericClientMessage;
import it.polimi.ingsw.Message.ClientToServerMsg.JoinGameMessage;
import it.polimi.ingsw.Message.Message;
import it.polimi.ingsw.Message.ServerToClientMsg.*;
import it.polimi.ingsw.Networking.Client;
import it.polimi.ingsw.Networking.rmi.RMIClient;
import it.polimi.ingsw.Networking.socket.SocketClient;
import it.polimi.ingsw.model.MatchStatus;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayerStatus;
import it.polimi.ingsw.model.ViewModel;
import it.polimi.ingsw.view.Ui;
import javafx.application.Platform;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static it.polimi.ingsw.view.TextualInterfaceUnit.Print.print;

public class GUI extends Thread implements Ui {
    private GUIApplication guiApplication;

    private String username;

    private   PlayerStatus status;

    private   ViewModel myMatch;

    private   Player myPlayer;

    private   int matchID;

    private Client client;

    private static ArrayList<ServerChatMessage> chat;

    private BlockingQueue<Message> processQueue;

    public GUI(GUIApplication guiApplication) {
        this.guiApplication =guiApplication;
        processQueue = new LinkedBlockingQueue<>();
        chat = new ArrayList<>();
        this.start();
        matchID =0;
        username = null;

    }

    @Override
    public void handleMessage(GenericServerMessage msg) {
        processQueue.add(msg);
    }
    public void connect(Boolean isRmi, String ip, String port) throws IOException, NotBoundException {
        Integer pt = Integer.parseInt(port);
        if (isRmi){
            client = new RMIClient(ip,pt,this);
        }else {
            client = new SocketClient(ip,pt,this);
        }
    }
    @Override
    public void run(){
        Message temp;
        while(currentThread().isAlive()){
            try {
                temp = processQueue.take();
                handle(temp);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void handle(Message msg){
        if (msg instanceof ActionNotRecognize){
            guiApplication.showErrorMessage(((ActionNotRecognize)msg).getDescription());
        }else if (guiApplication.getActualScene()==ScenesName.ASKCONNECTION){
            if (msg instanceof ActionSuccessMsg){
                Platform.runLater(()->guiApplication.showScene(ScenesName.STARTMENU));
            }
        } else if(guiApplication.getActualScene() == ScenesName.ASKNICKNAME || guiApplication.getActualScene() == ScenesName.EASYJOIN) {
            if (msg instanceof joinSuccessMsg){
                myMatch =((joinSuccessMsg)msg).getModel();
                matchID=myMatch.getIdMatch();
                myPlayer = myMatch.getPlayerByNickname(username);
                Platform.runLater(()->guiApplication.showScene(ScenesName.WAITING));

            }

        } else if (msg instanceof ActionSuccessMsg) {
            myMatch =((ActionSuccessMsg)msg).getModel();
            matchID=myMatch.getIdMatch();
            guiApplication.updateCurrentSceneModel();
        }
    }

    public void notify(Message msg) throws RemoteException {

        if (msg instanceof JoinGameMessage){
            username =((GenericClientMessage)msg).getNickname();
        } else if (username!=null) {
            ((GenericClientMessage)msg).setNickname(username);
            ((GenericClientMessage) msg).setGameID(matchID);
        }
        
        client.messageToServer((GenericClientMessage) msg);
    }

    public ViewModel getMyMatch() {
        return myMatch;
    }

    public String getMatchID() {
        return new String(String.valueOf(matchID));
    }
}
