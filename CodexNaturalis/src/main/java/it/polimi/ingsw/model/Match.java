package it.polimi.ingsw.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Match {

	public int idMatch;

	private PointTable pt;

	private ArrayList<Player> players;

	private ArrayList<InitialCard> initialDeck;

	private ArrayList<ResourceCard> resourceDeck;

	private ArrayList<GoldCard> goldDeck;

	private ArrayList<TargetCard> targetDeck;

	private ArrayList<TargetCard> commonTarget;

	private boolean isLastRound;


	private Player firstPlayer;

	public int getIdMatch() {
		return idMatch;
	}

	public void setIdMatch(int idMatch) {
		this.idMatch = idMatch;
	}

	public PointTable getPt() {
		return pt;
	}

	public void setPt(PointTable pt) {
		this.pt = pt;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}

	public ArrayList<InitialCard> getInitialDeck() {
		return initialDeck;
	}

	public void setInitialDeck(ArrayList<InitialCard> initialDeck) {
		this.initialDeck = initialDeck;
	}

	public ArrayList<ResourceCard> getResourceDeck() {
		return resourceDeck;
	}

	public void setResourceDeck(ArrayList<ResourceCard> resourceDeck) {
		this.resourceDeck = resourceDeck;
	}

	public ArrayList<GoldCard> getGoldDeck() {
		return goldDeck;
	}

	public void setGoldDeck(ArrayList<GoldCard> goldDeck) {
		this.goldDeck = goldDeck;
	}

	public ArrayList<TargetCard> getTargetDeck() {
		return targetDeck;
	}

	public void setTargetDeck(ArrayList<TargetCard> targetDeck) {
		this.targetDeck = targetDeck;
	}

	public ArrayList<TargetCard> getCommonTarget() {
		return commonTarget;
	}

	public void setCommonTarget(ArrayList<TargetCard> commonTarget) {
		this.commonTarget = commonTarget;
	}

	public boolean isLastRound() {
		return isLastRound;
	}

	public void setLastRound(boolean lastRound) {
		isLastRound = lastRound;
	}


	public Player getFirstPlayer() {
		return firstPlayer;
	}

	public void setFirstPlayer(Player firstPlayer) {
		this.firstPlayer = firstPlayer;
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	private Player currentPlayer;

	public Match(int idMatch) throws IOException {
		CardParsing cp= new CardParsing();
		this.idMatch = idMatch;
		pt=new PointTable();
		initialDeck = (ArrayList<InitialCard>) cp.loadInitialCards();
		goldDeck = (ArrayList<GoldCard>) cp.loadGoldCards();
		targetDeck = (ArrayList<TargetCard>) cp.loadTargetCards();
		shuffleAll();
	}
	public Match() throws IOException {
		CardParsing cp= new CardParsing();
		pt=new PointTable();
		initialDeck = (ArrayList<InitialCard>) cp.loadInitialCards();
		goldDeck = (ArrayList<GoldCard>) cp.loadGoldCards();
		targetDeck = (ArrayList<TargetCard>) cp.loadTargetCards();
		shuffleAll();
	}


	public void shuffleAll() {
		Collections.shuffle(targetDeck);
		Collections.shuffle(resourceDeck);
		Collections.shuffle(initialDeck);
		Collections.shuffle(goldDeck);
	}

	public ArrayList<Player> getWinners (){
		ArrayList<Player> winners = new ArrayList<>();
		ArrayList<Player> possibleWinners= (ArrayList<Player>) pt.findMaxPointPlayers();
		if(possibleWinners.size()!=1){
			int maxTargetCount = pt.CountTarget(possibleWinners.getFirst(),commonTarget);
			winners.add(possibleWinners.getFirst());
			for(int i=1; i<possibleWinners.size(); i++){
				int tempTargetCount = pt.CountTarget(possibleWinners.get(i),commonTarget);
				if(tempTargetCount>maxTargetCount){
					winners.clear();
					winners.add(possibleWinners.get(i));
					maxTargetCount=tempTargetCount;
				}
				if(tempTargetCount==maxTargetCount){
					winners.add(possibleWinners.get(i));
				}
			}
		}else{
			winners=possibleWinners;
		}
		return winners;
	};
	void nextPlayer (){
		String currPlayerNick = currentPlayer.nickname;
		for (int i = 0; i < players.size(); i++) {
			 if(currPlayerNick==players.get(i).nickname){
				 if(i+1>players.size()){
					 currentPlayer = players.getFirst();
				 }else{
					 currentPlayer = players.get(i+1);
				 }
			 }
		}
	}

	public void setPlayerReady (String nickname){
		for(int i =0; i<players.size(); i++)
			if(players.get(i).nickname.equals(nickname))
				players.get(i).setReady(true);
	}

	public void addPlayer (String nickname){
		Player p = new Player(nickname);
		players.add(p);
	}

	public boolean isAllPlayersReady() {
		boolean allPlayersReady = true;
		for (Player p : players) {
			if (!p.getReady())
				return allPlayersReady;
		}
		return allPlayersReady;
	}
	public ResourceCard getFirstResourceCard() {
		ResourceCard ris= resourceDeck.getFirst();
		resourceDeck.removeFirst();
		return ris;
	}
	public GoldCard getFirstGoldCard(){
		GoldCard ris= goldDeck.getFirst();
		goldDeck.removeFirst();
		return ris;
	}

	public TargetCard getFirtTargetCard(){
		TargetCard ris= targetDeck.getFirst();
		goldDeck.removeFirst();
		return ris;
	}

	public InitialCard getFirstInitialCard(){
		InitialCard ris= initialDeck.getFirst();
		initialDeck.removeFirst();
		return ris;
	}




}
