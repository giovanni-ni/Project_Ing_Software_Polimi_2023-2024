package it.polimi.ingsw.view.Gui;

import it.polimi.ingsw.Message.Message;
import it.polimi.ingsw.Message.ServerToClientMsg.ActionNotRecognize;
import it.polimi.ingsw.Message.ServerToClientMsg.GenericServerMessage;
import it.polimi.ingsw.Message.ServerToClientMsg.ServerChatMessage;
import it.polimi.ingsw.Networking.Client;
import it.polimi.ingsw.Networking.rmi.RMIClient;
import it.polimi.ingsw.Networking.socket.SocketClient;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayerStatus;
import it.polimi.ingsw.model.ViewModel;
import it.polimi.ingsw.view.Ui;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class GUI implements Ui {
    private GUIApplication guiApplication;

    private String username;

    public static PlayerStatus status;

    public static ViewModel myMatch;

    public static Player myPlayer;

    public static int matchID;

    private Client client;

    public static ArrayList<ServerChatMessage> chat;

    public GUI(GUIApplication guiApplication) {
        this.guiApplication =guiApplication;
    }

    @Override
    public void handleMessage(Message msg) {
        if (msg instanceof ActionNotRecognize && guiApplication.getActualScene()==ScenesName.ASKCONNECTION){
            guiApplication.showErrorMessage(((ActionNotRecognize)msg).getDescription());
        }

    }
    public void connect(Boolean isRmi, String ip, String port) throws IOException, NotBoundException {
        Integer pt = Integer.parseInt(port);
        if (isRmi){
            client = new RMIClient(ip,pt,this);
        }else {
            client = new SocketClient(ip,pt,this);
        }
    }
}
