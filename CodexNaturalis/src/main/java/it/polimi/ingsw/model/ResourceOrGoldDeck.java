package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class ResourceOrGoldDeck extends Deck{

    protected int numCards;
    private ArrayList <Card> deck;
    public ResourceOrGoldDeck(List<Card> cards) {
        super(cards);
    }
    public Card getFirstFrontCard(){
        if(numCards>=0){
            Card tmp = deck.getLast();
            deck.removeLast();
            numCards--;
            return tmp;
        }
        return null;
    }

    public  Card getSecondFrontCard(){
        if(numCards>=0){
            Card tmp=deck.get(numCards-1);
            deck.remove(numCards-1);
            numCards--;
            return tmp;
        }
        return null;
    }

}