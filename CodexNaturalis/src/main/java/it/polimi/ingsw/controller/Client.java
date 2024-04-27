package it.polimi.ingsw.controller;


import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client{
    private  Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String nickname;

    public Client(Socket socket){
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }catch (IOException e){
            closeEverything( socket, bufferedReader,bufferedWriter);
        }
    }


    public void sendMessage() {
        try {
            Scanner scanner =new Scanner(System.in);
            while(socket.isConnected()) {
                String msgToSend = scanner.nextLine();
                bufferedWriter.write(msgToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
                System.out.println(bufferedReader.readLine());
                if (msgToSend.equalsIgnoreCase("BYE"))
                    break;
            }
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }
/*
    public void listenForMessage(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                    }catch (IOException e){
                        closeEverything(socket, bufferedReader, bufferedWriter);
                    }
                }
            }

        }).start();
*/

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try {
            if (bufferedReader != null)
                bufferedReader.close();

            if (bufferedWriter != null)
                bufferedWriter.close();

            if (socket != null)
                socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        Socket socket = new Socket("172.20.10.2", 2234);
        Client client = new Client(socket);
        /*client.listenForMessage();*/
        client.sendMessage();
    }

}

/*public class Client {
    private static final String SERVER_IP="127.0.0.1";
    private static final int SERVER_PORT= 9090;

    private static final String FAILED = "failed";

    public static void main (String[] args)throws IOException {
        Socket socket = new Socket(SERVER_IP, SERVER_PORT);
        BufferedReader input = new BufferedReader ( new InputStreamReader(socket.getInputStream()));
        BufferedReader keyboard = new BufferedReader( new InputStreamReader(System.in));
        PrintWriter out =new PrintWriter(socket.getOutputStream(), true);
        while(true) {
            String in = keyboard.readLine();
            out.println(in);
            String serverResponse = input.readLine();
            if(!in.equals("QUIT")){
                break;
            }
            System.out.println("ERRORE");
        }
        socket.close();
        System.exit(0);
    }


}

*/