package it.polimi.ingsw.model;


import java.io.Serializable;
import java.util.*;

public class PointTable implements Serializable {
	//max point that a player can reach
	private final int maxPoint;
	//end Point
	private final int maxPlayerPoint;

	private Map<Player, Integer> playerPoints;


	public int getMaxPoint() {
		return maxPoint;
	}

	public int getMaxPlayerPoint() {
		return maxPlayerPoint;
	}

	public Map<Player, Integer> getPlayerPoints() {
		return playerPoints;
	}

	public void setPlayerPoints(Map<Player, Integer> playerPoints) {
		this.playerPoints = playerPoints;
	}

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

	public List<Player> findMaxPointPlayers() {
		return getPlayers(this.playerPoints.keySet());
	}

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

	public PointTable() {
		playerPoints = new HashMap<Player, Integer>();

		maxPlayerPoint=20;
		maxPoint=29;

	}
}
