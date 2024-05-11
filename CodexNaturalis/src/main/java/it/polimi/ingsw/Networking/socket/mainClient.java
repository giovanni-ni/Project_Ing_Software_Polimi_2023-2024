package it.polimi.ingsw.Networking.socket;

import it.polimi.ingsw.Message.ClientToServerMsg.*;
import it.polimi.ingsw.Message.Message;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

public class mainClient
{

        /**
         *
         * @param args
         * @throws IOException
         */
        public static void main(String[] args) throws IOException
        {
            //try with resource
            try (Socket socket = new Socket("localhost", 1234);) //open a socket
            {
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                /*while(true){
                    Scanner scanner = new Scanner(System.in);
                    System.out.print("Enter command: ");
                    String command = scanner.nextLine();
                    switch (command){
                        case "1" ->outputStream.writeObject(new CreateGameMessage("Mako"));
                        case "2"-> outputStream.writeObject(new drawCardMessage("mako",1,true,3));
                        case "3"-> outputStream.writeObject(new JoinFirstMessage("Mike"));
                        case "4"-> outputStream.writeObject( new JoinGameMessage("Mike",12));
                        case "5"->outputStream.writeObject( new LeaveMessage("Mike",12));
                        case "6"-> outputStream.writeObject( new NewChatMessageMessage("Mike"));

                        case null, default-> outputStream.writeObject( new CreateGameMessage("mao"));
                    }
                    outputStream.flush();
                    outputStream.reset();
                }*/


            }
        }

}
