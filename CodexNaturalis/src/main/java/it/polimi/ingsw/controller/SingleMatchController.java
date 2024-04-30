package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.exeptions.EndGameExeption;
import it.polimi.ingsw.model.exeptions.GoldCardRequirmentsNotSatisfiedExeption;
import it.polimi.ingsw.model.exeptions.NotValidChoiceToPlayACardExeption;

import java.io.IOException;
import java.util.Random;

public class SingleMatchController {
    private Match match =new Match();

    private final int MAX_NUMCARD_ON_HAND= 3;
    private final int FIRST_CARD=0;
    private final int SECOND_CARD =1;
    private final int THIRD_CARD =2;

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
                p.getCardOnHand().add(match.getAResourceCard(FIRST_CARD));
                p.getCardOnHand().add(match.getAResourceCard(FIRST_CARD));
                p.getCardOnHand().add(match.getAGoldCard(FIRST_CARD));
            }
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
        if(match.getCurrentPlayer().nickname.equals(nickname)||
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
            }// else throw EndGameExeption();
        }//else throw new NotYourTurnExeption();
    }

    public void playACardOnHand (String nickname , int indexCardOnHand, int x, int y, boolean isFront) throws GoldCardRequirmentsNotSatisfiedExeption {
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
                    if(match.getCurrentPlayer().currentScore>20){
                        //throw new EndGameExeption();
                    }
                    else{

                    }
                    match.getPt().updatePoint(match.getCurrentPlayer());//update score on point table;
                }else {
                    //throw new NotValidChoiceToPlayACardExeption();
                }

        }//else throw new NotYourTurnExeption();

    }






}
