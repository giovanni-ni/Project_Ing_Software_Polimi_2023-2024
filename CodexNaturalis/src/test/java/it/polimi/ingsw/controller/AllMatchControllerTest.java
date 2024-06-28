package it.polimi.ingsw.controller;

import it.polimi.ingsw.Message.ClientToServerMsg.*;
import it.polimi.ingsw.Message.Message;
import it.polimi.ingsw.Message.ServerToClientMsg.ActionNotRecognize;
import it.polimi.ingsw.Message.ServerToClientMsg.JoinFailMsg;
import it.polimi.ingsw.Message.ServerToClientMsg.JoinSuccessMsg;
import it.polimi.ingsw.Networking.Listeners.Listener;
import it.polimi.ingsw.model.Match;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class AllMatchControllerTest {



    public static class SocketListener implements Listener{

        public List<Message> msg;

        private transient final ObjectOutputStream out;

        private String nickname;

        private Integer GameID ;

        public SocketListener(ObjectOutputStream outputStream) {
            this.msg = new ArrayList<>();
            this.out = outputStream;
        }

        @Override
        public void update(Message msg) {
            this.msg.add(msg);
        }
        @Override
        public void setNickname(String nickname) {
            this.nickname =nickname;
        }
        private void finishSending() throws IOException {
            out.flush();
            out.reset();
        }

        public OutputStream getOut() {
            return out;
        }
        @Override
        public String getNickname() {
            return nickname;
        }

        @Override
        public Integer getGameID() {
            return GameID;
        }

        @Override
        public void setGameID(Integer gameID) {
            GameID = gameID;
        }
    }

    @Test
    void createGameTest() throws IOException {
        AllMatchesController testC = new AllMatchesController();
        CreateGameMessage msg = new CreateGameMessage("paolo");
        OutputStream o = new OutputStream() {
            @Override
            public void write(int b) throws IOException {

            }
        };
        ObjectOutputStream out = new ObjectOutputStream(o);
        Listener l = new SocketListener(out);

        msg.setListener(l);
        testC.createNewMatch(msg);
        Match match = new Match();
        Message msg2 = new JoinSuccessMsg(match);
        assertEquals(testC.createNewMatch(msg).getClass(), JoinSuccessMsg.class);
    }

    @Test
    void joinMatchTest() throws IOException, InterruptedException {
        AllMatchesController clt = new AllMatchesController();
        CreateGameMessage msg = new CreateGameMessage("paolo");
        JoinGameMessage msg1 = new JoinGameMessage("pablo", 0);
        OutputStream o = new OutputStream() {
            @Override
            public void write(int b) throws IOException {

            }
        };
        ObjectOutputStream out = new ObjectOutputStream(o);
        Listener l = new SocketListener(out);
        SocketListener l1 = new SocketListener(out);
        msg.setListener(l);
        msg1.setListener(l1);

        clt.addInQueue(msg,l);
        Thread.sleep(1000);
        clt.addInQueue(msg1, l1);
        Thread.sleep(1000);


        assertEquals(l1.msg.getFirst().getClass(), JoinSuccessMsg.class);

        JoinGameMessage msg2 = new JoinGameMessage("antonio", 1);
        msg2.setListener(new SocketListener(out));
        assertEquals(clt.joinMatch(msg2).getClass(), JoinFailMsg.class);    //gameid not found

        JoinGameMessage msg3 = new JoinGameMessage("antonio", 0);
        JoinGameMessage msg4 = new JoinGameMessage("marco", 0);
        JoinGameMessage msg5 = new JoinGameMessage("franco", 0);
        Listener l2 = new SocketListener(out);
        Listener l3 = new SocketListener(out);
        Listener l4 = new SocketListener(out);
        msg3.setListener(l2);
        msg4.setListener(l3);
        msg5.setListener(l4);
        clt.addInQueue(msg3,l2);
        clt.addInQueue(msg4,l3);
        clt.addInQueue(msg5,l4);

        Thread.sleep(1000);

        JoinGameMessage msg6 = new JoinGameMessage("manuel", 0);
        Listener l5 = new SocketListener(out);
        msg6.setListener(l5);

        assertEquals(clt.joinMatch(msg6).getClass(), JoinFailMsg.class);    //Match full

    }

    @Test
    void joinFirstTest() throws IOException, InterruptedException {

        AllMatchesController clt = new AllMatchesController();
        JoinFirstMessage msg = new JoinFirstMessage("pablo");
        OutputStream o = new OutputStream() {
            @Override
            public void write(int b) throws IOException {

            }
        };
        ObjectOutputStream out = new ObjectOutputStream(o);
        SocketListener l = new SocketListener(out);
        msg.setListener(l);
        clt.addInQueue(msg, l);
        Thread.sleep(1000);
        assertEquals(l.msg.getFirst().getClass(), JoinSuccessMsg.class);
    }
    @Test
    void actionNotRecognizeTest() throws IOException, InterruptedException {
        AllMatchesController clt = new AllMatchesController();
        GenericClientMessage msg = new GenericClientMessage("pablo");
        msg.setMainControllerMessage(true);
        OutputStream o = new OutputStream() {
            @Override
            public void write(int b) throws IOException {

            }
        };
        ObjectOutputStream out = new ObjectOutputStream(o);
        SocketListener l = new SocketListener(out);
        msg.setListener(l);
        clt.addInQueue(msg, l);
        Thread.sleep(1000);
        assertEquals(l.msg.getFirst().getClass(), ActionNotRecognize.class);
    }

    @Test
    void getInstanceTest() throws IOException {
        AllMatchesController clt = new AllMatchesController();
        assertEquals(AllMatchesController.getInstance().getClass(), AllMatchesController.class);

    }

    @Test
    void joinFirstSecondTest() throws IOException, InterruptedException {
        AllMatchesController clt = new AllMatchesController();
        CreateGameMessage msg = new CreateGameMessage("paolo");
        JoinGameMessage msg1 = new JoinGameMessage("pablo", 0);
        OutputStream o = new OutputStream() {
            @Override
            public void write(int b) throws IOException {

            }
        };
        ObjectOutputStream out = new ObjectOutputStream(o);
        Listener l = new SocketListener(out);
        Listener l1 = new SocketListener(out);
        msg.setListener(l);
        msg1.setListener(l1);



        JoinGameMessage msg3 = new JoinGameMessage("antonio", 0);
        JoinGameMessage msg4 = new JoinGameMessage("marco", 0);
        Listener l2 = new SocketListener(out);
        Listener l3 = new SocketListener(out);
        msg3.setListener(l2);
        msg4.setListener(l3);


        clt.addInQueue(msg,l);
        clt.addInQueue(msg1,l1);
        clt.addInQueue(msg3, l2);
        clt.addInQueue(msg4, l3);

        Thread.sleep(1000);

        CreateGameMessage cr = new CreateGameMessage("franco");
        SocketListener l4 = new SocketListener(out);
        cr.setListener(l4);
        clt.addInQueue(cr,l4);

        JoinFirstMessage j = new JoinFirstMessage("anna");
        SocketListener l5 = new SocketListener(out);
        j.setListener(l5);
        clt.addInQueue(j, l5);
        Thread.sleep(1000);

        assertEquals(l5.msg.getFirst().getClass(), JoinSuccessMsg.class);
    }

    @Test
    void matchStartedTest() throws IOException, InterruptedException {
        AllMatchesController clt = new AllMatchesController();
        CreateGameMessage msg = new CreateGameMessage("paolo");
        JoinGameMessage msg1 = new JoinGameMessage("pablo", 0);
        OutputStream o = new OutputStream() {
            @Override
            public void write(int b) throws IOException {

            }
        };
        ObjectOutputStream out = new ObjectOutputStream(o);
        Listener l = new SocketListener(out);
        Listener l1 = new SocketListener(out);
        msg.setListener(l);
        msg1.setListener(l1);
        JoinGameMessage msg2 = new JoinGameMessage("annamaria", 0);
        msg2.setListener(new SocketListener(out));
        JoinGameMessage msg3 = new JoinGameMessage("antonio", 0);
        Listener l2 = new SocketListener(out);
        msg3.setListener(l2);

        SetReadyMessage ready  = new SetReadyMessage(0, "annamaria");
        SetReadyMessage ready1 = new SetReadyMessage(0, "paolo");
        SetReadyMessage ready2 = new SetReadyMessage(0, "pablo");
        SetReadyMessage ready3 = new SetReadyMessage(0, "antonio");

        clt.addInQueue(msg,l);
        clt.addInQueue(msg1,msg1.getListener());
        clt.addInQueue(msg2,msg2.getListener());
        clt.addInQueue(msg3, msg3.getListener());
        clt.addInQueue(ready, ready.getListener());
        clt.addInQueue(ready1, ready1.getListener());
        clt.addInQueue(ready2, ready2.getListener());
        clt.addInQueue(ready3, ready3.getListener());

        Thread.sleep(1000);

        JoinGameMessage msg4 = new JoinGameMessage("pippo", 0);
        SocketListener s = new SocketListener(out);
        msg4.setListener(s);

        clt.addInQueue(msg4, msg4.getListener());
        Thread.sleep(1000);

        assertEquals(s.msg.getFirst().getClass(), JoinFailMsg.class);

        CrashMsg crash = new CrashMsg("antonio");
        SocketListener l9 = new SocketListener(out);
        l9.setGameID(0);
        crash.setListener(l9);
        clt.addInQueue(crash, crash.getListener());
        Thread.sleep(2000);
        assertEquals(clt.getControllerbyId(0), null);

    }
}
