package it.polimi.ingsw.model;

import it.polimi.ingsw.Networking.Listeners.Listener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ViewModel implements Serializable {
    public final int idMatch;

    private final boolean autoStart;
    private final PointTable pt;

    private final ArrayList<Player> players;

    private final ArrayList<InitialCard> initialDeck;

    private final ArrayList<ResourceCard> resourceDeck;

    private final ArrayList<GoldCard> goldDeck;

    private final ArrayList<TargetCard> targetDeck;

    private final ArrayList<TargetCard> commonTarget;

    private final MatchStatus status;

    private final String firstPlayer;

    private final int roundCount=0;

    private final List<Player> winners;
    private final Player currentPlayer;

    private final List<PlayerColor> playerColors;

    public ViewModel(Match match) {
        this.autoStart = match.getAutoStart();
        this.idMatch = match.idMatch;
        this.pt = match.getPt();
        this.players = match.getPlayers();
        this.initialDeck = match.getInitialDeck();
        this.resourceDeck = match.getResourceDeck();
        this.goldDeck = match.getGoldDeck();
        this.targetDeck = match.getTargetDeck();
        this.commonTarget = match.getCommonTarget();
        this.status = match.getStatus();
        this.firstPlayer = match.getFirstPlayer();
        this.winners = match.getWinners();
        this.currentPlayer = match.getCurrentPlayer();
        this.playerColors = match.getNotChosenColor();
    }

    public int getIdMatch() {
        return idMatch;
    }

    public PointTable getPt() {
        return pt;
    }

    public ArrayList<Player> getPlayers() {
        return players;
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

    public MatchStatus getStatus() {
        return status;
    }

    public String getFirstPlayer() {
        return firstPlayer;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public int getRoundCount() {
        return roundCount;
    }

    public List<Player> getWinners() {
        return winners;
    }

    public Player getPlayerByNickname(String nickname) {
        for(Player p: this.players) {
            if(p.getNickname().equals(nickname)) {
                return p;
            }
        }
        return null;
    }

    public boolean getAutostart() {
        return this.autoStart;
    }

    public List<PlayerColor> getPlayerColors() {
        return playerColors;
    }
}
