package it.polimi.ingsw.module;

import java.util.*;
/*how to add point to the ptmap
* swipe is a  view control maybe*/
public class Player {
	//attributes
	private List<Card> cardOnHand;

	private Target_Card[] targetOnHand;

	private Board board;

	private Target_Card target;

	private Player_Color playerID;

	public int currentScore;

	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public Target_Card getTarget() {
		return target;
	}

	public void setTarget(Target_Card target) {
		this.target = target;
	}

	public Player_Color getPlayerID() {
		return playerID;
	}

	public void setPlayerID(Player_Color playerID) {
		this.playerID = playerID;
	}

	public int getCurrentScore() {
		return currentScore;
	}

	public void setCurrentScore(int currentScore) {
		this.currentScore = currentScore;
	}

	public List<Card> getCardOnHand() {
		return cardOnHand;
	}

	public void setCardOnHand(List<Card> cardOnHand) {
		this.cardOnHand = cardOnHand;
	}

	public Target_Card[] getTargetOnHand() {
		return targetOnHand;
	}

	public void setTargetOnHand(Target_Card[] targetOnHand) {
		this.targetOnHand = targetOnHand;
	}

	public void chooseTarget(Target_Card targetCard) {
		/*input of the card wanted*/
		setTarget(targetCard);
	}

	/*public void swap(Card input) {
	//controller or view method or card methos
		input.setIsFront(!input.getIsFront);
	}*/

	/*public void pick(Card input) {
	//controller method
	}*/

	public Player(Player_Color color) {
		setPlayerID(color);
	}

	public boolean add(Resource_Card card, int x, int y) {
		if (!board.check(x,y)){
			System.out.print("Card can't be placed here");
			return false;
		}
		//see also if requirement is satisfied
		board.addCard(card, x,y);
		return true;
	}

	/*public void endRound() {
		//controller method
	}*/

}
