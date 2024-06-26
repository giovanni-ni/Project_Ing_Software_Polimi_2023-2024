package it.polimi.ingsw.model;

import it.polimi.ingsw.Networking.Listeners.Listener;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

public class Match implements Serializable {

	public int idMatch;

	private boolean autostart;
	private PointTable pt;

	private ArrayList<Player> players;

	private ArrayList<InitialCard> initialDeck;

	private ArrayList<ResourceCard> resourceDeck;

	private ArrayList<GoldCard> goldDeck;

	private ArrayList<TargetCard> targetDeck;

	private ArrayList<TargetCard> commonTarget;

	private MatchStatus status = MatchStatus.Waiting;

	private String firstPlayer;

	private transient List<Listener> listenerList;

	private int roundCount=0;

	private List<Player> winners;

	private List<PlayerColor> playerColors;

	public ArrayList<PlayerColor> notChosenColor;

	private Player currentPlayer;

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

	public ArrayList<Player> getPlayers() {
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

	public ArrayList<PlayerColor> getNotChosenColor() {
		return notChosenColor;
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
	public boolean getAutoStart() {
		return this.autostart;
	}

	public void setAutostart(boolean autostart) {
		this.autostart = autostart;
	}

	public List<PlayerColor> getPlayerColors() {
		return playerColors;
	}




	/**
	 * Constructor to create a match with a specific ID.
	 *
	 * @param idMatch the ID of the match.
	 * @throws IOException if an I/O error occurs during card loading.
	 */
	public Match(int idMatch) throws IOException {
		CardParsing cp= new CardParsing();
		this.idMatch = idMatch;
		pt=new PointTable();
		autostart = false;

		listenerList = new ArrayList<>();
		players = new ArrayList<>();
        initialDeck = (ArrayList<InitialCard>) cp.loadInitialCards();
		goldDeck = (ArrayList<GoldCard>) cp.loadGoldCards();
		targetDeck = (ArrayList<TargetCard>) cp.loadTargetCards();
		resourceDeck = (ArrayList<ResourceCard>) cp.loadResourceCards();
		shuffleAll();
		status =MatchStatus.Waiting;
		currentPlayer = null;
		notChosenColor = new ArrayList<>();
		notChosenColor.addAll(List.of(PlayerColor.values()));
		commonTarget = new ArrayList<TargetCard>();

	}
	/**
	 * Default constructor to create a match.
	 *
	 * @throws IOException if an I/O error occurs during card loading.
	 */
	public Match() throws IOException {
		CardParsing cp= new CardParsing();
		pt=new PointTable();
		autostart = false;

		listenerList = new ArrayList<>();
		players = new ArrayList<>();;
		initialDeck = (ArrayList<InitialCard>) cp.loadInitialCards();
		goldDeck = (ArrayList<GoldCard>) cp.loadGoldCards();
		targetDeck = (ArrayList<TargetCard>) cp.loadTargetCards();
		resourceDeck = (ArrayList<ResourceCard>) cp.loadResourceCards();
		notChosenColor = new ArrayList<>();
		notChosenColor.addAll(List.of(PlayerColor.values()));
		shuffleAll();
		winners= null;

		playerColors = new ArrayList<>(Arrays.asList(PlayerColor.RED, PlayerColor.YELLOW, PlayerColor.GREEN, PlayerColor.BLUE));


	}
	/**
	 * Shuffles all the decks in the match.
	 */
	public void shuffleAll() {
		Collections.shuffle(targetDeck);
		Collections.shuffle(resourceDeck);
		Collections.shuffle(initialDeck);
		Collections.shuffle(goldDeck);
	}

	/**
	 * Sets the winners of the match based on the points and targets.
	 */
	public void setWinners (){
		updateAllTargetPoints();
		winners = new ArrayList<>();
		ArrayList<Player> possibleWinners= (ArrayList<Player>) pt.findMaxPointPlayers();
		if(possibleWinners.size()>=2){
			int maxTargetCount = pt.CountTarget(possibleWinners.getFirst(),commonTarget);
			winners.add(possibleWinners.getFirst());
			for(int i=1; i<possibleWinners.size(); i++){
				int tempTargetCount = pt.CountTarget(possibleWinners.get(i),commonTarget);
				if(tempTargetCount>maxTargetCount){
					winners.clear();
					winners.add(possibleWinners.get(i));
					maxTargetCount=tempTargetCount;
				}else if(tempTargetCount==maxTargetCount){
					winners.add(possibleWinners.get(i));
				}
			}
		}else{
			winners=possibleWinners;
		}

	};

	/**
	 * Updates the target points for all players.
	 */
	public void updateAllTargetPoints (){
		for(Player p : players){
			p.currentScore+=p.getTarget().countPoint(p.getBoard());
			p.currentScore+=getCommonTarget().getFirst().countPoint(p.getBoard())+
					commonTarget.get(1).countPoint(p.getBoard());
			pt.updatePoint(p);
		}
	}

	/**
	 * Advances to the next player in the turn order.
	 */
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
		if (roundCount>1){
			setStatus(MatchStatus.End);
		}

	}

	/**
	 * Sets a player as ready.
	 *
	 * @param nickname the nickname of the player to set as ready.
	 */
	public void setPlayerReady (String nickname){
		for(int i =0; i<players.size(); i++)
			if(players.get(i).nickname.equals(nickname))
				players.get(i).setReady(true);
	}

	/**
	 * Checks if all players are ready.
	 *
	 * @return true if all players are ready, false otherwise.
	 */
	public boolean isAllPlayersReady() {
		boolean allPlayersReady = true;
		for (Player p : players) {
			if (!p.getReady())
				return !allPlayersReady;
		}
		return allPlayersReady;
	}

	/**
	 * Draws a resource card from the deck.
	 *
	 * @param i the index of the resource card to draw.
	 * @return the drawn resource card.
	 */
	public ResourceCard getAResourceCard(int i) {
		ResourceCard ris= resourceDeck.get(i);
		resourceDeck.remove(i);
		return ris;
	}

	/**
	 * Draws a gold card from the deck.
	 *
	 * @param i the index of the gold card to draw.
	 * @return the drawn gold card.
	 */
	public GoldCard getAGoldCard(int i){
		GoldCard ris= goldDeck.get(i);
		goldDeck.remove(i);
		return ris;
	}

	/**
	 * Draws the first target card from the deck.
	 *
	 * @return the drawn target card.
	 */
	public TargetCard getFirtTargetCard(){
		TargetCard ris= targetDeck.getFirst();
		targetDeck.removeFirst();
		return ris;
	}

	/**
	 * Draws the first initial card from the deck.
	 *
	 * @return the drawn initial card.
	 */
	public InitialCard getFirstInitialCard(){
		InitialCard ris= initialDeck.getFirst();
		initialDeck.removeFirst();
		return ris;
	}

	/**
	 * Gets the status of the match.
	 *
	 * @return the status of the match.
	 */
	public MatchStatus getStatus() {
		return status;
	}

	/**
	 * Sets the status of the match.
	 *
	 * @param status the status to set.
	 */
	public void setStatus(MatchStatus status) {
		this.status = status;
	}

	/**
	 * Adds a player to the match.
	 *
	 * @param p the player to add.
	 * @return true if the player was added, false if a player with the same nickname already exists.
	 */
	public boolean addPlayer(Player p) {
		for (Player player : players){
			if (Objects.equals(player.getNickname(), p.getNickname()))
				return false;
		}
		players.add(p);
		pt.getPlayerPoints().put(p,0);

		return true;
	}


	/**
	 * Adds a listener to the match.
	 *
	 * @param listener the listener to add.
	 */
	public void addListener(Listener listener) {
		listenerList.add(listener);
	}

	/**
	 * Gets the list of listeners for the match.
	 *
	 * @return the list of listeners.
	 */
	public List<Listener> getListenerList() {
		return listenerList;
	}

	/**
	 * Gets the round count of the match.
	 *
	 * @return the round count.
	 */
	public int getRoundCount() {
		return roundCount;
	}


	/**
	 * Gets the winners of the match.
	 *
	 * @return the list of winners.
	 */
	public List<Player> getWinners() {
		return winners;
	}

	/**
	 * Updates the points for a player based on a resource card.
	 *
	 * @param card the resource card.
	 * @param currentPlayer the player whose points are to be updated.
	 */
	public void updatePoint(ResourceCard card, Player currentPlayer) {
		if(card.isGoldCard() && card.getIsFront()){
			currentPlayer.currentScore +=((GoldCard) card).getGoalPoint(currentPlayer.getBoard());
		}
		else if(card.getIsFront()){
			currentPlayer.currentScore += card.getBasePoint();
		}
		if (currentPlayer.currentScore > pt.getMaxPoint())
			currentPlayer.currentScore = pt.getMaxPoint();

		getPt().updatePoint(currentPlayer);
	}

}
