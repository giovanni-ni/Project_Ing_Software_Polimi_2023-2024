package it.polimi.ingsw.view.flow;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.Message;

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

    void playCard(Card card, boolean front, int x, int y) throws IOException;

    void drawCard(Deck deck, int number) throws IOException;

    void sendMessage(Message msg) throws IOException;  //message ancora non creato
}


