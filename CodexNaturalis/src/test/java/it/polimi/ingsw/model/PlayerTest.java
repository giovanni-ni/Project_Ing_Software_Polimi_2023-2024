package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void getPlayerID() {
        Player p = new Player(PlayerColor.GREEN);
        assertEquals(PlayerColor.GREEN, p.getPlayerID());
    }
    @Test
    void setPlayerID() {
        Player p = new Player(PlayerColor.GREEN);
        p.setPlayerID(PlayerColor.RED);
        assertEquals(PlayerColor.RED, p.getPlayerID());
    }
}