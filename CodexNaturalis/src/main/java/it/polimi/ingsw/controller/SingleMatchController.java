package it.polimi.ingsw.controller;

import it.polimi.ingsw.Message.ClientToServerMsg.*;
import it.polimi.ingsw.Message.Message;
import it.polimi.ingsw.Message.ServerToClientMsg.*;
import it.polimi.ingsw.Networking.Listeners.GameListener;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.exeptions.NotYourTurnException;


import javax.xml.stream.events.EndDocument;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class SingleMatchController extends Thread{

    private Match match;

    private final int MAX_NUMCARD_ON_HAND= 3;
    private final int FIRST_CARD=0;
    private final int SECOND_CARD =1;
    private final int THIRD_CARD =2;


    private final BlockingQueue<GenericClientMessage> processingQueue = new LinkedBlockingQueue<>();

    public SingleMatchController(int GameId) throws IOException {
        this.start();
        match =new Match(GameId);

    }
    public void setPlayerAsReady_StartGameIfAllReady(String p){

        match.setPlayerReady(p);
        /*the game will start automatically if all the players are ready*/
        if(match.isAllPlayersReady()&& match.getPlayers().size()>=2){
            match.setStatus(MatchStatus.Playing);
            extractCommonTargetCard();
            distributeCardsAndSetBoards();
            extractFirstPlayer();
            notifyAllListeners(new gameStartMsg(match));
        }
    }

    public void getACard (String nickname , boolean isGoldCard,int whichcard) {
        if (match.getCurrentPlayer().nickname.equals(nickname) &&
                //the number of card on hand should be less than 3
                match.getCurrentPlayer().getCardOnHand().size() < MAX_NUMCARD_ON_HAND) {
            if ((match.getGoldDeck().isEmpty()) && (match.getResourceDeck().isEmpty())) {
                if (isGoldCard && match.getGoldDeck().isEmpty() || // empty deck
                        !isGoldCard && match.getResourceDeck().isEmpty()) {
                    getListenerof(nickname).update( new ActionNotRecognize("Deck Empty"));
                } else if (whichcard >= FIRST_CARD && whichcard <= THIRD_CARD) {
                    if (isGoldCard)
                        match.getCurrentPlayer().getCardOnHand().add(match.getAGoldCard(whichcard));
                    else
                        match.getCurrentPlayer().getCardOnHand().add(match.getAResourceCard(whichcard));
                    ifLastTurn();
                    match.nextPlayer();
                    notifyAllListeners(new drawCardSuccess(match));
                } else { //wrong deck index
                    getListenerof(nickname).update( new ActionNotRecognize("Not Valid Choice"));
                }
            } else {
                match.setStatus(MatchStatus.LastRound);
                getListenerof(nickname).update( new ActionNotRecognize("Deck Empty"));
            }
        } else {
            getListenerof(nickname).update( new ActionNotRecognize("Can't draw Card now"));
        }
    }

    private void ifLastTurn() {
        if (match.getPt().findMaxPoint()>=match.getPt().getMaxPlayerPoint()){
            match.setStatus(MatchStatus.LastRound);
            notifyAllListeners(new LastRoundMessage());
        }
    }




    public void playACardOnHand (String nickname , int indexCardOnHand, Coordinate coo, boolean isFront)/* throws GoldCardRequirmentsNotSatisfiedExeption */{
        int x = coo.getX();
        int y = coo.getY();
        if(match.getCurrentPlayer().nickname.equals((nickname))){

                if(match.getCurrentPlayer().getBoard().check(x,y)) {
                    match.getCurrentPlayer().getCardOnHand().get(indexCardOnHand).setFront(isFront);
                    match.getCurrentPlayer().getBoard().addCard((ResourceCard) match.getCurrentPlayer().getCardOnHand().get(indexCardOnHand), x, y);
                    match.getCurrentPlayer().getCardOnHand().remove(indexCardOnHand);
                    //update current score of the player;
                    if(match.getCurrentPlayer().getCardOnHand().get(indexCardOnHand).isGoldCard()){
                        match.getCurrentPlayer().currentScore +=((GoldCard) match.getCurrentPlayer().getCardOnHand().get(indexCardOnHand)).goalCount(match.getCurrentPlayer().getBoard());
                    }
                    else {
                        match.getCurrentPlayer().currentScore += ((ResourceCard) match.getCurrentPlayer().getCardOnHand().get(indexCardOnHand)).getBasePoint();
                    }
                    match.getPt().updatePoint(match.getCurrentPlayer());
                    notifyAllListeners( new playCardSuccess(match));
                }else {
                     getListenerof(nickname).update( new ActionNotRecognize("Not valid choice"));
                }

        }else  getListenerof(nickname).update( new ActionNotRecognize("Can't play Card now"));


    }


    public void updateAllTargetPoints (){
        for(Player p : match.getPlayers()){
            int countSecretTarget=p.getTarget().checkGoal(p.getBoard());
            p.currentScore+=countSecretTarget*p.getTarget().getbasePoint();
            p.currentScore+=match.getCommonTarget().get(FIRST_CARD).checkGoal(p.getBoard())*match.getCommonTarget().get(FIRST_CARD).getbasePoint()+
                    match.getCommonTarget().get(SECOND_CARD).checkGoal(p.getBoard())*match.getCommonTarget().get(SECOND_CARD).getbasePoint();
            match.getPt().updatePoint(match.getCurrentPlayer());
        }
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public boolean addPlayer(Player p, GameListener listener) {

        if (match.addPlayer(p) && !isPlayerFull()){
            notifyAllListeners(new newPlayerInMsg(p.nickname));
            addListener(listener);
            return true;
        }
        return false;
    }



    @Override
    public void run() {
        GenericClientMessage temp;

        try {
            while (!this.isInterrupted() && match.getStatus()!=MatchStatus.End) {
                temp = processingQueue.take();
                this.execute(temp);
            }
        } catch (InterruptedException ignored) {}
        if (match.getStatus()== MatchStatus.End){
            updateAllTargetPoints();
            match.setWinners();
            notifyAllListeners(new endGameMessage(match));
        }
    }

    public void execute(GenericClientMessage msg) {

        if(msg instanceof drawCardMessage && (match.getStatus()==MatchStatus.Playing || match.getStatus() == MatchStatus.LastRound) ){
            getACard(msg.getNickname(),((drawCardMessage) msg).getDeck(),((drawCardMessage) msg).getNumberindex());
        } else if (msg instanceof playCardMessage &&(match.getStatus() ==MatchStatus.Playing|| match.getStatus() == MatchStatus.LastRound)) {
            playACardOnHand(msg.getNickname(),((playCardMessage) msg).getIndexOfCardOnHand(),((playCardMessage) msg).getCoo(),((playCardMessage) msg).isFront());
        } else if (msg instanceof SetReadyMessage && match.getStatus() == MatchStatus.Waiting){
            setPlayerAsReady_StartGameIfAllReady(msg.getNickname());
        }else {
           getListenerof(msg.getNickname()).update(new ActionNotRecognize("Action not recognize"));
        }


    }
    public void notifyAllListeners(Message msg){
        for (GameListener listener: match.getListenerList()){
            listener.update(msg);
        }
    }

    public void addInQueue(GenericClientMessage msg) {
        this.processingQueue.add(msg);
    }

    public boolean isPlayerFull(){
        return (match.getPlayers().size()>=4);
    }
    public void addListener(GameListener listener) {
        listener.setGameID(match.idMatch);
        match.addListener(listener);

    }

    public GameListener getListenerof(String nickName){
        for (GameListener listeners : match.getListenerList()){
            if (listeners.getNickname()==nickName)
                return listeners;
        }
        return null;
    }
    private void distributeCardsAndSetBoards(){
        for(Player p : match.getPlayers()){

            p.getCardOnHand().add(match.getAResourceCard(FIRST_CARD));
            p.getCardOnHand().add(match.getAResourceCard(FIRST_CARD));
            p.getCardOnHand().add(match.getAGoldCard(FIRST_CARD));

            p.getTargetOnHand() [FIRST_CARD]= match.getFirtTargetCard();
            p.getTargetOnHand() [SECOND_CARD] = match.getFirtTargetCard();
            Board b= new Board(match.getFirstInitialCard());
            p.setBoard(b);
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
        int randomNumber = random.nextInt(match.getPlayers().size());
        match.setFirstPlayer(match.getPlayers().get(randomNumber).nickname);
    }
}
