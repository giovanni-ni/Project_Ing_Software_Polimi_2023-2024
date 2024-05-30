package it.polimi.ingsw.view.Gui.SceneControllers;

import it.polimi.ingsw.Message.ClientToServerMsg.CreateGameMessage;
import it.polimi.ingsw.Message.ClientToServerMsg.JoinFirstMessage;
import it.polimi.ingsw.Message.ClientToServerMsg.JoinGameMessage;
import it.polimi.ingsw.view.Gui.ScenesName;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import org.w3c.dom.Text;

import java.rmi.RemoteException;
import java.util.Objects;

public class MenuController extends GenericSceneController{

    private boolean isCreateGame =false;
    private boolean isJoinGame = false;
    private int numOfError=0;
    @FXML
    TextField gameId;

    @FXML
    TextField nickName;

    @FXML
    void sendNickName(ActionEvent event) throws RemoteException {
        if (Objects.equals(nickName.getText(), "")){
            nickName.setText("nuge"+numOfError);
        }
        if (isCreateGame){
            getGuiApplication().getGui().notify(new CreateGameMessage(nickName.getText()));
        }else {
            getGuiApplication().getGui().notify(new CreateGameMessage(nickName.getText())); //ToDO if we do join first method
        }
        ShowJoiningMessage();

    }
    @FXML
    void sendAll(ActionEvent event) throws RemoteException {
        if (gameId.getText().matches("\\d+")){
            if (Objects.equals(nickName.getText(), "")){
                nickName.setText("nuge"+numOfError);
            }
            if (isJoinGame){
                getGuiApplication().getGui().notify(new JoinGameMessage(nickName.getText(), Integer.parseInt(gameId.getText())));
            }else {
                getGuiApplication().getGui().notify(new JoinGameMessage(nickName.getText(),Integer.parseInt(gameId.getText())));
                //todo reconnect if we do that
            }
            ShowJoiningMessage();

        }else {
            ShowErrorMessage("Game Id should be a number");
        }

    }

    @FXML
    void goCreateGame(ActionEvent event) {
        getGuiApplication().showScene(ScenesName.ASKNICKNAME);
        isCreateGame = true;
    }
    @FXML
    void goJoinGame(ActionEvent event) {
        getGuiApplication().showScene(ScenesName.EASYJOIN);
        isJoinGame = true;
    }
    @FXML
    void goReconnect(ActionEvent event) {
        getGuiApplication().showScene(ScenesName.EASYJOIN);
        isJoinGame = false;
    }
    @FXML
    void goJoinFirst(ActionEvent event) {
        getGuiApplication().showScene(ScenesName.ASKNICKNAME);
        isCreateGame= false;
    }

    @Override
    public void ShowErrorMessage(String string){
        ErrorMessage.setText(string);
        ErrorMessage.setFill(Color.RED);
        numOfError++;
        nickName.clear();
    }
    public void ShowJoiningMessage(){
        ErrorMessage.setText("Joining");
        ErrorMessage.setFill(Color.GREEN);
    }
}
