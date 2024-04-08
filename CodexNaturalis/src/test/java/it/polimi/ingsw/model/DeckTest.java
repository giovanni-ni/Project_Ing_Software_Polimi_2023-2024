package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

    private boolean ifShuffled(List<Card> cardvecchi,List<Card> cardnuovi ){
        for (int i=0;i<cardvecchi.size();i++){
            if (cardnuovi.get(i)!=cardvecchi.get(i))
                return true;
        }
        return false;
    }

    private List<Card> repeat(){
        List<Card> card= new ArrayList<>();
        Card c1=new Card(1);
        Card c2=new Card(2);
        Card c3=new Card(3);
        Card c4=new Card(4);
        Card c5=new Card(5);
        Card c6=new Card(6);
        Card c7=new Card(7);
        Card c8=new Card(8);
        Card c9=new Card(9);
        Card c10=new Card(10);
        card.add(c1);
        card.add(c2);
        card.add(c3);
        card.add(c4);
        card.add(c5);
        card.add(c6);
        card.add(c7);
        card.add(c8);
        card.add(c9);
        card.add(c10);
        return card;
    }

    @Test
    void shuffleCards() {
        List<Card> card = repeat();
        Deck deck1=new Deck(card);
        deck1.shuffleCards();
        assertTrue(ifShuffled(card,deck1.getDeck()));
    }

    @Test
    void getBackCardFromDeck() {
        List<Card> card = repeat();
        Deck deck1=new Deck(card);
        Card cardrisultato=new Card();
        cardrisultato= deck1.getBackCardFromDeck();
        assertEquals(cardrisultato,card.get(card.size()-2));


    }
    @Test
    void getBackCardFromDeckNullCase() {
        Deck deck1=new Deck();
        deck1.setNumCards(-2);
        assertEquals(null,deck1.getBackCardFromDeck());
    }

    @Test
    void seeFirstFrontCard() {
        List<Card> card = repeat();
        Deck deck1=new Deck(card);
        assertEquals(card.get(card.size()-1),deck1.seeFirstFrontCard());
    }
    @Test
    void seeFirstFrontCardNullCase() {
        Deck deck1=new Deck();
        deck1.setNumCards(-2);
        assertEquals(null,deck1.seeFirstFrontCard());
    }


    @Test
    void seeSecondFrontCard() {
        List<Card> card = repeat();
        Deck deck1=new Deck(card);
        assertEquals(card.get(card.size()-2),deck1.seeSecondFrontCard());
    }
    @Test
    void seeSecondFrontCardNullCase() {
        Deck deck1=new Deck();
        deck1.setNumCards(-2);
        assertEquals(null,deck1.seeSecondFrontCard());
    }

    @Test
    void getNumCards() {
        List<Card> card = repeat();
        Deck deck1=new Deck(card);
        assertEquals(card.size(),deck1.getNumCards());
    }

    @Test
    void getDeck() {
        List<Card> card = repeat();
        Deck deck1=new Deck(card);
        assertEquals(card,deck1.getDeck());
    }

    @Test
    void setDeck() {
        List<Card> card = repeat();
        Deck deck1=new Deck();
        deck1.setDeck(card);
        assertEquals(card,deck1.getDeck());

    }

    @Test
    void setNumCards() {
        Deck deck1=new Deck();
        deck1.setNumCards(100);
        assertEquals(100,deck1.getNumCards());

    }
}