package it.polimi.ingsw.view.Gui.SceneControllers;

import it.polimi.ingsw.view.Gui.GUIApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class GenericSceneController {
    private GUIApplication guiApplication;

    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("Button clicked!");
    }


    public void setGuiApplication(GUIApplication guiApplication) {
        this.guiApplication = guiApplication;
    }

    public GUIApplication getGuiApplication() {
        return guiApplication;
    }
    public void ShowErrorMessage(String string){

    }
}
