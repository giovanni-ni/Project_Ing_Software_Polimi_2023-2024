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

/**
 * Represents the graphical user interface for the client-side application,
 * handling interactions between the user interface elements and the game logic.
 */
public class GUI extends Thread implements Ui {
    private GUIApplication guiApplication;

    private String username;

    private ViewModel myMatch;

    private int matchID;

    private Client client;

    private ArrayList<ServerChatMessage> chat;

    private BlockingQueue<Message> processQueue;

    /**
     * Constructs a GUI instance associated with a GUIApplication.
     *
     * @param guiApplication The GUIApplication instance to associate with this GUI.
     */
    public GUI(GUIApplication guiApplication) {
        this.guiApplication = guiApplication;
        processQueue = new LinkedBlockingQueue<>();
        chat = new ArrayList<>();
        this.start();
        matchID = 0;
        username = null;
    }

    /**
     * Handles incoming server messages by adding them to the processing queue.
     *
     * @param msg The server message to handle.
     */
    @Override
    public void handleMessage(GenericServerMessage msg) {
        processQueue.add(msg);
    }

    /**
     * Establishes a connection to the server based on the connection type (RMI or Socket).
     *
     * @param isRmi Indicates whether to use RMI for connection.
     * @param ip    The IP address of the server.
     * @throws IOException If an I/O error occurs while connecting.
     */
    public void connect(Boolean isRmi, String ip) throws IOException {
        if (isRmi) {
            client = new RMIClient(ip, DefaultPort.RMIPORT.getNumber(), this);
        } else {
            client = new SocketClient(ip, DefaultPort.SOCKETPORT.getNumber(), this);
        }
    }

    /**
     * Starts the GUI thread for handling incoming messages from the server.
     */
    @Override
    public void run() {
        Message temp;
        while (currentThread().isAlive()) {
            try {
                temp = processQueue.take();
                handle(temp);
            } catch (InterruptedException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Handles different types of messages received from the server.
     *
     * @param msg The message received from the server to handle.
     * @throws IOException If an I/O error occurs during message handling.
     */
    public void handle(Message msg) throws IOException {
        if (msg instanceof LeaveMessage){
            Platform.runLater(() ->guiApplication.showAllert(((LeaveMessage)msg).getLeftPlayer()));
        }
        if (msg instanceof ServerChatMessage) {
            chat.add((ServerChatMessage) msg);
            guiApplication.updateCurrentSceneModel(UPDATE.CHATMESSAGE);
            if (((ServerChatMessage) msg).getChatMsg().equals("快点吧")) {
                Sound.playSound("6589.wav");
            }
        }
        if (msg instanceof ActionNotRecognize) {
            Platform.runLater(() -> guiApplication.showErrorMessage(((ActionNotRecognize) msg).getDescription()));
        } else if (guiApplication.getActualScene() == ScenesName.ASKCONNECTION) {
            if (msg instanceof ActionSuccessMsg) {
                Platform.runLater(() -> guiApplication.showScene(ScenesName.STARTMENU));
            }
        } else if (guiApplication.getActualScene() == ScenesName.ASKNICKNAME || guiApplication.getActualScene() == ScenesName.EASYJOIN) {
            if (msg instanceof gameStartMsg) {
                myMatch = ((gameStartMsg) msg).getModel();
                Platform.runLater(() -> guiApplication.showScene(ScenesName.PREPARE));
            } else if (msg instanceof joinSuccessMsg) {
                myMatch = ((joinSuccessMsg) msg).getModel();
                matchID = myMatch.getIdMatch();
                Platform.runLater(() -> guiApplication.showScene(ScenesName.WAITING));
            } else if (msg instanceof joinFailMsg) {
                Platform.runLater(() -> guiApplication.showErrorMessage(((joinFailMsg) msg).getDescription()));
            }
        } else if (guiApplication.getActualScene() == ScenesName.WAITING) {
            if (msg instanceof gameStartMsg) {
                myMatch = ((gameStartMsg) msg).getModel();
                Platform.runLater(() -> guiApplication.showScene(ScenesName.PREPARE));
            } else if (msg instanceof ActionSuccessMsg) {
                myMatch = ((ActionSuccessMsg) msg).getModel();
                matchID = myMatch.getIdMatch();
                guiApplication.updateCurrentSceneModel(UPDATE.GENERAL);
            }
        } else if (guiApplication.getActualScene() == ScenesName.BOARD) {
            if (msg instanceof playCardSuccess) {
                myMatch = ((playCardSuccess) msg).getModel();
                matchID = myMatch.getIdMatch();
                Platform.runLater(() -> guiApplication.updateCurrentSceneModel(UPDATE.PLAYCARD));
            } else if (msg instanceof drawCardSuccess) {
                myMatch = ((drawCardSuccess) msg).getModel();
                matchID = myMatch.getIdMatch();
                Platform.runLater(() -> guiApplication.updateCurrentSceneModel(UPDATE.DRAWCARD));
            } else if (msg instanceof LastRoundMessage) {
                matchID = myMatch.getIdMatch();
                Platform.runLater(() -> guiApplication.updateCurrentSceneModel(UPDATE.LASTROUND));
            } else if (msg instanceof endGameMessage) {
                myMatch = ((endGameMessage) msg).getModel();
                matchID = myMatch.getIdMatch();
                Platform.runLater(() -> guiApplication.updateCurrentSceneModel(UPDATE.ENDMESSAGE));
            } else if (msg instanceof NowIsYourRoundMsg) {
                Platform.runLater(() -> guiApplication.updateCurrentSceneModel(UPDATE.YOURROUND));
            } else if (msg instanceof ActionSuccessMsg) {
                myMatch = ((ActionSuccessMsg) msg).getModel();
                matchID = myMatch.getIdMatch();
                Platform.runLater(() -> guiApplication.updateCurrentSceneModel(UPDATE.GENERAL));
            }
        } else if (msg instanceof ActionSuccessMsg) {
            myMatch = ((ActionSuccessMsg) msg).getModel();
            matchID = myMatch.getIdMatch();
            guiApplication.updateCurrentSceneModel(UPDATE.GENERAL);
        } else if (msg instanceof joinFailMsg) {
            guiApplication.showErrorMessage(((joinFailMsg) msg).getDescription());
        }
    }

    /**
     * Sends a message to the server for processing.
     *
     * @param msg The message to send to the server.
     * @throws RemoteException If there is an issue with remote method invocation.
     */
    public void notify(Message msg) throws RemoteException {
        if (msg instanceof JoinGameMessage) {
            username = ((GenericClientMessage) msg).getNickname();
        } else if (username != null) {
            ((GenericClientMessage) msg).setNickname(username);
            ((GenericClientMessage) msg).setGameID(matchID);
        }
        client.messageToServer((GenericClientMessage) msg);
    }

    /**
     * Retrieves the ViewModel representing the current state of the match.
     *
     * @return The ViewModel object representing the current match state.
     */
    public ViewModel getMyMatch() {
        return myMatch;
    }

    /**
     * Retrieves the ID of the current match.
     *
     * @return The ID of the current match.
     */
    public Integer getMatchID() {
        return matchID;
    }

    /**
     * Retrieves the username of the client.
     *
     * @return The username of the client.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Retrieves the chat messages received from the server.
     *
     * @return The list of ServerChatMessage objects representing chat messages.
     */
    public ArrayList<ServerChatMessage> getChat() {
        return chat;
    }

    /**
     * Initialize the Gui data if game finished and user want to play with the same connection
     */
    public void initializeForNewGame(){
        matchID = 0;
        username = null;
        chat = new ArrayList<>();
    }

}
