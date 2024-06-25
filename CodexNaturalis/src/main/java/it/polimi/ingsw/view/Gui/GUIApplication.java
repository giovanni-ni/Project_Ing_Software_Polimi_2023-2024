package it.polimi.ingsw.view.Gui;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import it.polimi.ingsw.Message.Message;
import it.polimi.ingsw.model.ViewModel;
import it.polimi.ingsw.view.Gui.SceneControllers.GenericSceneController;
import it.polimi.ingsw.view.Gui.SceneControllers.UPDATE;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.concurrent.BlockingQueue;

import static it.polimi.ingsw.view.TextualInterfaceUnit.Print.print;

/**
 * The main application class for the graphical user interface.
 * Manages scenes, controllers, and interactions within the GUI.
 */
public class GUIApplication extends Application {
    private GUI Gui;
    private Stage stage;
    private BiMap<Scene, ScenesName> scenesList;
    private BiMap<GenericSceneController, ScenesName> controllerList;
    private Stage pStage;

    /**
     * Entry point of the JavaFX application.
     * Initializes the GUI, loads the primary stage, and shows the initial scene.
     *
     * @param primaryStage The primary stage of the application.
     * @throws IOException If an error occurs during stage loading.
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        initGUI();
        pStage = primaryStage;
        loadStage(primaryStage);
        stage.setOnCloseRequest(we -> System.exit(0)); // Ensure application closes properly
        showScene(ScenesName.START); // Show the initial scene
        stage.setAlwaysOnTop(true); // Keep the stage always on top
        stage.setAlwaysOnTop(false); // Allow the stage to be not always on top
    }

    /**
     * Main method to launch the application.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Initializes the GUI instance associated with this application.
     */
    private void initGUI() {
        Gui = new GUI(this);
    }

    /**
     * Initializes the GUI instance to get ready to another game.
     */
    public void initializeAfter() throws IOException {
        getGui().initializeForNewGame();
        loadStage(pStage);
    }
    /**
     * Loads the primary stage of the application with scenes defined in ScenesName enum.
     *
     * @param primaryStage The primary stage of the application.
     * @throws IOException If an error occurs during stage loading.
     */
    private void loadStage(Stage primaryStage) throws IOException {
        // Load icon
        Image icon = new Image("icon.png");
        primaryStage.setTitle("Codex Naturalis");
        primaryStage.getIcons().add(icon);
        primaryStage.setResizable(false);

        // Set primary stage
        stage = primaryStage;

        // Load all scenes defined in ScenesName enum using FXMLLoader
        FXMLLoader loader = new FXMLLoader();
        Parent root;
        scenesList = HashBiMap.create();
        controllerList = HashBiMap.create();
        for (ScenesName scenesName : ScenesName.values()) {
            loader = new FXMLLoader(getClass().getResource(scenesName.getPath()));
            root = loader.load();
            ((GenericSceneController) loader.getController()).setGuiApplication(this);
            controllerList.put(loader.getController(), scenesName);
            scenesList.put(new Scene(root, 1280, 720), scenesName);
        }
    }

    /**
     * Shows the specified scene in the primary stage.
     *
     * @param scenesName The name of the scene to show.
     */
    public void showScene(ScenesName scenesName) {
        Scene scene = scenesList.inverse().get(scenesName);
        print("change to scene " + scenesName.toString());
        stage.setScene(scene);
        if (scenesName == ScenesName.START)
            stage.centerOnScreen(); // Center the stage on screen for START scene
        stage.show();
        updateCurrentSceneModel(UPDATE.GENERAL); // Update the model for the current scene
    }

    /**
     * Retrieves the current scene name from the primary stage.
     *
     * @return The current scene name.
     */
    public ScenesName getActualScene() {
        return scenesList.get(stage.getScene());
    }

    /**
     * Retrieves the controller associated with the current scene.
     *
     * @return The GenericSceneController instance for the current scene.
     */
    public GenericSceneController getActualSceneController() {
        ScenesName scenesName = getActualScene();
        return controllerList.inverse().get(scenesName);
    }

    /**
     * Displays an error message in the current scene.
     *
     * @param description The error message description to display.
     */
    public void showErrorMessage(String description) {
        ScenesName scenesName = getActualScene();
        controllerList.inverse().get(scenesName).ShowErrorMessage(description);
    }

    /**
     * Updates the model of the current scene based on the specified update type.
     *
     * @param update The type of update to apply to the scene's model.
     */
    public void updateCurrentSceneModel(UPDATE update) {
        try {
            controllerList.inverse().get(getActualScene()).updateModel(update);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves the GUI instance associated with this application.
     *
     * @return The GUI instance.
     */
    public GUI getGui() {
        return Gui;
    }
}
