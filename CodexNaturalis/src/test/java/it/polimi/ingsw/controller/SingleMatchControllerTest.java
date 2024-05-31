package it.polimi.ingsw.controller;

import it.polimi.ingsw.Message.ClientToServerMsg.*;
import it.polimi.ingsw.Message.Message;
import it.polimi.ingsw.Message.ServerToClientMsg.*;
import it.polimi.ingsw.Networking.Listeners.Listener;
import it.polimi.ingsw.Networking.Listeners.SocketListener;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.MatchStatus;
import org.junit.jupiter.api.Test;
import org.w3c.dom.ls.LSException;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class SingleMatchControllerTest {

    Map<String, SocketListener> lis;
    public static class SocketListener implements Listener {

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
            this.msg.addFirst(msg);
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
    void mixTest() throws IOException, InterruptedException {
        AllMatchesController clt = new AllMatchesController();
        CreateGameMessage msg = new CreateGameMessage("paolo");
        JoinGameMessage msg1 = new JoinGameMessage("pablo", 0);
        OutputStream o = new OutputStream() {
            @Override
            public void write(int b) throws IOException {

            }
        };
        ObjectOutputStream out = new ObjectOutputStream(o);
        SocketListener l = new SocketListener(out);
        SocketListener l1 = new SocketListener(out);
        msg.setListener(l);
        msg1.setListener(l1);
        JoinGameMessage msg3 = new JoinGameMessage("antonio", 0);
        SocketListener l2 = new SocketListener(out);
        SocketListener l3 = new SocketListener(out);
        msg3.setListener(l2);

        lis = new HashMap<>();
        lis.put("paolo", l);
        lis.put("pablo", l1);
        lis.put("antonio", l2);


        clt.addInQueue(msg, msg.getListener());
        clt.addInQueue(msg1, msg1.getListener());
        clt.addInQueue(msg3, msg3.getListener());

        Thread.sleep(4000);


        SetReadyMessage ready1 = new SetReadyMessage(0, "paolo");
        SetReadyMessage ready2 = new SetReadyMessage(0, "pablo");
        SetReadyMessage ready3 = new SetReadyMessage(0, "antonio");

        clt.addInQueue(ready1, ready1.getListener());
        clt.addInQueue(ready2, ready2.getListener());
        clt.addInQueue(ready3, ready3.getListener());

        Thread.sleep(2000);

        assertEquals(l.msg.getFirst().getClass(), gameStartMsg.class);

        SetTargetCardMessage sMsg = new SetTargetCardMessage(0, "paolo", 0);
        SetTargetCardMessage sMsg1 = new SetTargetCardMessage(0, "pablo", 0);
        SetTargetCardMessage sMsg2 = new SetTargetCardMessage(0, "antonio", 0);
        clt.addInQueue(sMsg, sMsg.getListener());
        clt.addInQueue(sMsg1, sMsg1.getListener());
        clt.addInQueue(sMsg2, sMsg2.getListener());
        Thread.sleep(2000);

        assertEquals(l.msg.getFirst().getClass(), ActionSuccessMsg.class);

        FrontOrBackMessage fMsg = new FrontOrBackMessage(0,"paolo", false);
        FrontOrBackMessage fMsg1 = new FrontOrBackMessage(0,"pablo", false);
        FrontOrBackMessage fMsg2 = new FrontOrBackMessage(0,"antonio", false);
        clt.addInQueue(fMsg, fMsg.getListener());
        clt.addInQueue(fMsg1, fMsg1.getListener());
        clt.addInQueue(fMsg2, fMsg2.getListener());
        Thread.sleep(1000);

        assertEquals(l.msg.getFirst().getClass(),ActionSuccessMsg.class);

        String nickname = clt.getControllerbyId(0).getMatch().getCurrentPlayer().nickname;
        playCardMessage pMsg = new playCardMessage(nickname, 2, true, 5,5);
        clt.addInQueue(pMsg, pMsg.getListener());

        //playCardMessage pMsg1 = new playCardMessage("antonio", 2, true, 1,1);
        //clt.addInQueue(pMsg1, pMsg1.getListener());
        Thread.sleep(1000);

        assertEquals(lis.get(nickname).msg.getFirst().getClass(), ActionNotRecognize.class);
        //assertEquals(l2.msg.getFirst().getClass(), ActionNotRecognize.class);

        playCardMessage pMsg2 = new playCardMessage(nickname, 1, true, 1,1);
        clt.addInQueue(pMsg2, pMsg2.getListener());
        Thread.sleep(1000);

        assertEquals(lis.get(nickname).msg.getFirst().getClass(), playCardSuccess.class);

        drawCardMessage dMessage = new drawCardMessage(nickname, 0, true, 1);
        clt.addInQueue(dMessage, dMessage.getListener());
        Thread.sleep(1000);

        assertEquals(lis.get(nickname).msg.getFirst().getClass(), drawCardSuccess.class);

        ClientChatMessage chatMessage = new ClientChatMessage(0, "paolo", true,"", "ciao");
        clt.addInQueue(chatMessage, chatMessage.getListener());
        Thread.sleep(1000);

        for(String name: lis.keySet()) {
            if(!name.equals("paolo")) {
                assertEquals(lis.get(name).msg.getFirst().getClass(), ServerChatMessage.class);
            }
        }

        ClientChatMessage chatMessage1 = new ClientChatMessage(0, "paolo", false, "pablo", "ciao");
        clt.addInQueue(chatMessage1, chatMessage1.getListener());
        Thread.sleep(1000);

        assertEquals(lis.get("pablo").msg.getFirst().getClass(), ServerChatMessage.class);

        clt.getControllerbyId(0).getMatch().getCurrentPlayer().currentScore = 30;
        System.out.println(clt.getControllerbyId(0).getMatch().getCurrentPlayer().currentScore);

        nickname = clt.getControllerbyId(0).getMatch().getCurrentPlayer().nickname;
        playCardMessage pMsg1 = new playCardMessage(nickname, 1, true, 1,1);
        drawCardMessage dMessage1 = new drawCardMessage(nickname, 0, false, 1);
        clt.addInQueue(pMsg1, pMsg1.getListener());
        clt.addInQueue(dMessage1, dMessage1.getListener());
        Thread.sleep(1000);

        for(String n: lis.keySet()) {
            if(clt.getControllerbyId(0).getMatch().getCurrentPlayer().nickname.equals(n)) {
                assertEquals(lis.get(n).msg.getFirst().getClass(), NowIsYourRoundMsg.class);
                assertEquals(lis.get(n).msg.get(1).getClass(),  LastRoundMessage.class);
            } else {
                assertEquals( lis.get(n).msg.getFirst().getClass(), LastRoundMessage.class);
            }
        }


        GenericClientMessage generic = new GenericClientMessage("paolo");
        clt.addInQueue(generic, generic.getListener());
        Thread.sleep(1000);

        assertEquals(lis.get("paolo").msg.getFirst().getClass(), ActionNotRecognize.class);

        nickname = clt.getControllerbyId(0).getMatch().getCurrentPlayer().nickname.equals("paolo")? "pablo": "paolo";
        drawCardMessage d1 = new drawCardMessage(nickname, 0, true, 1);
        clt.addInQueue(d1, d1.getListener());
        Thread.sleep(1000);

        assertEquals(lis.get(nickname).msg.getFirst().getClass(), ActionNotRecognize.class);

        clt.getControllerbyId(0).getMatch().setStatus(MatchStatus.End);
        GenericClientMessage ge = new GenericClientMessage();
        clt.addInQueue(ge, ge.getListener());
        Thread.sleep(1000);

        for(String n: lis.keySet()) {
            assertEquals(lis.get(n).msg.getFirst().getClass(), endGameMessage.class);
        }

    }

    @Test
    void matchFullTest() throws InterruptedException, IOException {
        AllMatchesController clt = new AllMatchesController();
        CreateGameMessage msg = new CreateGameMessage("paolo");
        JoinGameMessage msg1 = new JoinGameMessage("pablo", 0);
        OutputStream o = new OutputStream() {
            @Override
            public void write(int b) throws IOException {

            }
        };
        ObjectOutputStream out = new ObjectOutputStream(o);
        SocketListener l = new SocketListener(out);
        SocketListener l1 = new SocketListener(out);
        msg.setListener(l);
        msg1.setListener(l1);
        JoinGameMessage msg3 = new JoinGameMessage("antonio", 0);
        JoinGameMessage msg4 = new JoinGameMessage("stefano", 0);
        SocketListener l2 = new SocketListener(out);
        SocketListener l3 = new SocketListener(out);
        msg3.setListener(l2);
        msg4.setListener(l3);

        clt.addInQueue(msg, msg.getListener());
        clt.addInQueue(msg1, msg1.getListener());
        clt.addInQueue(msg3, msg3.getListener());
        clt.addInQueue(msg4, msg4.getListener());

        Thread.sleep(4000);


        SetReadyMessage ready1 = new SetReadyMessage(0, "paolo");
        SetReadyMessage ready2 = new SetReadyMessage(0, "pablo");
        SetReadyMessage ready3 = new SetReadyMessage(0, "antonio");
        SetReadyMessage ready4 = new SetReadyMessage(0, "stefano");

        clt.addInQueue(ready1, ready1.getListener());
        clt.addInQueue(ready2, ready2.getListener());
        clt.addInQueue(ready3, ready3.getListener());
        clt.addInQueue(ready4, ready4.getListener());

        Thread.sleep(2000);

        assertEquals(l.msg.getFirst().getClass(), gameStartMsg.class);

        SetTargetCardMessage sMsg = new SetTargetCardMessage(0, "paolo", 0);
        SetTargetCardMessage sMsg1 = new SetTargetCardMessage(0, "pablo", 0);
        SetTargetCardMessage sMsg2 = new SetTargetCardMessage(0, "antonio", 0);
        SetTargetCardMessage sMsg3 = new SetTargetCardMessage(0, "stefano", 0);
        clt.addInQueue(sMsg, sMsg.getListener());
        clt.addInQueue(sMsg1, sMsg1.getListener());
        clt.addInQueue(sMsg2, sMsg2.getListener());
        clt.addInQueue(sMsg3, sMsg3.getListener());
        Thread.sleep(2000);

        assertEquals(l.msg.getFirst().getClass(), ActionSuccessMsg.class);
    }


    @Test
    void setMatchTest() throws IOException {
        Match m = new Match();

        SingleMatchController s = new SingleMatchController(0);
        s.setMatch(m);

        assertEquals(m, s.getMatch());
    }


}
