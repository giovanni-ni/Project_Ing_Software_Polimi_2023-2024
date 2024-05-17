package it.polimi.ingsw.view;

import it.polimi.ingsw.Message.Message;
import it.polimi.ingsw.model.Coordinate;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.util.Collections;
import java.util.List;

public interface View {

    void askLogin() throws Exception;

    void askMenuAction() throws Exception;

    boolean askCreateMatch() throws Exception;

    //int askMaxSeats() throws Exception;

    boolean askJoinMatch() throws Exception;

    boolean askLeaveMatch() throws RemoteException;

    boolean askExitGame() throws RemoteException;

    void showCommonGoals();

    void showPersonalGoal() throws RemoteException;

    void announceCurrentPlayer() throws RemoteException;

    void showBoard();

    void drawCard() throws IOException, InterruptedException;

     void askJoinFirst() throws InterruptedException, RemoteException;

     void askSetReady() throws InterruptedException, RemoteException;

     void askDrawCard() throws InterruptedException, RemoteException;

     void askPlayCard() throws InterruptedException, RemoteException;

     void askChat() throws RemoteException, InterruptedException;

     void endGame();

}


