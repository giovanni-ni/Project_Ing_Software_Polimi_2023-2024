package it.polimi.ingsw.view.Gui.SceneControllers;

import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.Gui.GUIApplication;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class BoardControllerTest {
    private Match getStartedMatch() throws IOException {
        Match startedMatch = new Match(0);
        Player p1 = new Player("Pippo");
        p1.setReady(true);
        Player p2 = new Player("Franco");
        p2.setReady(true);
        startedMatch.addPlayer(p1);
        startedMatch.addPlayer(p2);
    return startedMatch;
    }
    @Test
    void chatTest(){
        GUIApplication guiApplication = new GUIApplication();
        guiApplication.main(null);

    }

}