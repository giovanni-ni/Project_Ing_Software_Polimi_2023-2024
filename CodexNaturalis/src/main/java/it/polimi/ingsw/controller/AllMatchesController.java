package it.polimi.ingsw.controller;

import it.polimi.ingsw.Message.ClientToServerMsg.*;
import it.polimi.ingsw.Message.Message;
import it.polimi.ingsw.Message.ServerToClientMsg.*;
import it.polimi.ingsw.Networking.Listeners.Listener;
import it.polimi.ingsw.model.MatchStatus;
import it.polimi.ingsw.model.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The AllMatchesController class manages multiple game matches
 * SingleMatchController instances and coordinates incoming messages
 * from clients.

 * It follows the Singleton pattern to ensure there is only one instance of
 * AllMatchesController in the application.
 * Messages from clients are processed asynchronously using a blocking queue
 * controllerMessages and are dispatched to the appropriate
 * SingleMatchController instance based on the message type.
 */
public class AllMatchesController extends Thread {

    private static AllMatchesController instance = null;

    /**
     * Retrieves the singleton instance of {@code AllMatchesController}. If the
     * instance does not exist, it creates a new one.
     *
     * @return The singleton instance of {@code AllMatchesController}
     * @throws IOException If an I/O error occurs during initialization
     */
    public synchronized static AllMatchesController getInstance() throws IOException {
        if (instance == null) {
            instance = new AllMatchesController();
        }
        return instance;
    }
    //private static AllMatchesController instance =null;
    private ArrayList<SingleMatchController> runningControllers;
    private final BlockingQueue<GenericClientMessage> controllerMessages = new LinkedBlockingQueue<>();


    /**
     * Adds a client message to the processing queue and associates it with a
     * listener if it is marked as a main controller message.
     *
     * @param temp     The client message to add to the queue
     * @param listener The listener associated with the message
     */
    public void addInQueue(GenericClientMessage temp, Listener listener){
        if (temp.isMainControllerMessage()){
            temp.setListener(listener);
        }
        controllerMessages.add(temp);
    }

    /**
     * Initializes the {@code AllMatchesController} instance and starts its thread.
     *
     * @throws IOException If an I/O error occurs during initialization
     */
    public AllMatchesController() throws IOException {
        this.start();
        this.runningControllers = new ArrayList<>();
    }

    /**
     * The main processing loop for handling client messages. Messages are taken
     * from the queue and either executed directly or forwarded to the appropriate
     * {@link SingleMatchController} instance.
     */
    @Override
    public void run() {
        GenericClientMessage temp;

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


    /**
     * Creates a new game match based on the provided {@link CreateGameMessage}.
     *
     * @param msg The message containing details for creating a new game match
     * @return A message indicating the result of creating the match
     * @throws IOException If an I/O error occurs during match creation
     */
    public Message createNewMatch(CreateGameMessage msg ) throws IOException {

        msg.setGameID(runningControllers.size());
        SingleMatchController c= new SingleMatchController(runningControllers.size());
        if (msg.getLimitPly() != 0){
            c.setLimitPly(msg.getLimitPly());
            c.getMatch().setAutostart(true);
        }
        runningControllers.add(c);

        return joinMatch(msg);

    }

    /**
     * Allows a player to join an existing game match based on the provided
     * {@link JoinGameMessage}.
     *
     * @param msg The message containing player details and game ID
     * @return A message indicating the result of the join operation
     * @throws IOException If an I/O error occurs during the join process
     */
    public Message joinMatch(JoinGameMessage msg ) throws IOException {
        Player p= new Player(msg.getNickname());
        msg.getListener().setNickname(p.getNickname());
        for (SingleMatchController ctrl: runningControllers){
            if (ctrl.getMatch().idMatch== msg.getGameID()) {
                if (ctrl.getMatch().getStatus()!= MatchStatus.Waiting)
                    return new JoinFailMsg("Match Started");
                if (ctrl.isPlayerFull())
                    return new JoinFailMsg("Match Full");
                if(!ctrl.addPlayer(p,msg.getListener())){
                    return new JoinFailMsg("Nick Name Used");
                }
                return new JoinSuccessMsg(ctrl.getMatch());
            }
        }
        return new JoinFailMsg("GameId not Found");

    }

    /**
     * Executes a client message by determining its type and delegating it to the
     * appropriate method.
     *
     * @param msg The client message to execute
     * @throws IOException If an I/O error occurs during message execution
     */
    public void execute(GenericClientMessage msg) throws IOException {
        Message message = null;
        if(msg instanceof CreateGameMessage) {
            message= createNewMatch((CreateGameMessage) msg);
        } else if (msg instanceof JoinFirstMessage) {

            for (int i =0 ; i<runningControllers.size();i++){
                msg.setGameID(runningControllers.get(i).getMatch().getIdMatch());
                message= joinMatch((JoinGameMessage) msg);
                if(message instanceof JoinSuccessMsg) {
                    break;
                }
            }
            if (! (message instanceof JoinSuccessMsg)){
                CreateGameMessage createGameMessage=new CreateGameMessage(msg.getNickname());
                createGameMessage.setListener(msg.getListener());
                message = createNewMatch(createGameMessage);
            }

        } else if (msg instanceof JoinGameMessage){
            message = joinMatch((JoinGameMessage) msg);
        }else {
            message= new ActionNotRecognize();
        }

        if (msg instanceof CrashMsg){
            if(msg.getGameID()>=0){
                SingleMatchController singleMatchController = getControllerbyId(msg.getGameID());
                if (singleMatchController!=null){
                    for (Player p :singleMatchController.getMatch().getPlayers()){
                        if (Objects.equals(p.nickname, msg.getNickname())){
                            //remove the disconnected player's listener
                            singleMatchController.getMatch().getListenerList().remove(msg.getListener());
                            getControllerbyId(msg.getGameID()).notifyAllListeners(new LeaveMessage(p.nickname));
                            singleMatchController.interrupt();
                            runningControllers.remove(singleMatchController);
                        }
                    }
                }

            }
        }else {
            msg.getListener().update(message);
        }
    }

    /**
     * Retrieves the {@link SingleMatchController} instance based on the provided ID.
     *
     * @param id The ID of the match controller to retrieve
     * @return The {@link SingleMatchController} instance corresponding to the ID
     */
    public SingleMatchController getControllerbyId(int id) {
        for (SingleMatchController sC : runningControllers){
            if (sC.getMatch().getIdMatch() == id){
                return sC;
            }
        }
        return null;
    }
}
