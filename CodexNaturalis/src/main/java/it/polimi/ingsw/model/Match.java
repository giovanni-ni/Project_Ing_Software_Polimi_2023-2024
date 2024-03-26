package it.polimi.ingsw.model;

import java.util.List;

public class Match {

	public int idMatch;

	private PointTable pt;

	private List<Player> players;

	private Deck initialdeck;

	private ResourceOrGoldDeck resourceDeck;

	private ResourceOrGoldDeck goldDeck;

	private List<TargetCard> commonTarget;


	private int numPlayers;

	private Player firstPlayer;

	private Player[] player;

	private PointTable pointTable;

	public void DistributeResourceDeck() {

	}

	public void DistributeGoldDeck() {

	}

	public void DistributeInitialDeck() {

	}

	public void DistributeObjectiveDeck() {

	}

	public void ChooseFirstPlayer() {

	}

	public void shuffleAll() {

	}

	public Player getPlayerInTurn() {
		return null;
	}

	public Player[] getWinner() {
		return null;
	}

	public Player getNextPlayer() {
		return null;
	}

	public int getObjectiveScore(Player p) {
		return 0;
	}

	public void IslLastRound() {

	}

	public int countRound() {
		return 0;
	}

	public void update() {

	}

	public void getFirstPlayer() {

	}

}
