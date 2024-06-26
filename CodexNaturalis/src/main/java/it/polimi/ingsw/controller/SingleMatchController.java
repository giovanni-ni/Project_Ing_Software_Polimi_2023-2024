package it.polimi.ingsw.controller;

import it.polimi.ingsw.Message.ClientToServerMsg.*;
import it.polimi.ingsw.Message.Message;
import it.polimi.ingsw.Message.ServerToClientMsg.*;
import it.polimi.ingsw.Networking.Listeners.Listener;
import it.polimi.ingsw.model.*;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static it.polimi.ingsw.view.TextualInterfaceUnit.Print.print;
/**
 * The SingleMatchController class manages a single match in a game. It handles player actions, game state transitions, and
 * communication with clients through listeners. It extends Thread to allow concurrent processing of player actions.
 */

public class SingleMatchController extends Thread implements Serializable{

    private Match match;
    private final int MAX_NUMCARD_ON_HAND= 3;
    private final int FIRST_CARD=0;
    private final int SECOND_CARD =1;
    private final int THIRD_CARD =2;
    private int limitPly =0;

    private final BlockingQueue<GenericClientMessage> processingQueue = new LinkedBlockingQueue<>();
    /**
     * Constructs a new SingleMatchController for the specified game ID and starts the thread.
     *
     * @param GameId the ID of the game to be controlled.
     * @throws IOException if an I/O error occurs when creating the match.
     */
    public SingleMatchController(int GameId) throws IOException {
        match =new Match(GameId);
        this.start();
    }

    /**
     * Sets a player as ready and starts the game if all players are ready.
     *
     * @param p the nickname of the player who is ready.
     * @throws RemoteException if a remote communication error occurs.
     */
    public void setPlayerAsReady_StartGameIfAllReady(String p) throws RemoteException {

        match.setPlayerReady(p);

        /*the game will start automatically if all the players are ready*/
        if(match.isAllPlayersReady()&& match.getPlayers().size()>=2 && limitPly==0){
            startGame();
        }else {
            notifyAllListeners(new ActionSuccessMsg(this.match));
        }
    }
    /**
     * Starts the game by setting the match status, extracting target cards, distributing cards, and selecting the first player.
     *
     * @throws RemoteException if a remote communication error occurs.
     */
    private void startGame() throws RemoteException {
        match.setStatus(MatchStatus.Playing);
        extractCommonTargetCard();
        distributeCardsAndSetBoards();
        extractFirstPlayer();

        notifyAllListeners(new gameStartMsg(this.match));

    }
    /**
     * Allows a player to draw a card from the specified deck.
     *
     * @param nickname the nickname of the player.
     * @param isGoldCard true if drawing from the gold deck, false if from the resource deck.
     * @param whichCard the index of the card to draw.
     * @throws RemoteException if a remote communication error occurs.
     */
    public void getACard (String nickname , boolean isGoldCard,int whichCard) throws RemoteException {
        Player currentPlayer=new Player(nickname);
        for (Player p :match.getPlayers()){
            if (p.getNickname().equals(match.getCurrentPlayer().nickname))
                currentPlayer=p;

        }
        if (currentPlayer.nickname.equals(nickname) &&
                //the number of card on hand should be less than 3
                currentPlayer.getCardOnHand().size() < MAX_NUMCARD_ON_HAND) {
            if ((!(match.getGoldDeck().isEmpty())) || (!(match.getResourceDeck().isEmpty()))) {
                if ((isGoldCard && match.getGoldDeck().isEmpty()) || // empty deck
                        (!isGoldCard && match.getResourceDeck().isEmpty())) {
                    getListenerOf(nickname).update( new ActionNotRecognize("Deck Empty"));
                } else if (whichCard >= FIRST_CARD && whichCard <= THIRD_CARD) {
                    if (isGoldCard)
                        currentPlayer.getCardOnHand().add(match.getAGoldCard(whichCard));
                    else
                        currentPlayer.getCardOnHand().add(match.getAResourceCard(whichCard));

                    ifLastTurn();
                    match.nextPlayer();
                    notifyAllListeners(new drawCardSuccess(match));
                    getListenerOf(match.getCurrentPlayer().nickname).update(new NowIsYourRoundMsg());
                } else { //wrong deck index
                    getListenerOf(nickname).update( new ActionNotRecognize("Not Valid Choice"));
                }
            } else {
                getListenerOf(nickname).update( new ActionNotRecognize("Deck Empty"));
            }
        } else {
            getListenerOf(nickname).update( new ActionNotRecognize("Can't draw Card now"));
        }

    }
    /**
     * Checks if the current turn is the last turn of the game.
     * The game enters the last round if the maximum points are reached
     * or if both the gold and resource decks are empty.
     *
     * @throws RemoteException if a remote communication error occurs.
     */
    private void ifLastTurn() throws RemoteException {
        if(match.getStatus()!=MatchStatus.LastRound && match.getStatus()!=MatchStatus.End){
            if (match.getPt().findMaxPoint()>=match.getPt().getMaxPlayerPoint() || (match.getGoldDeck().isEmpty()&&match.getResourceDeck().isEmpty())){
                match.setStatus(MatchStatus.LastRound);
                notifyAllListeners(new LastRoundMessage());
            }
        }
    }

