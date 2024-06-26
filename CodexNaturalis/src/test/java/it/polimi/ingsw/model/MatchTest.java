package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.view.TextualInterfaceUnit.Print.print;
import static org.junit.jupiter.api.Assertions.*;

class MatchTest {
    CardParsing cp = new CardParsing();
    ArrayList<InitialCard> initialCards = (ArrayList<InitialCard>) cp.loadInitialCards();
    ArrayList<TargetCard> targetCards = (ArrayList<TargetCard>) cp.loadTargetCards();
    ArrayList<ResourceCard> resourceCards = (ArrayList<ResourceCard>) cp.loadResourceCards();

    MatchTest() throws IOException {
    }


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
        assertEquals(testMatch.getFirstPlayer(),testMatch.getCurrentPlayer().nickname);
        assertEquals("Pippo",testMatch.getCurrentPlayer().nickname);
        testMatch.nextPlayer();
        assertEquals("Franco",testMatch.getCurrentPlayer().nickname);
        testMatch.nextPlayer();
        assertEquals("Pippo",testMatch.getCurrentPlayer().nickname);

        testMatch.setPlayerReady("Pippo");
        assertTrue(player1.getReady());

        assertFalse(testMatch.isAllPlayersReady());

        testMatch.setPlayerReady("Franco");
        assertTrue(player1.getReady());

        assertTrue(testMatch.isAllPlayersReady());

        assertTrue(testMatch.getListenerList().isEmpty());

        ArrayList<Player> oldPlayers = testMatch.getPlayers();
        testMatch.setPlayers(oldPlayers);

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
        assertTrue(player1.getReady());

        testMatch.setPlayerReady("Franco");
        assertTrue(player1.getReady());

        assertTrue(testMatch.isAllPlayersReady());

        //getDeck methods check

        assertEquals( testMatch.getInitialDeck().size(), cp.loadInitialCards().size());
        assertEquals( testMatch.getTargetDeck().size(), targetCards.size());
        assertEquals( testMatch.getListenerList().size(), 0);

        // common target
        ArrayList<TargetCard> commonTargets = new ArrayList<>();
        commonTargets.add(targetCards.get(0));
        commonTargets.add(targetCards.get(1));

        testMatch.setCommonTarget(commonTargets);

        // Winner
        oldPlayers.get(0).setTarget(targetCards.get(4));
        Board board = new Board(initialCards.getFirst());
        oldPlayers.get(0).setBoard(board);
        board= new Board(initialCards.get(1));
        oldPlayers.get(1).setTarget(targetCards.get(5));
        oldPlayers.get(1).setBoard(board);


        assertEquals(testMatch.getPt().findMaxPointPlayers().size(), testMatch.getPlayers().size());
        testMatch.setWinners();
        assertEquals(testMatch.getWinners().size(),testMatch.getPlayers().size());

        resourceCards.get(7).setFront(true);
        oldPlayers.get(1).getBoard().addCard(resourceCards.get(7),1, 1);

        testMatch.updatePoint(resourceCards.get(7),oldPlayers.get(0));
        assertEquals(resourceCards.get(7).getBasePoint(),1);

        testMatch.setWinners();
        assertEquals(testMatch.getWinners().size(),1);

        resourceCards.get(8).setFront(true);

        //addd cards so that player 1 has 3 points like player 2
        assertTrue(oldPlayers.get(0).getBoard().addCard(resourceCards.get(7),4, 4));


        testMatch.updatePoint(resourceCards.get(7),oldPlayers.get(0));

        //add cards that player 2 can reach the target of his secret goal
        oldPlayers.get(1).getBoard().addCard(resourceCards.get(9),2, 2);
        oldPlayers.get(1).getBoard().addCard(resourceCards.get(3),3, 3);
        oldPlayers.get(1).getBoard().addCard(resourceCards.get(4),4,4);

        assertEquals(oldPlayers.get(1).getBoard().getCounterOfElements().get(Elements.MUSHROOMS),4);

       assertEquals(targetCards.get(4).checkGoal(oldPlayers.get(1).getBoard()),1);
       oldPlayers.get(1).setTarget(targetCards.get(4));
        print(oldPlayers.get(1).getTarget().countPoint(oldPlayers.get(1).getBoard()));
        testMatch.setWinners();
        assertEquals(testMatch.getWinners().size(),1);

    }
    @Test
    void GetWinnerWithSameTargetCount() throws IOException{
        Match testMatch = new Match(12);
        Player player1 = new Player("Pippo");
        Player player2 = new Player("Franco");
        assertTrue (testMatch.addPlayer(player1));
        assertTrue(testMatch.addPlayer(player2));
        ArrayList<Player> oldPlayers =testMatch.getPlayers();
        oldPlayers.get(0).setTarget(targetCards.get(4));
        Board board = new Board(initialCards.getFirst());
        oldPlayers.get(0).setBoard(board);
        board= new Board(initialCards.get(1));
        oldPlayers.get(1).setTarget(targetCards.get(4));
        oldPlayers.get(1).setBoard(board);

        ArrayList<TargetCard> commonTargets = new ArrayList<>();
        commonTargets.add(targetCards.get(0));
        commonTargets.add(targetCards.get(1));

        testMatch.setCommonTarget(commonTargets);

        //add cards that player 2 can reach the target of his secret goal
        oldPlayers.get(1).getBoard().addCard(resourceCards.get(9),2, 2);
        oldPlayers.get(1).getBoard().addCard(resourceCards.get(3),3, 3);
        oldPlayers.get(1).getBoard().addCard(resourceCards.get(4),1,1);

        oldPlayers.get(0).getBoard().addCard(resourceCards.get(9),2, 2);
        oldPlayers.get(0).getBoard().addCard(resourceCards.get(3),3, 3);
        oldPlayers.get(0).getBoard().addCard(resourceCards.get(4),1,1);

        assertEquals(oldPlayers.get(1).getBoard().getCounterOfElements().get(Elements.MUSHROOMS),4);

        assertEquals(targetCards.get(4).checkGoal(oldPlayers.get(1).getBoard()),1);
        oldPlayers.get(1).setTarget(targetCards.get(4));
        print(oldPlayers.get(1).getTarget().countPoint(oldPlayers.get(1).getBoard()));
        testMatch.setWinners();
        assertEquals(testMatch.getWinners().size(),2);
    }

}