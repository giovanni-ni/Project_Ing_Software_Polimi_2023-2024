package it.polimi.ingsw.view.Gui.SceneControllers;

import it.polimi.ingsw.Message.ClientToServerMsg.CreateGameMessage;
import it.polimi.ingsw.Message.ClientToServerMsg.JoinFirstMessage;
import it.polimi.ingsw.Message.ClientToServerMsg.JoinGameMessage;
import it.polimi.ingsw.view.Gui.ScenesName;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.w3c.dom.Text;

import java.rmi.RemoteException;

public class MenuController extends GenericSceneController{

    private boolean isCreateGame =false;
    private boolean isJoinGame = false;
    @FXML
    TextField gameId;

    @FXML
    TextField nickName;

    @FXML
    void sendNickName(ActionEvent event) throws RemoteException {
        if (isCreateGame){
            getGuiApplication().notify(new CreateGameMessage(nickName.getText()));
        }else {
            getGuiApplication().notify(new CreateGameMessage(nickName.getText())); //ToDO if we do join first method
        }

    }
    @FXML
    void sendAll(ActionEvent event) throws RemoteException {
        if (isJoinGame){
            getGuiApplication().notify(new JoinGameMessage(nickName.getText(), Integer.parseInt(gameId.getText())));
        }else {
            getGuiApplication().notify(new JoinGameMessage(nickName.getText(),Integer.parseInt(gameId.getText())));
            //todo reconnect if we do that
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
}