    /**
     * Allows a player to play a card from their hand onto the board.
     * The card is placed at the specified coordinates and orientation.
     * Updates the player's score and advances to the next player if conditions are met.
     *
     * @param nickname      the nickname of the player playing the card
     * @param indexCardOnHand the index of the card in the player's hand
     * @param coo           the coordinates where the card will be placed
     * @param isFront       whether the card is placed front-side up
     * @throws RemoteException if a remote communication error occurs
     */


    public void playACardOnHand (String nickname , int indexCardOnHand, Coordinate coo, boolean isFront) throws RemoteException {
        int x = coo.getX();
        int y = coo.getY();
        Player currentPlayer = match.getCurrentPlayer();
        ResourceCard card = (ResourceCard) currentPlayer.getCardOnHand().get(indexCardOnHand);
        Board board= currentPlayer.getBoard();
        if(currentPlayer.nickname.equals((nickname))){

                if(board.check(x,y) &&(!isFront ||card.checkRequirements(board))) {
                    card.setFront(isFront);
                    board.addCard(card, x, y);
                    currentPlayer.getCardOnHand().remove(indexCardOnHand);
                    //update current score of the player;
                    match.updatePoint(card,currentPlayer);
                    if(match.getStatus()==MatchStatus.LastRound && match.getGoldDeck().isEmpty() && match.getResourceDeck().isEmpty()){
                        match.nextPlayer();
                    }
                    notifyAllListeners( new playCardSuccess(match));
                }else {
                     getListenerOf(nickname).update( new ActionNotRecognize("Not valid choice"));
                }

        }else  getListenerOf(nickname).update( new ActionNotRecognize("Can't play Card now"));


    }
    /**
     * Adds a player to the match and associates a listener with the player.
     * Notifies all listeners when a new player joins. Starts the game if the player limit is reached.
     *
     * @param p        the player to be added
     * @param listener the listener associated with the player
     * @return true if the player was successfully added, false otherwise
     * @throws RemoteException if a remote communication error occurs
     */
    public boolean addPlayer(Player p, Listener listener) throws RemoteException {

        if (!isPlayerFull() && match.addPlayer(p)){
            notifyAllListeners(new newPlayerInMsg(this.match));
            listener.setNickname(p.nickname);
            addListener(listener);
            if (limitPly>=2 && limitPly<=4 && match.getPlayers().size() == limitPly)
                startGame();
            return true;
        }
        return false;
    }


