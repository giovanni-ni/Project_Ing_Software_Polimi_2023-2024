package it.polimi.ingsw.view.Gui.SceneControllers;

import it.polimi.ingsw.view.Gui.GUIApplication;
import javafx.fxml.FXML;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import java.io.IOException;

/**
 * Base controller class for JavaFX scenes in the GUI application.
 * Provides common functionality such as error message display and model updating.
 */
public class GenericSceneController {

    private GUIApplication guiApplication;

    @FXML
    Text ErrorMessage;

    /**
     * Sets the GUI application instance associated with this controller.
     *
     * @param guiApplication The GUI application instance.
     */
    public void setGuiApplication(GUIApplication guiApplication) {
        this.guiApplication = guiApplication;
    }

    /**
     * Retrieves the GUI application instance associated with this controller.
     *
     * @return The GUI application instance.
     */
    public GUIApplication getGuiApplication() {
        return guiApplication;
    }

    /**
     * Displays an error message on the UI.
     *
     * @param string The error message to display.
     */
    public void ShowErrorMessage(String string){
        ErrorMessage.setText(string);
        ErrorMessage.setFill(Color.RED);
    }

    /**
     * Placeholder method for updating the model based on server updates.
     * Implementations of this method in subclasses should handle model updates.
     *
     * @param update The update object containing new model data.
     * @throws IOException If there's an error in IO operations during model update.
     */
    public void updateModel(UPDATE update) throws IOException {
        // Placeholder method for updating the model based on server updates
    }
}
