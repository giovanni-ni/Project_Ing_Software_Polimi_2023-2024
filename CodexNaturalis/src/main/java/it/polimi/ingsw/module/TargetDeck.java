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

    public int getNumTargetCards() {
        return numTargetCards;
    }


}
