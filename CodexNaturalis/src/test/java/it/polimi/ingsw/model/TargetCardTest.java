package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TargetCardTest {
    CardParsing cp= new CardParsing();
    public ArrayList<InitialCard> initialCards = (ArrayList<InitialCard>) cp.loadInitialCards();
    public ArrayList<TargetCard> targetCards = (ArrayList<TargetCard>) cp.loadTargetCards();
    TargetCardTest() throws IOException {
    }
    @Test
    public void CountTargetCard(){
        InitialCard startCard= initialCards.getFirst();
        Board playerBoard = new Board(startCard);
        assertTrue(targetCards.get(0) instanceof CountTargetCard);
    }

}