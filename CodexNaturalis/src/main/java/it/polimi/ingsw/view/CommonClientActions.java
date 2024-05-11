package it.polimi.ingsw.view;

import it.polimi.ingsw.Message.Message;

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

    void playCard(int CardIndex, boolean front, int x, int y) throws IOException;

    void drawCard(boolean deck, int number) throws IOException;

    void sendMessage(Message msg) throws IOException;  //message ancora non creato
}


