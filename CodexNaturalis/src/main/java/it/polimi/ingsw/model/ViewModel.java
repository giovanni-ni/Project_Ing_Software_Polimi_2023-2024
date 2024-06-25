package it.polimi.ingsw.model;

import it.polimi.ingsw.Networking.Listeners.Listener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a read-only view model of a match.
 */
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

    /**
     * Constructs a ViewModel from a Match object.
     *
     * @param match the Match object to create the ViewModel from.
     */
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

    /**
     * Gets the match ID.
     *
     * @return the match ID.
     */
    public int getIdMatch() {
        return idMatch;
    }

    /**
     * Gets the list of players in the match.
     *
     * @return the list of players.
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * Gets the initial deck of cards.
     *
     * @return the initial deck of cards.
     */
    public ArrayList<InitialCard> getInitialDeck() {
        return initialDeck;
    }

    /**
     * Gets the deck of resource cards.
     *
     * @return the deck of resource cards.
     */
    public ArrayList<ResourceCard> getResourceDeck() {
        return resourceDeck;
    }

    /**
     * Gets the deck of gold cards.
     *
     * @return the deck of gold cards.
     */
    public ArrayList<GoldCard> getGoldDeck() {
        return goldDeck;
    }

    /**
     * Gets the deck of target cards.
     *
     * @return the deck of target cards.
     */
    public ArrayList<TargetCard> getTargetDeck() {
        return targetDeck;
    }

    /**
     * Gets the common target cards.
     *
     * @return the list of common target cards.
     */
    public ArrayList<TargetCard> getCommonTarget() {
        return commonTarget;
    }

    /**
     * Gets the status of the match.
     *
     * @return the match status.
     */
    public MatchStatus getStatus() {
        return status;
    }

    /**
     * Gets the nickname of the first player.
     *
     * @return the nickname of the first player.
     */
    public String getFirstPlayer() {
        return firstPlayer;
    }

    /**
     * Gets the nickname of the first player.
     *
     * @return the nickname of the first player.
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Gets the number of rounds.
     *
     * @return the round count.
     */
    public int getRoundCount() {
        return roundCount;
    }

    /**
     * Gets the list of winners.
     *
     * @return the list of winners.
     */
    public List<Player> getWinners() {
        return winners;
    }

    /**
     * Gets a player by their nickname.
     *
     * @param nickname the nickname of the player.
     * @return the player with the given nickname, or null if not found.
     */
    public Player getPlayerByNickname(String nickname) {
        for(Player p: this.players) {
            if(p.getNickname().equals(nickname)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Checks if the match is set to auto-start.
     *
     * @return true if the match is set to auto-start, false otherwise.
     */
    public boolean getAutostart() {
        return this.autoStart;
    }

    /**
     * Gets the list of player colors.
     *
     * @return the list of player colors.
     */
    public List<PlayerColor> getPlayerColors() {
        return playerColors;
    }
}
