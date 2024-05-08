package it.polimi.ingsw.controller;

import it.polimi.ingsw.Message.ClientToServerMsg.*;
import it.polimi.ingsw.model.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class AllMatchesController extends Thread {

    private static AllMatchesController instance =null;
    private ArrayList<SingleMatchController> runningControllers;
    private BlockingQueue<GenericClientMessage> controllerMessages;


    public void addInQueue(GenericClientMessage temp){
        controllerMessages.add(temp);
    }
    public AllMatchesController() throws IOException {
        this.start();
    }

    @Override
    public void run() {
        GenericClientMessage temp;

        try {
            while (!this.isInterrupted()) {

                temp = controllerMessages.take();
                if (temp.isMainControllerMessage()) {
                    this.execute(temp);
                }else{
                    runningControllers.get(temp.getGameID()).addInQueue(temp);
                }

            }
        } catch (InterruptedException ignored) {} catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public void execute(GenericClientMessage msg) throws IOException {
        if(msg instanceof CreateGameMessage) {
            createNewMatch(msg.getNickname());
        } else if (msg instanceof JoinFirstMessage) {
            Random ran = new Random();
            int random = ran.nextInt(runningControllers.size()-1);
            runningControllers.get(random).addPlayer(msg.getNickname());
        } else if (msg instanceof JoinGameMessage){
            runningControllers.get(msg.getGameID()).addPlayer(msg.getNickname());
        }else {
            //todo handle message not recognize
        }
    }
    /**
     * Singleton Pattern
     * Il Singleton Pattern è un design pattern creazionale utilizzato nella
     * programmazione orientata agli oggetti per garantire che una classe abbia una
     * sola istanza e fornire un punto di accesso globale a tale istanza.
     * @return
     */

    public synchronized static AllMatchesController getInstance() throws IOException {
        if(instance==null) {
            instance = new AllMatchesController();
        }
        return instance;
    }


    public SingleMatchController createNewMatch(String nickname ) throws IOException {
        Player p= new Player(nickname);
        SingleMatchController c= new SingleMatchController();
        runningControllers.add(c);
        c.addPlayer(p);
        return c;
    }

}