    /**
     * The main execution method for the SingleMatchController thread.
     * Continuously processes messages from the processing queue until the match ends.
     * Sets the winners and notifies all listeners when the match ends.
     */
    @Override
    public void run(){
        GenericClientMessage temp;

        try {
            while (!this.isInterrupted() && match.getStatus()!=MatchStatus.End) {
                temp = processingQueue.take();
                this.execute(temp);
            }
        } catch (InterruptedException | RemoteException ignored) {}
        if (match.getStatus()== MatchStatus.End){
            match.setWinners();
            try {
                notifyAllListeners(new endGameMessage(match));
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }
    /**
     * Executes the appropriate action based on the type of the incoming message.
     *
     * @param msg The message containing the action to be executed.
     * @throws RemoteException If there is a communication-related exception.
     */
    public void execute(GenericClientMessage msg) throws RemoteException {

        switch (msg) {
            case drawCardMessage drawCardMessage when (match.getStatus() == MatchStatus.Playing || match.getStatus() == MatchStatus.LastRound) ->
                    getACard(msg.getNickname(), drawCardMessage.getDeck(), drawCardMessage.getNumberindex());
            case playCardMessage playCardMessage when (match.getStatus() == MatchStatus.Playing || match.getStatus() == MatchStatus.LastRound) ->
                    playACardOnHand(msg.getNickname(), playCardMessage.getIndexOfCardOnHand(), playCardMessage.getCoo(), playCardMessage.isFront());
            case SetReadyMessage setReadyMessage when match.getStatus() == MatchStatus.Waiting ->
                    setPlayerAsReady_StartGameIfAllReady(msg.getNickname());
            case SetTargetCardMessage SetTargetCardMessage when (match.getStatus() == MatchStatus.Playing) ->
                    setTargetCard(msg);
            case FrontOrBackMessage FrontOrBackMessage when (match.getStatus() == MatchStatus.Playing) ->
                    setInitialCard(msg);
            case ClientChatMessage clientChatMessage ->
                    sendChatMsg(msg);
            case ChooseColorMsg chooseColorMsg when (match.getStatus() == MatchStatus.Playing)  ->
                    chooseColor(msg);


            case null, default -> {
                assert msg != null;

                print(msg);
                try {
                    getListenerOf(msg.getNickname()).update(new ActionNotRecognize("Action not recognize"));
                }catch (NullPointerException e){

                }
            }
        }
    }

    /**
     * Handles the process of choosing a color by a player.
     *
     * @param msg The message containing the color choice.
     * @throws RemoteException If there is a communication-related exception.
     */
    private void chooseColor(GenericClientMessage msg) throws RemoteException {

        ChooseColorMsg chooseColorMsg = (ChooseColorMsg)msg;
        PlayerColor color = chooseColorMsg.getColor();
        for(Player p: match.getPlayers()) {
            if (p.getNickname().equals(msg.getNickname())) {
                if (match.getNotChosenColor().contains(color)){
                    match.getNotChosenColor().remove(color);
                    p.setPlayerID(color);
                    notifyAllListeners(new ActionSuccessMsg(this.match));
                } else {
                    getListenerOf(p.getNickname()).update(new ActionNotRecognize("Color already choose"));
                }
            }
        }

    }
    /**
     * Sends a chat message to either all players or a specific player.
     *
     * @param msg The message to be sent.
     * @throws RemoteException If there is a communication-related exception.
     */
    private void sendChatMsg(GenericClientMessage msg) throws RemoteException {
        ClientChatMessage clientChatMessage = (ClientChatMessage) msg;
        if( clientChatMessage.isForAll() ){
            notifyAllListeners(new ServerChatMessage(clientChatMessage));
        }else {
            boolean findPlayer = false;
            for(Player p : match.getPlayers()){
                if (Objects.equals(p.getNickname(), clientChatMessage.getToPlayer()))
                    findPlayer= true;
            }
            if (findPlayer){
                getListenerOf(clientChatMessage.getToPlayer()).update(new ServerChatMessage(clientChatMessage));

            }else {
                getListenerOf(clientChatMessage.getFromPlayer()).update(new ActionNotRecognize("Player name not recognize"));
            }
        }
    }
    /**
     * Sets the initial card for a player based on the client message received.
     * Updates the player's board with the initial card and notifies all listeners
     * upon successful execution.
     *
     * @param msg The client message containing the initial card information.
     * @throws RemoteException If there is a communication-related exception.
     */
    public void setInitialCard(GenericClientMessage msg) throws RemoteException {
        for(Player p: match.getPlayers()) {

            if(p.getNickname().equals(msg.getNickname())) {

                p.getInitialCard().setFront(((FrontOrBackMessage) msg).getFrontOrBack());
                Board board = new Board(p.getInitialCard());
                p.setBoard(board);
                p.getBoard().getCardInBoard(0,0).setFront(((FrontOrBackMessage) msg).getFrontOrBack());
            }
        }
        notifyAllListeners(new ActionSuccessMsg(match));
    }
    /**
     * Sets the target card for a player based on the client message received.
     * Updates the player's target card and notifies the corresponding listener
     * upon successful execution.
     *
     * @param msg The client message containing the target card choice.
     * @throws RemoteException If there is a communication-related exception.
     */
    public void setTargetCard(GenericClientMessage msg) throws RemoteException {
        for(Player p: match.getPlayers()) {
            if(p.getNickname().equals(msg.getNickname())) {
                p.setTarget(p.getTargetOnHand()[((SetTargetCardMessage) msg).getChoice()]);
            }
        }

        getListenerOf(msg.getNickname()).update(new ActionSuccessMsg(match));

    }

    /**
     * Notifies all listeners in the match with the provided message.
     *
     * @param msg The message to be sent to all listeners.
     * @throws RemoteException If there is a communication-related exception.
     */
    public void notifyAllListeners(Message msg)throws RemoteException{
        for (Listener listener: match.getListenerList()){
            listener.update(msg);
        }
    }
    /**
     * Adds a generic client message to the processing queue.
     * Messages in the queue are processed sequentially in a separate thread.
     *
     * @param msg The generic client message to be added to the processing queue.
     */
    public void addInQueue(GenericClientMessage msg) {
        this.processingQueue.add(msg);
    }
    /**
     * Checks if the maximum number of players has been reached in the match.
     *
     * @return {@code true} if the number of players in the match is equal to or greater than 4; {@code false} otherwise.
     */
    public boolean isPlayerFull(){
        return (match.getPlayers().size()>=4);
    }

    /**
     * Adds a listener to the match and assigns the match ID to the listener.
     *
     * @param listener The listener object to be added.
     * @throws RemoteException If there is a communication-related issue while adding the listener.
     */
    public void addListener(Listener listener) throws RemoteException {
        listener.setGameID(match.idMatch);
        match.addListener(listener);
    }

    /**
     * Retrieves the listener associated with the specified nickname.
     *
     * @param nickName The nickname of the player for which the listener is needed.
     * @return The listener object associated with the specified nickname.
     * @throws RemoteException If there is a communication-related issue while retrieving the listener.
     * @throws NullPointerException If no listener is found for the given nickname.
     */

    public Listener getListenerOf(String nickName) throws RemoteException {

        for (Listener listeners : match.getListenerList()){

            if (Objects.equals(listeners.getNickname(), nickName))
                return listeners;
        }
        throw new NullPointerException();

    }
    /**
     * Distributes initial cards and sets up boards for all players in the match.
     * Each player receives resource cards, target cards, and initializes their initial card and score.
     */
    private void distributeCardsAndSetBoards(){
        for(Player p : match.getPlayers()){

            /*List<Card> cardOnHand = p.getCardOnHand();*/
            p.getCardOnHand().add(match.getAResourceCard(FIRST_CARD));
            p.getCardOnHand().add(match.getAResourceCard(FIRST_CARD));
            p.getCardOnHand().add(match.getAGoldCard(FIRST_CARD));

            p.getTargetOnHand() [FIRST_CARD]= match.getFirtTargetCard();
            p.getTargetOnHand() [SECOND_CARD] = match.getFirtTargetCard();
            p.setInitialCard(match.getFirstInitialCard()) ;
            //Board b= new Board(match.getFirstInitialCard());
            //p.setBoard(b);
            p.currentScore=0;
        }
    }
    /**
     * Extracts common target cards for the match.
     * Adds the first target card to the common target list twice.
     */
    private void extractCommonTargetCard(){
        match.getCommonTarget().add(match.getFirtTargetCard());
        match.getCommonTarget().add(match.getFirtTargetCard());
    }

    /**
     * Randomly selects the first player from the list of players in the match.
     * Sets the selected player as both the first player and the current player in the match.
     */
    private void extractFirstPlayer(){
        Random random = new Random();
        /*get a random num between 0 and 1 || 0 and 2 ||0 and 3*/
        int randomNumber = random.nextInt(match.getPlayers().size()-1);
        match.setFirstPlayer(match.getPlayers().get(randomNumber).nickname);
        match.setCurrentPlayer(match.getPlayers().get(randomNumber));
    }
    /**
     * Sets the limit of players allowed in the match.
     *
     * @param limitPly The maximum number of players allowed in the match.
     */
    public void setLimitPly(int limitPly) {
        this.limitPly = limitPly;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }
}
