package it.polimi.ingsw.controller;

import it.polimi.ingsw.Message.ClientToServerMsg.*;
import it.polimi.ingsw.Message.Message;
import it.polimi.ingsw.Message.ServerToClientMsg.*;
import it.polimi.ingsw.Networking.Listeners.SocketListener;
import it.polimi.ingsw.Networking.Listeners.Listener;
//import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.MatchStatus;
import it.polimi.ingsw.model.Player;

import java.io.*;
import java.util.ArrayList;
//import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

//import it.polimi.ingsw.model.ViewModel;
//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
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

    public AllMatchesController(ArrayList<SingleMatchController> runningControllers) {
        this.start();
        this.runningControllers = runningControllers;
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
        if (msg.getLimitPly() != 0){
            c.setLimitPly(msg.getLimitPly());
            c.getMatch().setAutostart(true);
        }
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
                    return new joinFailMsg("Nick Name Used");
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
                CreateGameMessage createGameMessage=new CreateGameMessage(msg.getNickname());
                createGameMessage.setListener(msg.getListener());
                message = createNewMatch(createGameMessage);
            }

        } else if (msg instanceof JoinGameMessage){
            message = joinMatch((JoinGameMessage) msg);
        } /*else if (msg instanceof ReconnectRequestMsg) {
            ReconnectRequestMsg m = (ReconnectRequestMsg) msg;
            boolean found = false;
            for(int i= 0; i < runningControllers.get(m.getGameID()).getMatch().getPlayers().size() && !found; i++) {
                if(m.getNickname().equals(runningControllers.get(m.getGameID()).getMatch().getPlayers().get(i).nickname)) {
                    message = new ReconnectSuccess(runningControllers.get(m.getGameID()).getMatch());

                    msg.getListener().setNickname(msg.getNickname());
                    found = true;
                }
            }
            if(!found) {
                message = new ActionNotRecognize("the username doesn't match");
            }

        } */else {
            message= new ActionNotRecognize();
        }
        msg.getListener().update(message);
        //saveMatchesToJson();

    }

    public SingleMatchController getControllerbyId(int id) {
        return this.runningControllers.get(id);
    }

    /*public void saveMatchesToJson() {

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("salvataggio.ser"))) {
            oos.writeObject(runningControllers);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }*/
}
