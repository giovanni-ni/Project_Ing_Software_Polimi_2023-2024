package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.*;


public class Player implements Serializable {
	//attributes
	private List<Card> cardOnHand;

	private TargetCard[] targetOnHand;

	private Board board;

	private boolean isReady =false;
	private TargetCard target;

	private PlayerColor playerID;
	private InitialCard initialCard;

	public String nickname;

	public int currentScore;

	/**
	 * Constructor to create a player with a specific score.
	 *
	 * @param currentScore the initial score of the player.
	 */
	public Player(int currentScore){
		this.currentScore=currentScore;
		this.cardOnHand=new ArrayList<>();
		this.board=new Board();
	}
	/**
	 * Constructor to create a player with a target card.
	 *
	 * @param targetOnHand the target card for the player.
	 */
	public Player(TargetCard targetOnHand){
		this.target=targetOnHand;
		this.cardOnHand=new ArrayList<>();
		this.board=new Board();
	}

	/**
	 * Constructor to create a player with target cards, a player ID, and a score.
	 *
	 * @param targetOnHand the target cards for the player.
	 * @param playerID the player's color ID.
	 * @param currentScore the initial score of the player.
	 */
	public Player (TargetCard[] targetOnHand,PlayerColor playerID, int currentScore){
		this.targetOnHand=targetOnHand;
		this.playerID=playerID;
		this.currentScore=currentScore;
		this.cardOnHand=new ArrayList<>();
		this.board=new Board();
		//this.target=new CountTargetCard();//********

	}

	/**
	 * Constructor to create a player with a nickname.
	 *
	 * @param nickname the player's nickname.
	 */
	public Player(String nickname) {
		this.nickname=nickname;
		this.cardOnHand=new ArrayList<>();
		this.board=new Board();
		this.target=null;
		this.targetOnHand = new TargetCard[2];
	}
	/**
	 * Gets the player's board.
	 *
	 * @return the board.
	 */
	public Board getBoard() {
		return board;
	}

	/**
	 * Gets the player's nickname.
	 *
	 * @return the nickname.
	 */
	public String getNickname() {
		return nickname;
	}

	/**
	 * Sets the player's board.
	 *
	 * @param board the board to set.
	 */
	public void setBoard(Board board) {
		this.board = board;
	}

	/**
	 * Gets the player's target card.
	 *
	 * @return the target card.
	 */
	public TargetCard getTarget() {
		return target;
	}

	/**
	 * Sets the player's target card.
	 *
	 * @param target the target card to set.
	 */
	public void setTarget(TargetCard target) {
		this.target = target;
	}

	/**
	 * Gets the player's color ID.
	 *
	 * @return the player's color ID.
	 */
	public PlayerColor getPlayerID() {
		return playerID;
	}

	/**
	 * Sets the player's color ID.
	 *
	 * @param playerID the color ID to set.
	 */
	public void setPlayerID(PlayerColor playerID) {
		this.playerID = playerID;
	}

	/**
	 * Sets the player's nickname.
	 *
	 * @param nickname the nickname to set.
	 */
	public void setNickname(String nickname){this.nickname=nickname;};

	/**
	 * Gets the cards in the player's hand.
	 *
	 * @return the list of cards in hand.
	 */
	public List<Card> getCardOnHand() {
		return cardOnHand;
	}

	/**
	 * Sets the cards in the player's hand.
	 *
	 * @param cardOnHand the list of cards to set.
	 */
	public void setCardOnHand(List<Card> cardOnHand) {
		this.cardOnHand = cardOnHand;
	}

	/**
	 * Gets the target cards in the player's hand.
	 *
	 * @return the array of target cards.
	 */
	public TargetCard[] getTargetOnHand() {
		return targetOnHand;
	}

	/**
	 * Sets the target cards in the player's hand.
	 *
	 * @param targetOnHand the array of target cards to set.
	 */
	public void setTargetOnHand(TargetCard[] targetOnHand) {
		this.targetOnHand = targetOnHand;
	}


	/**
	 * Constructor to create a player with a specific color.
	 *
	 * @param color the player's color.
	 */
	public Player(PlayerColor color) {
		this.playerID = color;
	}

	/**
	 * Adds a resource card to the player's board.
	 *
	 * @param card the resource card to add.
	 * @param x the x-coordinate to place the card.
	 * @param y the y-coordinate to place the card.
	 * @return true if the card was successfully added, false otherwise.
	 */
	public boolean add(ResourceCard card, int x, int y) {
		if (!board.check(x,y)){
			//System.out.print("Card can't be placed here");
			return false;
		}
		//see also if requirement is satisfied
		board.addCard(card, x,y);
		return true;
	}

	/**
	 * Checks if the player is ready.
	 *
	 * @return true if the player is ready, false otherwise.
	 */
	public boolean getReady() {
		return isReady;
	}

	/**
	 * Sets the player's ready status.
	 *
	 * @param ready true to set the player as ready, false otherwise.
	 */
	public void setReady(boolean ready) {
		isReady = ready;
	}

	/**
	 * Gets the player's initial card.
	 *
	 * @return the initial card.
	 */
	public InitialCard getInitialCard() {
		return initialCard;
	}

	/**
	 * Sets the player's initial card.
	 *
	 * @param initialCard the initial card to set.
	 */
	public void setInitialCard(InitialCard initialCard) {
		this.initialCard = initialCard;
	}

}
