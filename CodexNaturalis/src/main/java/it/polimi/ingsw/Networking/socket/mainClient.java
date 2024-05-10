package it.polimi.ingsw.Networking.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

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


                //receives data
                InputStream input = socket.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                System.out.println("Client: start receiving");
                String line = reader.readLine();    // reads a line of text
                System.out.println(line);

                //close the socket
                socket.close();
            }
        }


}
