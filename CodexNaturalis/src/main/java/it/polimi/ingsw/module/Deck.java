package it.polimi.ingsw.module;
import java.util.ArrayList;
import java.util.Collections;

public class Deck {

	protected int numCards;
	private ArrayList<Card> deck;

    public Deck (Card[] cards){
		deck =new ArrayList<>();
        for(int i =0; i<cards.length; i++) {
			deck.add(cards[i]);
			/*method that sets all the cards into backs*/
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
	public int getNumOfRemainingCards(){
		return numCards;
	}

}
