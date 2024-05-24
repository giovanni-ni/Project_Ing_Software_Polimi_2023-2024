package it.polimi.ingsw.controller;

import it.polimi.ingsw.Message.ClientToServerMsg.*;
import it.polimi.ingsw.Message.Message;
import it.polimi.ingsw.Message.ServerToClientMsg.*;
import it.polimi.ingsw.Networking.Listeners.SocketListener;
import it.polimi.ingsw.Networking.Listeners.Listener;
import it.polimi.ingsw.model.MatchStatus;
import it.polimi.ingsw.model.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class AllMatchesController extends Thread {

    private static AllMatchesController instance = null;

    public synchronized static AllMatchesController getInstance() throws IOException {
        if (instance == null) {
            instance = new AllMatchesController();
        }
        return instance;
    }
    //private static AllMatchesController instance =null;
    private ArrayList<SingleMatchController> runningControllers;
    private final BlockingQueue<GenericClientMessage> controllerMessages = new LinkedBlockingQueue<>();


    public void addInQueue(GenericClientMessage temp, Listener listener){
        if (temp.isMainControllerMessage()){
            temp.setListener(listener);
        }
        controllerMessages.add(temp);
    }
    public AllMatchesController() throws IOException {
        this.start();
        this.runningControllers = new ArrayList<>();
    }

    @Override
    public void run() {
        GenericClientMessage temp;
        //this.runningControllers = new ArrayList<>();

        try {
            while (!this.isInterrupted()) {

                temp = controllerMessages.take();
                if (temp.isMainControllerMessage()) {
                    this.execute(temp);
                }else{
                    for (SingleMatchController smc : runningControllers ){
                        if (smc.getMatch().idMatch==temp.getGameID())
                            smc.addInQueue(temp);
                    }

                }

            }
        } catch (InterruptedException ignored) {} catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    /**
     * Singleton Pattern
     * Il Singleton Pattern è un design pattern creazionale utilizzato nella
     * programmazione orientata agli oggetti per garantire che una classe abbia una
     * sola istanza e fornire un punto di accesso globale a tale istanza.
     * @return
     */

    /*public synchronized static AllMatchesController getInstance() throws IOException {
        if(instance==null) {
            instance = new AllMatchesController();
        }
        return instance;
    }*/


    public Message createNewMatch(CreateGameMessage msg ) throws IOException {

        msg.setGameID(runningControllers.size());
        SingleMatchController c= new SingleMatchController(runningControllers.size());
        runningControllers.add(c);

        return joinMatch((JoinGameMessage)msg);

    }
    public Message joinMatch(JoinGameMessage msg ) throws IOException {
        Player p= new Player(msg.getNickname());
        msg.getListener().setNickname(p.getNickname());
        for (SingleMatchController ctrl: runningControllers){
            if (ctrl.getMatch().idMatch== msg.getGameID()) {
                if (ctrl.getMatch().getStatus()!= MatchStatus.Waiting)
                    return new joinFailMsg("Match Started");
                if (ctrl.isPlayerFull())
                    return new joinFailMsg("Match Full");
                if(!ctrl.addPlayer(p,msg.getListener())){
                    return new joinFailMsg("NickNameUsed");
                }
                return new joinSuccessMsg(ctrl.getMatch());
            }
        }
        return new joinFailMsg("GameId not Found");

    }

    public void execute(GenericClientMessage msg) throws IOException {
        Message message = null;
        if(msg instanceof CreateGameMessage) {
            message= createNewMatch((CreateGameMessage) msg);
        } else if (msg instanceof JoinFirstMessage) {

            for (int i =0 ; i<runningControllers.size();i++){
                msg.setGameID(runningControllers.get(i).getMatch().getIdMatch());
                message= joinMatch((JoinGameMessage) msg);
                if(message instanceof joinSuccessMsg) {
                    break;
                }
            }
            if (! (message instanceof joinSuccessMsg)){
                message = new joinFailMsg("No Match available");
            }

        } else if (msg instanceof JoinGameMessage){
            message = joinMatch((JoinGameMessage) msg);
        }else {
            message= new ActionNotRecognize();
        }
        msg.getListener().update(message);
    }
}
