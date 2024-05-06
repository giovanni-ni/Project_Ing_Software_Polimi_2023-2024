package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Player;

import java.io.IOException;
import java.util.ArrayList;

public class AllMatchesController {

    private static AllMatchesController instance =null;



    /**
     * Singleton Pattern
     * Il Singleton Pattern è un design pattern creazionale utilizzato nella
     * programmazione orientata agli oggetti per garantire che una classe abbia una
     * sola istanza e fornire un punto di accesso globale a tale istanza.
     * @return
     */

    public synchronized static AllMatchesController getInstance(){
        if(instance==null) {
            instance = new AllMatchesController();
        }
        return instance;
    }
    private ArrayList<SingleMatchController> runningControllers;

    public SingleMatchController createNewMatch(String nickname ) throws IOException {
        Player p= new Player(nickname);
        SingleMatchController c= new SingleMatchController();
        runningControllers.add(c);
        c.addPlayer(p);
        return c;
    }

}
