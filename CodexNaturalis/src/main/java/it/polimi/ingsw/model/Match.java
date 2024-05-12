package it.polimi.ingsw.model;

import it.polimi.ingsw.Networking.Listeners.GameListener;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Match implements Serializable {

	public int idMatch;

	private PointTable pt;

	private ArrayList<Player> players;

	private ArrayList<InitialCard> initialDeck;

	private ArrayList<ResourceCard> resourceDeck;

	private ArrayList<GoldCard> goldDeck;

	private ArrayList<TargetCard> targetDeck;

	private ArrayList<TargetCard> commonTarget;

	private MatchStatus status = MatchStatus.Waiting;

	private String firstPlayer;

	private transient List<GameListener> listenerList;

	private int roundCount=0;

	private List<Player> winners;

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


	public ArrayList<ResourceCard> getResourceDeck() {
		return resourceDeck;
	}


	public ArrayList<GoldCard> getGoldDeck() {
		return goldDeck;
	}


	public ArrayList<TargetCard> getTargetDeck() {
		return targetDeck;
	}


	public ArrayList<TargetCard> getCommonTarget() {
		return commonTarget;
	}

	public void setCommonTarget(ArrayList<TargetCard> commonTarget) {
		this.commonTarget = commonTarget;
	}

	public String getFirstPlayer() {
		return firstPlayer;
	}

	public void setFirstPlayer(String firstPlayer) {
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

		listenerList = new ArrayList<>();
		players = new ArrayList<>();
        initialDeck = (ArrayList<InitialCard>) cp.loadInitialCards();
		goldDeck = (ArrayList<GoldCard>) cp.loadGoldCards();
		targetDeck = (ArrayList<TargetCard>) cp.loadTargetCards();
		resourceDeck = (ArrayList<ResourceCard>) cp.loadResourceCards();
		shuffleAll();
		status =MatchStatus.Waiting;
		currentPlayer = null;

		commonTarget = new ArrayList<TargetCard>();
	}
	public Match() throws IOException {
		CardParsing cp= new CardParsing();
		pt=new PointTable();

		listenerList = new ArrayList<>();
		players = new ArrayList<>();;
		initialDeck = (ArrayList<InitialCard>) cp.loadInitialCards();
		goldDeck = (ArrayList<GoldCard>) cp.loadGoldCards();
		targetDeck = (ArrayList<TargetCard>) cp.loadTargetCards();
		resourceDeck = (ArrayList<ResourceCard>) cp.loadResourceCards();
		shuffleAll();

	}


	public void shuffleAll() {
		Collections.shuffle(targetDeck);
		Collections.shuffle(resourceDeck);
		Collections.shuffle(initialDeck);
		Collections.shuffle(goldDeck);
	}

	public void setWinners (){
		winners = new ArrayList<>();
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

	};
	public void nextPlayer(){
		String currPlayerNick = currentPlayer.nickname;

		for (int i = 0; i < players.size(); i++) {
			 if(Objects.equals(currPlayerNick, players.get(i).nickname)){
				 if(players.get(i)==players.getLast()){
					 currentPlayer= players.getFirst();
				 }else{
					 currentPlayer= players.get(i+1);
				 }
			 }
		}
		if (status==MatchStatus.LastRound){
			if (Objects.equals(currentPlayer.nickname, firstPlayer)){
				roundCount++;
			}
		}
		if (roundCount>2){
			setStatus(MatchStatus.End);
		}

	}

	public void setPlayerReady (String nickname){
		for(int i =0; i<players.size(); i++)
			if(players.get(i).nickname.equals(nickname))
				players.get(i).setReady(true);
	}


	public boolean isAllPlayersReady() {
		boolean allPlayersReady = true;
		for (Player p : players) {
			if (!p.getReady())
				return !allPlayersReady;
		}
		return allPlayersReady;
	}
	public ResourceCard getAResourceCard(int i) {
		ResourceCard ris= resourceDeck.get(i);
		resourceDeck.remove(i);
		return ris;
	}
	public GoldCard getAGoldCard(int i){
		GoldCard ris= goldDeck.get(i);
		goldDeck.remove(i);
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

	public MatchStatus getStatus() {
		return status;
	}

	public void setStatus(MatchStatus status) {
		this.status = status;
	}

	public boolean addPlayer(Player p) {
		for (Player player : players){
			if (Objects.equals(player.getNickname(), p.getNickname()))
				return false;
		}
		players.add(p);
		return true;
	}

	public void addListener(GameListener listener) {
		listenerList.add(listener);
	}

	public void setListenerList(List<GameListener> listenerList) {
		this.listenerList = listenerList;
	}

	public List<GameListener> getListenerList() {
		return listenerList;
	}
}
