package it.polimi.ingsw.module;
import java.util.ArrayList;
import java.util.Collections;

public class Deck {

	protected int numCards;
	private ArrayList<Card> deck;

    public Deck (Card[] cards){
		deck =new ArrayList<>();
        for(int i =0; i<cards.length; i++) {
			/*method that sets all the cards into backs*/
			deck.add(cards[i]);
		}
		int numCards=cards.length;
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
			return deck.get(numCards - 1);
		}else
			return null;
	}
	public int getNumOfRemainingCards(){
		return numCards;
	}

}
