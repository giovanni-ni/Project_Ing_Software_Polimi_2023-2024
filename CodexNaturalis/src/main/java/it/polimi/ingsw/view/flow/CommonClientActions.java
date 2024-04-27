package it.polimi.ingsw.view.flow;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Deck;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public interface CommonClientActions {
    void createGame(String nickname) throws IOException, InterruptedException, NotBoundException;

    void joinFirstAvailble(String nickname) throws IOException, InterruptedException, NotBoundException;

    void joinGame(String nickname, int idGame) throws IOException, InterruptedException, NotBoundException;

    void reconnect(String nickname, int idGame) throws IOException, InterruptedException, NotBoundException;

    void leave(String nickname, int idGame) throws IOException, NotBoundException;

    void setAsReady() throws IOException;

    boolean isMyTurn() throws RemoteException;

    void giocaCarta(Card carta, boolean front, int x, int y);

    void pescaCarta(Deck deck, int number);

    void checkTurn(String nickname);

    void sendMessage(/*Message msg*/) throws RemoteException;  //message ancora non creato
}


