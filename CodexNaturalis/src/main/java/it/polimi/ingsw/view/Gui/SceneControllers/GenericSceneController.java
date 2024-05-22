package it.polimi.ingsw.view.Gui.SceneControllers;

import it.polimi.ingsw.view.Gui.GUIApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class GenericSceneController {
    private boolean goNextScene = false;
    private GUIApplication guiApplication;

    @FXML
    Text ErrorMessage;

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
        ErrorMessage.setText(string);
        ErrorMessage.setFill(Color.RED);
    }

}
