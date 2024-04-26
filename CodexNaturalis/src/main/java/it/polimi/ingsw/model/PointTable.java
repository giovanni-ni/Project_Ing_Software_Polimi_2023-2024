package it.polimi.ingsw.model;


import java.util.*;

public class PointTable {

	private int maxPoint;

	private int maxPlayerPoint;

	private Map<Player, Integer> playerPoints;

	private Map<Player, Integer> targetPoints;

	public int getMaxPoint() {
		return maxPoint;
	}

	public void setMaxPoint(int maxPoint) {
		this.maxPoint = maxPoint;
	}

	public int getMaxPlayerPoint() {
		return maxPlayerPoint;
	}

	public void setMaxPlayerPoint(int maxPlayerPoint) {
		this.maxPlayerPoint = maxPlayerPoint;
	}

	public Map<Player, Integer> getPlayerPoints() {
		return playerPoints;
	}

	public void setPlayerPoints(Map<Player, Integer> playerPoints) {
		this.playerPoints = playerPoints;
	}

	public Map<Player, Integer> getTargetPoints() {
		return targetPoints;
	}

	public void setTargetPoints(Map<Player, Integer> targetPoints) {
		this.targetPoints = targetPoints;
	}

	public void updatePoint(Player p){
		playerPoints.put(p,p.currentScore);
	}

	public int findMaxPoint(List<Player> players) {
		int max=0;
		int playerPoint;

		for(Player p : players){
			playerPoint =playerPoints.get(p);

			if (playerPoint > max){
				max=playerPoint;

			}
		}

		return max;
	}
	public List<Player> findMaxTargetPlayers() {
		return getPlayers(this.targetPoints.keySet(), targetPoints);

	}

	public List<Player> findMaxPointPlayers() {
		return getPlayers(this.playerPoints.keySet(), playerPoints);
	}

	private List<Player> getPlayers(Set<Player> players, Map<Player, Integer> points) {
		int max=0;
		int playerPoint;
		List<Player> maxPlayers = new ArrayList<>();

		for(Player p : players){
			playerPoint = points.get(p);
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
	/*
	public void updateCountTarget(Player p,Board board, TargetCard[] targetCards) {
		int countOfTarget=0;
		if(p.getTarget().checkTarget(board)>0)
			countOfTarget++;
		for (TargetCard tc:targetCards){
			if (tc.checkTarget(board)>0)
				countOfTarget++;
		}
		targetPoints.put(p,countOfTarget);
	}
*/

	public PointTable() {
		playerPoints = new HashMap<Player, Integer>();
		targetPoints = new HashMap<Player,Integer>();

		maxPlayerPoint=0;
		maxPoint=29;

	}
}
