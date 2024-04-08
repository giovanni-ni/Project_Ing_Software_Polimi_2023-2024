package it.polimi.ingsw.model;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {

	protected int numCards;
	private List<Card> deck;

    public Deck (List<Card> cards){
		deck =new ArrayList<>();
        for(int i =0; i<cards.size(); i++) {
			/*method that sets all the cards into backs*/
			deck.add(cards.get(i));
		}
		this.numCards=cards.size();
	}
	public Deck (){

	}
	public void shuffleCards() {
			Collections.shuffle(deck);
	}
	public Card getBackCardFromDeck() {
		Card tmpCard;
		if(numCards>=0) {
			tmpCard = deck.get(numCards - 2);
			deck.remove(numCards - 2);
			numCards--;
			return tmpCard;
		}else
			return null;
	}

	public  Card seeFirstFrontCard(){

		if(numCards>=0) {
			/*set front card*/
			return deck.getLast();
		}
		else
			return null;
	}
	public Card seeSecondFrontCard() {

		if(numCards>=0) {
			/*set front card*/
			return deck.get(numCards - 2);
		}else
			return null;
	}

	public int getNumCards() {

		return numCards;
	}
	public List<Card> getDeck() {
		return deck;
	}

	public void setDeck(List<Card> deck) {
		this.deck = deck;
	}

	public void setNumCards(int numCards) {
		this.numCards = numCards;
	}
}
