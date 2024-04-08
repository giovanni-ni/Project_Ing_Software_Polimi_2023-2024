package it.polimi.ingsw.model;

import java.util.*;
/*how to add point to the ptmap
 * swipe is a  view control maybe*/
public class Player {
	//attributes
	private List<Card> cardOnHand;

	private TargetCard[] targetOnHand;

	private Board board;

	private TargetCard target;

	private PlayerColor playerID;

	public int currentScore;
	public Player(int currentScore){
		this.currentScore=currentScore;
	}
	public Player(TargetCard targetOnHand){
		this.target=targetOnHand;
	}
	public Player (TargetCard[] targetOnHand,PlayerColor playerID, int currentScore){
		this.targetOnHand=targetOnHand;
		this.playerID=playerID;
		this.currentScore=currentScore;
		this.cardOnHand=new ArrayList<>();
		this.board=new Board();
		this.target=new CountTargetCard();//********

	}

	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public TargetCard getTarget() {
		return target;
	}

	public void setTarget(TargetCard target) {
		this.target = target;
	}

	public PlayerColor getPlayerID() {
		return playerID;
	}

	public void setPlayerID(PlayerColor playerID) {
		this.playerID = playerID;
	}

	public List<Card> getCardOnHand() {
		return cardOnHand;
	}

	public void setCardOnHand(List<Card> cardOnHand) {
		this.cardOnHand = cardOnHand;
	}

	public TargetCard[] getTargetOnHand() {
		return targetOnHand;
	}

	public void setTargetOnHand(TargetCard[] targetOnHand) {
		this.targetOnHand = targetOnHand;
	}

	public void chooseTarget(TargetCard targetCard) {
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

	public Player(PlayerColor color) {
		this.playerID = color;
	}

	public boolean add(ResourceCard card, int x, int y) {
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
