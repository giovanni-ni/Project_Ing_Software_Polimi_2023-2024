package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.model.CornerPosition.DOWNLEFT;
import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    public CardParsing cp = new CardParsing();
    public ArrayList<InitialCard> initialCards = (ArrayList<InitialCard>) cp.loadInitialCards();
    public ArrayList<GoldCard> goldCards = (ArrayList<GoldCard>) cp.loadGoldCards();

    public ArrayList<ResourceCard> resourceCards = (ArrayList<ResourceCard>) cp.loadResourceCards();
    public final InitialCard startCard = initialCards.get(81-TypeOfCard.INITIALCARD.codeCardStart);
    public final Board playerBoard = new Board(startCard);

    BoardTest() throws IOException {
    }

    @Test
    void addCardFailRequirment(){
        assertFalse(playerBoard.addCard(goldCards.getLast(),1,1));

    }
    @Test
    void addCardFront(){
        ResourceCard card = resourceCards.getLast();
        card.setFront(true);
        assertTrue(playerBoard.addCard(card,1,1));

    }
    @Test
    void checkNoCardsAbouts(){
        assertFalse(playerBoard.check(4,4));
        assertTrue(playerBoard.check(1,1));
        playerBoard.addCard(resourceCards.getLast(),1,1);
        assertFalse(playerBoard.check(1,1));


    }
    @Test
    void checkNoCardsAboutsForUpLeft(){
        playerBoard.addCard(resourceCards.get(resourceCards.size()-4),1,1);
        playerBoard.check(2,0);


    }
    @Test
    void checkNoCardsAboutsForUpRight(){
        playerBoard.addCard(resourceCards.get(resourceCards.size()-5),3,1);
        playerBoard.check(2,0);


    }
    @Test
    void checkNoCardsAboutsForDownRight(){
        playerBoard.addCard(resourceCards.getLast(),3,-1);
        assertFalse(playerBoard.check(2,0));


    }
    @Test
    void checkNoCardsAboutsForDownLeft(){
        assertTrue(playerBoard.checkCorner(4,4,DOWNLEFT));
        playerBoard.addCard(resourceCards.get(resourceCards.size()-3),1,-1);
        assertFalse(playerBoard.check(2,0));
    }
    @Test
    void checkAllCornerWithCards(){
        ResourceCard card = resourceCards.getLast();
        card.setFront(false);
        ResourceCard card1= resourceCards.getFirst();
        card1.setFront(false);
        ResourceCard card2 =   resourceCards.get(1);
        card2.setFront(false);
        ResourceCard card3 = resourceCards.get(2);
        card3.setFront(false);
        Card c= new Card(12);

        playerBoard.addCard(card1,1,-1);
        playerBoard.addCard(card2,1,1);
        playerBoard.addCard(card3,3,-1);
        playerBoard.addCard(card,3,1);
        assertTrue(playerBoard.check(2,0));
        ArrayList<Integer> exists = playerBoard.getExists();
        for (int cardID : exists){
            Coordinate xy= playerBoard.getCoordinate(cardID);
            Card card4 = playerBoard.getCardInBoard(xy.getX(), xy.getY());
            assertEquals(cardID,card4.getCode());

            assertEquals(xy,playerBoard.getCoordinate(card4));
        }


    }
}