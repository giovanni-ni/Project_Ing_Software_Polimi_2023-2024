package it.polimi.ingsw.module;

import java.util.List;

public class Match {

	public GoldCard[]goldCards;

	public ResourceOrGoldDeck ResourseDeck= new ResourceOrGoldDeck(goldCards);

	public int idMatch;

	private PointTable pt;

	private List<Player> players;

	private Deck initialdeck;

	private ResourceOrGoldDeck resourcedeck;

	private ResourceOrGoldDeck golddeck;

	private List<TargetCard> commonTarget;


	private int numplayers;

	private Player firstPlayer;

	private Player[] player;

	private Deck deck;

	private ResourceOrGoldDeck[] resource_Deck;



	private PointTable pointTable;

	public void DIstributeResourceDeck() {

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

	public Player getplayerInTurn() {
		return null;
	}

	public Player[] getWinner() {
		return null;
	}

	public Player getNextPlayer() {
		return null;
	}

	public int getObjectivescore(Player p) {
		return 0;
	}

	public void IslLastRound() {

	}

	public int coutRound() {
		return 0;
	}

	public void update() {

	}

	public void getFirstPlayer() {

	}

}
