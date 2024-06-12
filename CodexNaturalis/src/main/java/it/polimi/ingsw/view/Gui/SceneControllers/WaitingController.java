package it.polimi.ingsw.view.Gui.SceneControllers;

import it.polimi.ingsw.Message.ClientToServerMsg.SetReadyMessage;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayerColor;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class WaitingController extends GenericSceneController implements Initializable {

    private  HashMap<Integer,ImageView> ballList;
    private  HashMap<Integer,ImageView> tickList;
    private  HashMap<Integer, Text> nickList;
    boolean isReady = false;


    @FXML
    ImageView blueBall, redBall, yellowBall, greenBall, tickBlue, tickRed,tickYellow, tickGreen;

    @FXML
    Text roomID, nickBlue, nickRed, nickYellow, nickGreen;

    @FXML
    void setPlayerReady() throws RemoteException {
        if (!isReady){
            getGuiApplication().getGui().notify(new SetReadyMessage());
            isReady=true;
        }
    }


    @Override
    public void updateModel(UPDATE update){

        ArrayList<Player> players = getGuiApplication().getGui().getMyMatch().getPlayers();
        int i = 0;
        for (Player p : players){
            ballList.get(i).setVisible(true);
            nickList.get(i).setText(p.nickname);
            tickList.get(i).setVisible(p.getReady());
            i++;
        }
        roomID.setText("Room ID: "+getGuiApplication().getGui().getMatchID());

    }


    private HashMap<Integer,ImageView> getBallList(){
        return getPlayerColorImageViewHashMap(blueBall, redBall, yellowBall, greenBall);
    }
    private HashMap<Integer,ImageView> getTickList(){
        return getPlayerColorImageViewHashMap(tickBlue, tickRed, tickYellow, tickGreen);
    }

    private HashMap<Integer, ImageView> getPlayerColorImageViewHashMap(ImageView tickBlue, ImageView tickRed, ImageView tickYellow, ImageView tickGreen) {
        HashMap<Integer,ImageView> tickList = new HashMap<>();
        tickList.put(0, tickBlue);
        tickList.put(1, tickRed);
        tickList.put(2, tickYellow);
        tickList.put(3, tickGreen);
        return tickList;
    }

    private  HashMap<Integer, Text> getNickList(){
        HashMap<Integer, Text>  nickList= new HashMap<>();
        nickList.put(0, nickBlue);
        nickList.put(1,nickRed);
        nickList.put(2, nickYellow);
        nickList.put(3, nickGreen);
        return nickList;
    }
    @Override
    public void ShowErrorMessage(String string){
        ErrorMessage.setText(string);
        ErrorMessage.setFill(Color.RED);
        isReady=false;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
            tickList = getTickList();
            ballList = getBallList();
            nickList = getNickList();
    }
}

