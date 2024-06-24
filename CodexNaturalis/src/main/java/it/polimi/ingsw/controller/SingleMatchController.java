package it.polimi.ingsw.controller;

import it.polimi.ingsw.Message.ClientToServerMsg.*;
import it.polimi.ingsw.Message.Message;
import it.polimi.ingsw.Message.ServerToClientMsg.*;
import it.polimi.ingsw.Networking.Listeners.Listener;
import it.polimi.ingsw.model.*;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static it.polimi.ingsw.view.TextualInterfaceUnit.Print.print;


public class SingleMatchController extends Thread{

    private Match match;
    private final int MAX_NUMCARD_ON_HAND= 3;
    private final int FIRST_CARD=0;
    private final int SECOND_CARD =1;
    private final int THIRD_CARD =2;
    private int limitPly =0;

    private final BlockingQueue<GenericClientMessage> processingQueue = new LinkedBlockingQueue<>();

    public SingleMatchController(int GameId) throws IOException {
        match =new Match(GameId);
        this.start();
    }
    public void setPlayerAsReady_StartGameIfAllReady(String p) throws RemoteException {

        match.setPlayerReady(p);

        /*the game will start automatically if all the players are ready*/
        if(match.isAllPlayersReady()&& match.getPlayers().size()>=2 && limitPly==0){
            startGame();
        }else {
            notifyAllListeners(new ActionSuccessMsg(this.match));
        }
    }

    private void startGame() throws RemoteException {
        match.setStatus(MatchStatus.Playing);
        extractCommonTargetCard();
        distributeCardsAndSetBoards();
        extractFirstPlayer();

        notifyAllListeners(new gameStartMsg(this.match));

    }

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

    private void ifLastTurn() throws RemoteException {
        if(match.getStatus()!=MatchStatus.LastRound && match.getStatus()!=MatchStatus.End){
            if (match.getPt().findMaxPoint()>=match.getPt().getMaxPlayerPoint() || (match.getGoldDeck().isEmpty()&&match.getResourceDeck().isEmpty())){
                match.setStatus(MatchStatus.LastRound);
                notifyAllListeners(new LastRoundMessage());
            }
        }
    }




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

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

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
    public void setTargetCard(GenericClientMessage msg) throws RemoteException {
        for(Player p: match.getPlayers()) {
            if(p.getNickname().equals(msg.getNickname())) {
                p.setTarget(p.getTargetOnHand()[((SetTargetCardMessage) msg).getChoice()]);
            }
        }

        getListenerOf(msg.getNickname()).update(new ActionSuccessMsg(match));

    }
    public void notifyAllListeners(Message msg)throws RemoteException{
        for (Listener listener: match.getListenerList()){
            listener.update(msg);
        }
    }

    public void addInQueue(GenericClientMessage msg) {
        this.processingQueue.add(msg);
    }

    public boolean isPlayerFull(){
        return (match.getPlayers().size()>=4);
    }
    public void addListener(Listener listener) throws RemoteException {
        listener.setGameID(match.idMatch);
        match.addListener(listener);
    }

    public Listener getListenerOf(String nickName) throws RemoteException {

        for (Listener listeners : match.getListenerList()){

            if (Objects.equals(listeners.getNickname(), nickName))
                return listeners;
        }
        throw new NullPointerException();

    }
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
    private void extractCommonTargetCard(){
        match.getCommonTarget().add(match.getFirtTargetCard());
        match.getCommonTarget().add(match.getFirtTargetCard());
    }
    private void extractFirstPlayer(){
        Random random = new Random();
        /*get a random num between 0 and 1 || 0 and 2 ||0 and 3*/
        int randomNumber = random.nextInt(match.getPlayers().size()-1);
        match.setFirstPlayer(match.getPlayers().get(randomNumber).nickname);
        match.setCurrentPlayer(match.getPlayers().get(randomNumber));
    }

    public void setLimitPly(int limitPly) {
        this.limitPly = limitPly;
    }
}
