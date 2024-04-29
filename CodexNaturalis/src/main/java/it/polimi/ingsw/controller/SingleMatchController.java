package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.GoldCard;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exeptions.CardOnHandIsFullExeption;
import it.polimi.ingsw.model.exeptions.NotYourTurnExeption;
import it.polimi.ingsw.model.exeptions.DeckIsFullExeption;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class SingleMatchController {
    private Match match =new Match();

    private final int MAX_NUMCARD_ON_HAND= 3;

    public SingleMatchController() throws IOException {
    }

    void addPlayer(String nickName){
        match.addPlayer(nickName);
    }
    public void setPlayerAsReady_StartGameIfAllReady(String p){
        match.setPlayerReady(p);
        /*the game will start automatically if all the players are ready*/
        if(match.isAllPlayersReady()){
            extractCommonTargetCard();
            distributeCardsAndSetBoards();
            extractFirstPlayer();
        }
    }

    private void distributeCardsAndSetBoards(){
        for(Player p : match.getPlayers()){
            for(int i =0; i<MAX_NUMCARD_ON_HAND; i++) {
                p.getCardOnHand().add(match.getFirstResourceCard());
                p.getCardOnHand().add(match.getFirstResourceCard());
                p.getCardOnHand().add(match.getFirstGoldCard());
            }
            p.setTarget(match.getFirtTargetCard());
            Board b= new Board(match.getFirstInitialCard());
            p.setBoard(b);
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

    public void getFirstOpenedGoldCard(String nickname){
        if(match.getCurrentPlayer().nickname.equals(nickname)){
            if(!match.getGoldDeck().isEmpty()){
                if(match.getCurrentPlayer().getCardOnHand().size()<MAX_NUMCARD_ON_HAND)
                    match.getCurrentPlayer().getCardOnHand().add(match.getFirstGoldCard());
                //else throw new CardOnHandIsFullExeption();
            }
            //else throw new DeckIsFullExeption();
        }//else throw new NotYourTurnExeption();
    }

    public void getFirstOpenedResourceCard(String nickname){
        if(match.getCurrentPlayer().nickname.equals(nickname)){
            if(!match.getGoldDeck().isEmpty()){
                if(match.getCurrentPlayer().getCardOnHand().size()<MAX_NUMCARD_ON_HAND)
                    match.getCurrentPlayer().getCardOnHand().add(match.getFirstResourceCard());
                //else throw new CardOnHandIsFullExeption();
            }
            //else throw new DeckIsFullExeption();
        }//else throw new NotYourTurnExeption();
    }


    public void getSecondOpenedGoldCard(String nickname){
        if(match.getCurrentPlayer().nickname.equals(nickname)){
            if(!match.getGoldDeck().isEmpty()){
                if(match.getCurrentPlayer().getCardOnHand().size()<MAX_NUMCARD_ON_HAND)
                    match.getCurrentPlayer().getCardOnHand().add(match.getFirstGoldCard());
                //else throw new CardOnHandIsFullExeption();
            } else if (match.getGoldDeck().isEmpty()&&match.getResourceDeck().isEmpty()) {
                //throw new EndGameExeption();
            }

            //else throw new DeckIsFullExeption();
        }//else throw new NotYourTurnExeption();
    }
    public void getSecOpenedGoldCard(String nickname){
        if(match.getCurrentPlayer().nickname.equals(nickname)){
            if(!match.getGoldDeck().isEmpty()){
                if(match.getCurrentPlayer().getCardOnHand().size()<MAX_NUMCARD_ON_HAND)
                    match.getCurrentPlayer().getCardOnHand().add(match.getFirstGoldCard());
                //else throw new CardOnHandIsFullExeption();
            } else if (match.getGoldDeck().isEmpty()&&match.getResourceDeck().isEmpty()) {
                //throw new EndGameExeption();
            }

            //else throw new DeckIsFullExeption();
        }//else throw new NotYourTurnExeption();
    }

    public void getAGoldCard(String nickname){

    }
    public void getAResourseCard(String nickname){

    }







}
