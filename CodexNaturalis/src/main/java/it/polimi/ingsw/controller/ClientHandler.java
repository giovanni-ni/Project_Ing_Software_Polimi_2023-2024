package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Player;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    public static ArrayList<ClientHandler> clientHandlers =new ArrayList<>();
    private ArrayList<Player> players= new ArrayList<>();
    private String nickname;
    private int index =0;
    public ClientHandler (Socket socket, int index) {
        try{
            this.socket= socket;
            this.index=index;
            this.bufferedWriter = new BufferedWriter( new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            clientHandlers.add(this);
        }catch ( IOException e){
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }
    @Override
    public void run(){
        while (socket.isConnected()){
            try{
                sendStringToAll("message has been received by the server");
                String messageFromClient = bufferedReader.readLine();
                System.out.println(messageFromClient);
            }
            catch (IOException e ){
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }

    public void sendStringToSpecificClient(int index, String singlemessage){
        try{
            clientHandlers.get(index).bufferedWriter.write(singlemessage);
            clientHandlers.get(index).bufferedWriter.newLine();
            clientHandlers.get(index).bufferedWriter.flush();
        }catch (IOException e ){
            closeEverything(socket, bufferedReader, bufferedWriter);
        }

    }
    public void sendStringToAll(String string){
        try {
            bufferedWriter.write(string);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }catch (IOException e){
            closeEverything(socket, bufferedReader, bufferedWriter);
        }

    }

/*
    public void broadcastMessage(String messageToSend){
        for(ClientHandler clientHandler: clientHandlers){
            try{
                if(!clientHandler.nickname.equals(nickname)){
                    clientHandler.bufferedWriter.write(messageToSend);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }

            }catch(IOException e){
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }
    public void sendMessage(String singlemassge, int index){
        try{
            clientHandlers.get(index).bufferedWriter.write(singlemassge);
            clientHandlers.get(index).bufferedWriter.newLine();
            clientHandlers.get(index).bufferedWriter.flush();
        }catch (IOException e ){
            closeEverything(socket, bufferedReader, bufferedWriter);
        }

    }
    public void setNickname(String nickname){this.nickname= nickname;};
*/
    public void removeClientHandler(){
        clientHandlers.remove(this);
    }
    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        removeClientHandler();
        try {
            if (bufferedReader != null)
                bufferedReader.close();

            if (bufferedWriter != null)
                bufferedWriter.close();

            if (socket != null)
                socket.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
/*
    @Override
    public void run() {
        try {
            while (true){
                out.println("if you want to enter game, write ENTER");
                String request =in.readLine();
                /*if(request.contains("ENTER")){
                    out.println("choose a nickname");
                    request= in.readLine();
                    Player player = new Player(index, request);;
                }
            }

        }catch (IOException e){
            System.err.println("IO expetion in client handle");
            System.err.println(e.getStackTrace());
        } finally {
            out.close();
            try {
                in.close();
            } catch (IOException e){
                e.getStackTrace();
            }
        }
}*/
