package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Player;

import java.io.IOException;
import java.util.Collections;

public class SingleMatchController {
    private Match match =new Match();

    private final int MAX_NUMCARD_ON_HAND= 3;

    public SingleMatchController() throws IOException {
    }

    void addPlayer(String nickName){
        match.addPlayer(nickName);
    }
    public boolean setPlayerAsReady_StartGameIfAllReady(String p){
        match.setPlayerReady(p);
        /*the game will start automatically if all the players are ready*/
        if(match.isAllPlayersReady()){
            distribute();
            /*
            extractCommonTargetCard();
            setBord();
            setPointTable();
            */
            return true;
        }

        return false;
    }

    private void distribute(){
        for(Player p : match.getPlayers()){
            for(int i =0; i<MAX_NUMCARD_ON_HAND; i++) {
                p.getCardOnHand().add(match.getResourceDeck().getFirst());
                p.getCardOnHand().add(match.getGoldDeck().getFirst());
            }
        }
    }



}
