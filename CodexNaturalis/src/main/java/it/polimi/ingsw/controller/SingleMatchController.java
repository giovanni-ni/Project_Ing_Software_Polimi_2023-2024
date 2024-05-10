package it.polimi.ingsw.controller;

import it.polimi.ingsw.Message.ClientToServerMsg.*;
import it.polimi.ingsw.Networking.Listeners.GameListener;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.exeptions.GoldCardRequirmentsNotSatisfiedExeption;


import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Objects;
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
        // todo check the player if is in the match list
        match.setPlayerReady(p);
        /*the game will start automatically if all the players are ready*/
        if(match.isAllPlayersReady()){
            match.setStatus(MatchStatus.Playing);
            extractCommonTargetCard();
            distributeCardsAndSetBoards();
            extractFirstPlayer();
        }
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
        match.setFirstPlayer(match.getPlayers().get(randomNumber));
    }

    public void getACard (String nickname , boolean isGoldCard,int whichcard){
        if(match.getCurrentPlayer().nickname.equals(nickname) &&
                //il numero delle carte in mano del client deve essere minore di 3
                match.getCurrentPlayer().getCardOnHand().size()<MAX_NUMCARD_ON_HAND){
            if((match.getGoldDeck().isEmpty())&&(match.getResourceDeck().isEmpty())){
                    if(isGoldCard && match.getGoldDeck().isEmpty()|| // il mazzo che il cliente ha scelto è vuoto
                            !isGoldCard && match.getResourceDeck().isEmpty()) {
                        //throw new ClientShouldChooseTheOtherDeckExeption;
                    } else if (whichcard>=FIRST_CARD && whichcard <= THIRD_CARD) {
                        if (isGoldCard)
                            match.getCurrentPlayer().getCardOnHand().add(match.getAGoldCard(whichcard));
                        else
                            match.getCurrentPlayer().getCardOnHand().add(match.getAResourceCard(whichcard));
                    } else if (whichcard <FIRST_CARD || whichcard >THIRD_CARD){ // l'indice della carta che il cliente vuole pesacere non è valido
                        //throw new NotValidChoiceToGetACardExeption;
                    }
            }// else throw EndGameException();
        }//else throw new NotYourTurnException();
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
                    match.getPt().updatePoint(match.getCurrentPlayer());//update score on point table;
                }else {
                    //throw new NotValidChoiceToPlayACardExeption();
                }

        }//else throw new NotYourTurnException();


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



    @Override
    public void run() {
        GenericClientMessage temp;

        try {
            while (!this.isInterrupted()) {
                temp = processingQueue.take();
                this.execute(temp);
            }
        } catch (InterruptedException ignored) {}
    }

    public void execute(GenericClientMessage msg) { //todo more exceptions maybe
        //TODO execute method leaveMessage, newChatMessage,reconnectMessage
        if(msg instanceof drawCardMessage && match.getStatus()==MatchStatus.Playing) {
            getACard(msg.getNickname(),((drawCardMessage) msg).getDeck(),((drawCardMessage) msg).getNumberindex());
        } else if (msg instanceof playCardMessage && match.getStatus() ==MatchStatus.Playing) {
            playACardOnHand(msg.getNickname(),((playCardMessage) msg).getIndexOfCardOnHand(),((playCardMessage) msg).getCoo(),((playCardMessage) msg).isFront());
        } else if (msg instanceof SetReadyMessage && match.getStatus() == MatchStatus.Waiting){
            setPlayerAsReady_StartGameIfAllReady(msg.getNickname());
        }else {
            //todo handle message not recognize
        }
    }

    public void addInQueue(GenericClientMessage msg) {
        this.processingQueue.add(msg);
    }

    public boolean addPlayer(Player p, GameListener listener) {

        if (match.addPlayer(p) && !isPlayerFull()){
            addListener(listener);
            return true;
        }
        return false;
    }
    public boolean isPlayerFull(){
        return (match.getPlayers().size()>=4);
    }

    public void addListener(GameListener listener) {
        listener.setGameID(match.idMatch);
        match.addListener(listener);

    }
}
