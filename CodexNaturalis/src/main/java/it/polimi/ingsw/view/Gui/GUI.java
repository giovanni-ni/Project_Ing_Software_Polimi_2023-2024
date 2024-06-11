package it.polimi.ingsw.view.Gui;

import it.polimi.ingsw.Message.ClientToServerMsg.CreateGameMessage;
import it.polimi.ingsw.Message.ClientToServerMsg.GenericClientMessage;
import it.polimi.ingsw.Message.ClientToServerMsg.JoinGameMessage;
import it.polimi.ingsw.Message.Message;
import it.polimi.ingsw.Message.ServerToClientMsg.*;
import it.polimi.ingsw.Networking.Client;
import it.polimi.ingsw.Networking.DefaultPort;
import it.polimi.ingsw.Networking.rmi.RMIClient;
import it.polimi.ingsw.Networking.socket.SocketClient;
import it.polimi.ingsw.model.MatchStatus;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayerStatus;
import it.polimi.ingsw.model.ViewModel;
import it.polimi.ingsw.view.Gui.SceneControllers.BoardController;
import it.polimi.ingsw.view.Gui.SceneControllers.UPDATE;
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


public class GUI extends Thread implements Ui {
    private GUIApplication guiApplication;

    private String username;

    private ViewModel myMatch;

    private int matchID;

    private Client client;

    private ArrayList<ServerChatMessage> chat;

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

    public void connect(Boolean isRmi, String ip) throws IOException {
        if (isRmi){
            client = new RMIClient(ip, DefaultPort.RMIPORT.getNumber(),this);
        }else {
            client = new SocketClient(ip,DefaultPort.SOCKETPORT.getNumber(), this);
        }
    }
    @Override
    public void run(){
        Message temp;
        while(currentThread().isAlive()){
            try {
                temp = processQueue.take();
                handle(temp);
            } catch (InterruptedException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void handle(Message msg) throws IOException {
        if (msg instanceof ServerChatMessage){
            chat.add((ServerChatMessage) msg);
            guiApplication.updateCurrentSceneModel(UPDATE.CHATMESSAGE);
            if(((ServerChatMessage) msg).getChatMsg().equals("快点吧")) {

            }

        }
        if (msg instanceof ActionNotRecognize){
            Platform.runLater(()->guiApplication.showErrorMessage(((ActionNotRecognize)msg).getDescription()));
        }else if (guiApplication.getActualScene()==ScenesName.ASKCONNECTION){
            if (msg instanceof ActionSuccessMsg){
                Platform.runLater(()->guiApplication.showScene(ScenesName.STARTMENU));
            }
        } else if(guiApplication.getActualScene() == ScenesName.ASKNICKNAME || guiApplication.getActualScene() == ScenesName.EASYJOIN) {
            if (msg instanceof gameStartMsg) {
                myMatch = ((gameStartMsg) msg).getModel();
                Platform.runLater(() -> guiApplication.showScene(ScenesName.PREPARE));
            }else if (msg instanceof joinSuccessMsg){
                myMatch =((joinSuccessMsg)msg).getModel();
                matchID=myMatch.getIdMatch();
                Platform.runLater(()->guiApplication.showScene(ScenesName.WAITING));

            }else if (msg instanceof joinFailMsg){
                Platform.runLater(()->guiApplication.showErrorMessage("Joined Failed"));
            }

        }else if (guiApplication.getActualScene() == ScenesName.WAITING) {
            if (msg instanceof gameStartMsg){
                myMatch =((gameStartMsg)msg).getModel();
                Platform.runLater(()->guiApplication.showScene(ScenesName.PREPARE));
            }else if (msg instanceof ActionSuccessMsg) {
                myMatch = ((ActionSuccessMsg) msg).getModel();
                matchID = myMatch.getIdMatch();
                guiApplication.updateCurrentSceneModel(UPDATE.GENERAL);
            }
        }else if(guiApplication.getActualScene() == ScenesName.BOARD){
            if(msg instanceof playCardSuccess) {
                myMatch = ((playCardSuccess) msg).getModel();
                matchID = myMatch.getIdMatch();
                Platform.runLater(()->guiApplication.updateCurrentSceneModel(UPDATE.PLAYCARD));
            }else if(msg instanceof drawCardSuccess){
                myMatch = ((drawCardSuccess) msg).getModel();
                matchID = myMatch.getIdMatch();
                Platform.runLater(()->guiApplication.updateCurrentSceneModel(UPDATE.DRAWCARD));
            }else if(msg instanceof LastRoundMessage){
                matchID = myMatch.getIdMatch();
                Platform.runLater(()->guiApplication.updateCurrentSceneModel(UPDATE.LASTROUND));
            }else if(msg instanceof  endGameMessage){
                myMatch = ((endGameMessage) msg).getModel();
                matchID = myMatch.getIdMatch();
                Platform.runLater(()->guiApplication.updateCurrentSceneModel(UPDATE.ENDMESSAGE));
            }
            else if(msg instanceof  NowIsYourRoundMsg){
                Platform.runLater(()->guiApplication.updateCurrentSceneModel(UPDATE.YOURROUND));
            }else if(msg instanceof  ActionSuccessMsg) {
                myMatch = ((ActionSuccessMsg) msg).getModel();
                matchID = myMatch.getIdMatch();
                Platform.runLater(() -> guiApplication.updateCurrentSceneModel(UPDATE.GENERAL));
            }
        }
        else if (msg instanceof ActionSuccessMsg) {
            myMatch =((ActionSuccessMsg)msg).getModel();
            matchID=myMatch.getIdMatch();
            guiApplication.updateCurrentSceneModel(UPDATE.GENERAL);
        }else if (msg instanceof joinFailMsg) {
            guiApplication.showErrorMessage(((joinFailMsg) msg).getDescription());
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

    public Integer getMatchID() {
        return matchID;
    }

    public String getUsername() {
        return username;
    }

    public ArrayList<ServerChatMessage> getChat() {
        return chat;
    }
}
