package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static it.polimi.ingsw.model.TypeOfCard.TARGETCARD;
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
        assertInstanceOf(CountTargetCard.class, targetCards.getFirst());
        TargetCard testTargetCard= targetCards.getFirst();
        assertEquals(testTargetCard.getIdCard(),87);
        assertFalse(testTargetCard.isIfCommon());
        testTargetCard.checkGoal(playerBoard);
        assertEquals(testTargetCard.checkGoal(playerBoard),0);
        assertEquals(testTargetCard.countPoint(playerBoard),0);
        playerBoard.addElement(Elements.INK);
        assertEquals(testTargetCard.checkGoal(playerBoard),0);
        assertEquals(testTargetCard.countPoint(playerBoard),0);
        playerBoard.addElement(Elements.INK);
        assertEquals(testTargetCard.checkGoal(playerBoard),1);
        assertEquals(testTargetCard.countPoint(playerBoard),2);
        playerBoard.addElement(Elements.INK,2);
        assertEquals(testTargetCard.checkGoal(playerBoard),2);
        assertEquals(testTargetCard.countPoint(playerBoard),4);
        CountTargetCard testCard2 = (CountTargetCard) targetCards.get(2);
        Elements[] elements = testCard2.getElemRequired();
        assertEquals(elements[0],Elements.PARCHMENT);
        assertEquals(elements[1],Elements.PARCHMENT);

    }
    @Test
    public void testObliqueTarget() throws IOException {
        ObliqueTargetCard card = new ObliqueTargetCard(91,2,false,Elements.MUSHROOMS,CornerPosition.UPRIGHT);
        ObliqueTargetCard testTargetCard = (ObliqueTargetCard) targetCards.get(8);
        assertEquals(testTargetCard.getIdCard(),card.getIdCard());
        assertEquals(testTargetCard.getbasePoint(),card.getbasePoint());
        assertEquals(testTargetCard.getcornerPosition(),card.getcornerPosition());
        assertEquals(testTargetCard.getElemRequired(),card.getElemRequired());
        assertEquals(testTargetCard.getClass(),card.getClass());
        InitialCard startCard= initialCards.getFirst();
        Board playerBoard = new Board(startCard);

        assertEquals(testTargetCard.checkGoal(playerBoard),0);
        assertEquals(testTargetCard.countPoint(playerBoard),0);
        ArrayList<ResourceCard> resourceCards = (ArrayList<ResourceCard>) cp.loadResourceCards();
        playerBoard.addCard(resourceCards.getFirst(),1,1);
        playerBoard.addCard(resourceCards.get(1),2,2);
        playerBoard.addCard(resourceCards.get(2),3,3);
        playerBoard.addCard(resourceCards.get(3),4,4);
        assertEquals(testTargetCard.checkGoal(playerBoard),1);
        playerBoard.addCard(resourceCards.get(4),5,5);
        playerBoard.addCard(resourceCards.get(5),6,6);
        assertEquals(testTargetCard.checkGoal(playerBoard),2);


    }

}