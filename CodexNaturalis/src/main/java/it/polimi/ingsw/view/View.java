package it.polimi.ingsw.view;

import it.polimi.ingsw.Message.Message;

import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
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

    void showWhoIsPlaying();

    void showBoard();

    void askPlayerMove();

     void askJoinFirst() throws InterruptedException, RemoteException;

     void askSetReady() throws InterruptedException, RemoteException;

     void askDrawCard() throws InterruptedException, RemoteException;

     void askPlayCard() throws InterruptedException, RemoteException;



}


