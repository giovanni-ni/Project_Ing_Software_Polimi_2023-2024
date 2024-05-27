package it.polimi.ingsw.view.Gui.SceneControllers;

import it.polimi.ingsw.Message.ClientToServerMsg.SetReadyMessage;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayerColor;
import it.polimi.ingsw.view.Gui.GUIApplication;
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

import static it.polimi.ingsw.view.TextualInterfaceUnit.Print.print;

public class WaitingController extends GenericSceneController implements Initializable {

    private  HashMap<PlayerColor,ImageView> ballList;
    private  HashMap<PlayerColor,ImageView> tickList;
    private  HashMap<PlayerColor, Text> nickList;
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
    public void updateModel(){

        ArrayList<Player> players = getGuiApplication().getGui().getMyMatch().getPlayers();
        for (Player p : players){
            ballList.get(p.getPlayerID()).setVisible(true);
            nickList.get(p.getPlayerID()).setText(p.nickname);
            tickList.get(p.getPlayerID()).setVisible(p.getReady());
        }
        roomID.setText("Room ID: "+getGuiApplication().getGui().getMatchID());

    }


    private HashMap<PlayerColor,ImageView> getBallList(){
        return getPlayerColorImageViewHashMap(blueBall, redBall, yellowBall, greenBall);
    }
    private HashMap<PlayerColor,ImageView> getTickList(){
        return getPlayerColorImageViewHashMap(tickBlue, tickRed, tickYellow, tickGreen);
    }

    private HashMap<PlayerColor, ImageView> getPlayerColorImageViewHashMap(ImageView tickBlue, ImageView tickRed, ImageView tickYellow, ImageView tickGreen) {
        HashMap<PlayerColor,ImageView> tickList = new HashMap<>();
        tickList.put(PlayerColor.BLUE, tickBlue);
        tickList.put(PlayerColor.RED, tickRed);
        tickList.put(PlayerColor.YELLOW, tickYellow);
        tickList.put(PlayerColor.GREEN, tickGreen);
        return tickList;
    }

    private  HashMap<PlayerColor, Text> getNickList(){
        HashMap<PlayerColor, Text>  nickList= new HashMap<>();
        nickList.put(PlayerColor.BLUE, nickBlue);
        nickList.put(PlayerColor.RED,nickRed);
        nickList.put(PlayerColor.YELLOW, nickYellow);
        nickList.put(PlayerColor.GREEN, nickGreen);
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

