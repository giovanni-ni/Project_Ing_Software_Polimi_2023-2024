package it.polimi.ingsw.Networking.rmi;

import it.polimi.ingsw.Message.ClientToServerMsg.GenericClientMessage;
import it.polimi.ingsw.Message.Message;
import it.polimi.ingsw.Message.ServerToClientMsg.gameStartMsg;
import it.polimi.ingsw.Message.ServerToClientMsg.joinFailMsg;
import it.polimi.ingsw.Message.ServerToClientMsg.joinSuccessMsg;
import it.polimi.ingsw.Message.ServerToClientMsg.newPlayerInMsg;
import it.polimi.ingsw.Networking.Listeners.Listener;
import it.polimi.ingsw.Networking.remoteInterface.VirtualServer;
import it.polimi.ingsw.model.PlayerStatus;
import it.polimi.ingsw.Networking.Client;
import it.polimi.ingsw.view.TextualInterfaceUnit.Tui;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.view.TextualInterfaceUnit.Print.print;

public class RMIClient extends UnicastRemoteObject implements Listener, Client {

    final List<String> playersNickname= new ArrayList<>();

    private String nickname;

    private Integer gameId;
    final VirtualServer server;
    public RMIClient(VirtualServer server) throws RemoteException {
        this.server=server;
        this.server.connect(this);
    }

    @Override
    public void update(Message msg) throws RemoteException{
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
    }

    @Override
    public void setNickname(String nickname) throws RemoteException{
        this.nickname=nickname;
    }

    @Override
    public String getNickname() throws RemoteException {
        return this.nickname;
    }

    @Override
    public Integer getGameID() throws RemoteException {
        return this.gameId;
    }

    @Override
    public void setGameID(Integer gameID) throws RemoteException {
        this.gameId=gameID;
    }

    @Override
    public void messageToServer(GenericClientMessage msg) throws RemoteException {
        server.addInQueue(msg, this );
    }
}
