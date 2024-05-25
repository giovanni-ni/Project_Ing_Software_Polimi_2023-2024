package it.polimi.ingsw.view.Gui.SceneControllers;

import it.polimi.ingsw.model.ViewModel;
import it.polimi.ingsw.view.Gui.GUI;
import it.polimi.ingsw.view.Gui.GUIApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import javax.swing.text.View;
import java.io.IOException;

public class GenericSceneController {

    private GUIApplication guiApplication;

    @FXML
    Text ErrorMessage;


    public void setGuiApplication(GUIApplication guiApplication) {
        this.guiApplication = guiApplication;
    }

    public GUIApplication getGuiApplication() {
        return guiApplication;
    }
    public void ShowErrorMessage(String string){
        ErrorMessage.setText(string);
        ErrorMessage.setFill(Color.RED);
    }
    public void updateModel() throws IOException {
    }

}
