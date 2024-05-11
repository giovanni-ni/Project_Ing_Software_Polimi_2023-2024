package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class MatchTest {
    @Test
    void MatchBasicMethods() throws IOException {
        //two type of constructor check if MatchId is set properly
        Match testMatch = new Match(12);
        assertEquals(testMatch.getIdMatch(),12);
        testMatch.setIdMatch(13);
        assertNotEquals(testMatch.idMatch,14);
        Match testMatch2 = new Match();
        testMatch.setPt(testMatch.getPt());

        // Player methods, add, next player
        assertEquals(testMatch.getPlayers().size(),0);
        assertFalse(testMatch.getResourceDeck().isEmpty());
        assertFalse(testMatch.getGoldDeck().isEmpty());
        testMatch.getCommonTarget().add(new CountTargetCard());
        assertFalse(testMatch.getCommonTarget().isEmpty());
        assertNull(testMatch.getCurrentPlayer());
        Player player1 = new Player("Pippo");
        Player player2 = new Player("Franco");
        assertTrue (testMatch.addPlayer(player1));
        assertTrue(testMatch.addPlayer(player2));
        assertFalse(testMatch.addPlayer(new Player("Pippo")));
        assertEquals(testMatch.getPlayers().size(),2);
        testMatch.setCurrentPlayer(testMatch.getPlayers().getFirst());
        testMatch.setFirstPlayer(testMatch.getCurrentPlayer().nickname);
        assertEquals("Pippo",testMatch.getCurrentPlayer().nickname);
        testMatch.nextPlayer();
        assertEquals("Franco",testMatch.getCurrentPlayer().nickname);
        testMatch.nextPlayer();
        assertEquals("Pippo",testMatch.getCurrentPlayer().nickname);

        testMatch.setPlayerReady("Pippo");
        assertEquals(player1.getReady(),true);

        assertFalse(testMatch.isAllPlayersReady());

        testMatch.setPlayerReady("Franco");
        assertEquals(player1.getReady(),true);

        assertTrue(testMatch.isAllPlayersReady());

    }

}