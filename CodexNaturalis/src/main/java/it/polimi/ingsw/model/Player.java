package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.*;

/**
 * Represents a player in the game.
 * This class holds the player's hand, board, and various attributes such as score, nickname, and readiness.
 * It provides methods to manipulate and access these attributes.
 */
public class Player implements Serializable {
	// Attributes

	/**
	 * The cards currently in the player's hand.
	 */
	private List<Card> cardOnHand;
	/**
	 * The target cards in the player's hand.
	 */
	private TargetCard[] targetOnHand;

	/**
	 * The board associated with the player.
	 */
	private Board board;
	/**
	 * Indicates whether the player is ready.
	 */
	private boolean isReady =false;
	/**
	 * The target card of the player.
	 */
	private TargetCard target;
	/**
	 * The color ID of the player.
	 */
	private PlayerColor playerID;


	/**
	 * The initial card of the player.
	 */
	private InitialCard initialCard;


	/**
	 * The nickname of the player.
	 */
	public String nickname;

	/**
	 * The current score of the player.
	 */
	public int currentScore;

	// Constructors

	/**
	 * Constructs a new Player with the specified score.
	 *
	 * @param currentScore the initial score of the player
	 */
	public Player(int currentScore){
		this.currentScore=currentScore;
		this.cardOnHand=new ArrayList<>();
		this.board=new Board();
	}

	/**
	 * Constructs a new Player with the specified target card.
	 *
	 * @param targetOnHand the target card of the player {@link TargetCard}
	 */
	public Player(TargetCard targetOnHand){
		this.target=targetOnHand;
		this.cardOnHand=new ArrayList<>();
		this.board=new Board();
	}

	/**
	 * Constructs a new Player with the specified target cards, player color, and score.
	 *
	 * @param targetOnHand the target cards of the player {@link TargetCard}
	 * @param playerID     the color ID of the player {@link PlayerColor}
	 * @param currentScore the initial score of the player
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
	 * Constructs a new Player with the specified nickname.
	 *
	 * @param nickname the nickname of the player
	 */
	public Player(String nickname) {
		this.nickname=nickname;
		this.cardOnHand=new ArrayList<>();
		this.board=new Board();
		this.target=null;
		this.targetOnHand = new TargetCard[2];
	}

	/**
	 * Constructs a new Player with the specified color ID.
	 *
	 * @param color the color ID of the player {@link PlayerColor}
	 */
	public Player(PlayerColor color) {
		this.playerID = color;
	}

	// Methods

	/**
	 * Gets the board associated with the player.
	 *
	 * @return the board
	 */
	public Board getBoard() {
		return board;
	}

	/**
	 * Gets the nickname of the player.
	 *
	 * @return the nickname
	 */
	public String getNickname() {
		return nickname;
	}

	/**
	 * Sets the board associated with the player.
	 *
	 * @param board the board to set
	 */
	public void setBoard(Board board) {
		this.board = board;
	}

	/**
	 * Gets the target card of the player.
	 *
	 * @return the target card
	 */
	public TargetCard getTarget() {
		return target;
	}

	/**
	 * Sets the target card of the player.
	 *
	 * @param target the target card to set
	 */
	public void setTarget(TargetCard target) {
		this.target = target;
	}

	/**
	 * Gets the color ID of the player.
	 *
	 * @return the color ID
	 */
	public PlayerColor getPlayerID() {
		return playerID;
	}

	/**
	 * Sets the color ID of the player.
	 *
	 * @param playerID the color ID to set
	 */
	public void setPlayerID(PlayerColor playerID) {
		this.playerID = playerID;
	}

	/**
	 * Sets the nickname of the player.
	 *
	 * @param nickname the nickname to set
	 */
	public void setNickname(String nickname){this.nickname=nickname;};

	/**
	 * Gets the cards in the player's hand.
	 *
	 * @return the cards in the hand
	 */
	public List<Card> getCardOnHand() {
		return cardOnHand;
	}

	/**
	 * Sets the cards in the player's hand.
	 *
	 * @param cardOnHand the cards to set
	 */
	public void setCardOnHand(List<Card> cardOnHand) {
		this.cardOnHand = cardOnHand;
	}

	/**
	 * Gets the target cards in the player's hand.
	 *
	 * @return the target cards
	 */
	public TargetCard[] getTargetOnHand() {
		return targetOnHand;
	}

	/**
	 * Sets the target cards in the player's hand.
	 *
	 * @param targetOnHand the target cards to set
	 */
	public void setTargetOnHand(TargetCard[] targetOnHand) {
		this.targetOnHand = targetOnHand;
	}


	/**
	 * Adds a resource card to the board at the specified position if the position is valid.
	 *
	 * @param card the resource card to add {@link ResourceCard}
	 * @param x    the x-coordinate on the board
	 * @param y    the y-coordinate on the board
	 * @return true if the card was added successfully, false otherwise
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
	 * Gets the readiness state of the player.
	 *
	 * @return true if the player is ready, false otherwise
	 */
	public boolean getReady() {
		return isReady;
	}

	/**
	 * Sets the readiness state of the player.
	 *
	 * @param ready the readiness state to set
	 */
	public void setReady(boolean ready) {
		isReady = ready;
	}

	/**
	 * Gets the initial card of the player.
	 *
	 * @return the initial card
	 */
	public InitialCard getInitialCard() {
		return initialCard;
	}

	/**
	 * Sets the initial card of the player.
	 *
	 * @param initialCard the initial card to set
	 */
	public void setInitialCard(InitialCard initialCard) {
		this.initialCard = initialCard;
	}

}
