package it.polimi.ingsw.model;


import java.io.Serializable;
import java.util.*;

public class PointTable implements Serializable {

	//max point that a player can reach
	private final int maxPoint;
	//end Point
	private final int maxPlayerPoint;

	private Map<Player, Integer> playerPoints;


	/**
	 * Getter of the max point that a player can reach
	 * @return : integer of max point that a player can reach
	 */
	public int getMaxPoint() {
		return maxPoint;
	}

	/**
	 * Getter of the point that turns the game last round
	 * @return : integer of the point that turns the game last round
	 */
	public int getMaxPlayerPoint() {
		return maxPlayerPoint;
	}

	/**
	 * Getter of the map with player and his point
	 * @return : Map with the key of player and the value as the respective point during the game
	 */
	public Map<Player, Integer> getPlayerPoints() {
		return playerPoints;
	}

	/**
	 * Setter of the map with player and his point
	 * @param playerPoints : Map with the key of player and the value as the respective point during the game that would be set
	 */
	public void setPlayerPoints(Map<Player, Integer> playerPoints) {
		this.playerPoints = playerPoints;
	}

	/**
	 * Update of the player map with the respective point
	 * this Method will avoid that the player point would be higher that the max point reachable
	 * @param p : player that may have a new point that would be updated in the map
	 */
	public void updatePoint(Player p){
		Player playerToRemove = null;
		for (Player player : playerPoints.keySet())
			if (Objects.equals(player.getNickname(), p.getNickname()))
				playerToRemove=player;
		playerPoints.remove(playerToRemove);
		if(p.currentScore>=maxPoint)
			p.currentScore=maxPoint;
		playerPoints.put(p,p.currentScore);
	}

	/**
	 * Getter of max point from the players in game
	 * @return : Integer of the max point of the players
	 */
	public int findMaxPoint() {
		int max=0;
		int playerPoint;
		Set<Player> players = playerPoints.keySet();

		for(Player p : players){
			playerPoint =playerPoints.get(p);

			if (playerPoint > max){
				max=playerPoint;

			}
		}

		return max;
	}

	/**
	 * Getter of the players who has the greater point in the game
	 * @return : List of the max point players
	 */
	public List<Player> findMaxPointPlayers() {
		return getPlayers(this.playerPoints.keySet());
	}

	/**
	 * Getter of the players who has the greater point in the game
	 * @return : List of the max point players
	 */
	private List<Player> getPlayers(Set<Player> players) {
		int max=0;
		int playerPoint;
		List<Player> maxPlayers = new ArrayList<>();

		for(Player p : players){
			playerPoint = playerPoints.get(p);
			if(playerPoint == max){
				maxPlayers.add(p);
			}else if (playerPoint > max){
				max=playerPoint;
				maxPlayers.clear();
				maxPlayers.add(p);
			}
		}

		return maxPlayers;
	}

	/**
	 * Getter of the number of the target completed by the player
	 * This method will count the common targets and the private one
	 * It will count only once if the target is completed
	 * @return : Integer of the count fo targets completed
	 */
	public int CountTarget(Player p, ArrayList<TargetCard> targetCards) {
		int countOfTarget = 0;
		if (p.getTarget().countPoint(p.getBoard()) > 0)
			countOfTarget++;
		for (TargetCard tc : targetCards) {
			if (tc.countPoint(p.getBoard()) > 0)
				countOfTarget++;
		}
		return countOfTarget;

	}

	/**
	 * Constructor of the Point table with the point of last round and the limit of the player's point
	 */
	public PointTable() {
		playerPoints = new HashMap<Player, Integer>();

		maxPlayerPoint=20;
		maxPoint=29;

	}
}
