package it.polimi.ingsw.model;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TargetDeck {

    private ArrayList<TargetCard> targetDeck;
    private int numTargetCards;
    public  TargetDeck(){

    }

    public TargetDeck(List<TargetCard> targetCards){
        targetDeck= new ArrayList<>(targetCards);
        /*for(int i =0; i<targetCards.length; i++){
            targetDeck.add(targetCards[i]);
        }*/
        int num=targetCards.size();
    }

    public void shuffleTargetCards (){
        Collections.shuffle(targetDeck);
    }

    public TargetCard getTargetCard() {
        if (numTargetCards > 0) {
            TargetCard tmp;
            tmp = targetDeck.getLast();
            targetDeck.removeLast();
            numTargetCards--;
            return tmp;
        }
        else
            return null;
    }


    public TargetCard seeFirstTargetCard(){
        if(numTargetCards>=0)
            return targetDeck.getLast();
        else
            return null;
    }

    public TargetCard seeSecondTargetCard(){
        if(numTargetCards>=0)
            return  targetDeck.getLast();
        else
            return null;
    }


    public int getNumTargetCards() {
        return numTargetCards;
    }

    public ArrayList<TargetCard> getTargetDeck() {
        return targetDeck;
    }

    public void setNumTargetCards(int numTargetCards) {
        this.numTargetCards = numTargetCards;
    }

    public void setTargetDeck(ArrayList<TargetCard> targetDeck) {
        this.targetDeck = targetDeck;
    }


}