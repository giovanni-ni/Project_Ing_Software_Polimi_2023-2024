package it.polimi.ingsw.module;
import java.util.ArrayList;
import java.util.Collections;

public class TargetDeck {

    private ArrayList<TargetCard> targetDeck;
    private int numTargetCards;
    public TargetDeck(TargetCard [] targetCards){
        targetDeck= new ArrayList<>();
        for(int i =0; i<targetCards.length; i++){
            targetDeck.add(targetCards[i]);
        }
        int num=targetCards.length;
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
