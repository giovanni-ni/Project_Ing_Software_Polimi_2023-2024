package it.polimi.ingsw.view;

import it.polimi.ingsw.Message.Message;
import it.polimi.ingsw.Networking.Listeners.ViewListener;

import java.util.List;

public interface View {

    void askLogin() throws Exception;

    void askMenuAction() throws Exception;

    boolean askCreateMatch() throws Exception;

    int askMaxSeats() throws Exception;

    boolean askJoinMatch() throws Exception;
}
