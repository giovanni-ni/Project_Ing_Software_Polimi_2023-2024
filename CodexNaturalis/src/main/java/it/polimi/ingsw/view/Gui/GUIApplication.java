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


public class GUIApplication extends Application{
    private  GUI Gui;
    private Stage stage;
    private BiMap<Scene, ScenesName> scenesList;
    private BiMap<GenericSceneController, ScenesName> controllerList;



    @Override
    public void start(Stage primaryStage) throws IOException {
        initGUI();
        loadStage(primaryStage);
        stage.setOnCloseRequest(we -> System.exit(0));
        showScene(ScenesName.START);
        stage.setAlwaysOnTop(true);
        stage.setAlwaysOnTop(false);
    }

    public static void main(String[] args) {
        launch(args);
    }
    private void initGUI(){
        Gui = new GUI(this);

    }
    private void loadStage(Stage primaryStage) throws IOException {
        //icon
        Image icon = new Image("icon.png");
        primaryStage.setTitle("Codex Naturalis");
        primaryStage.getIcons().add(icon);
        primaryStage.setResizable(false);
        // set stage
        stage = primaryStage;

        //all stages loading
        FXMLLoader loader = new FXMLLoader();
        Parent root;
        scenesList = HashBiMap.create();
        controllerList = HashBiMap.create();
        for (ScenesName scenesName : ScenesName.values()){
            loader  = new FXMLLoader(getClass().getResource(scenesName.getPath()));
            root = loader.load();
            ((GenericSceneController)loader.getController()).setGuiApplication(this);
            controllerList.put(loader.getController(),scenesName);
            scenesList.put(new Scene(root, 1280,720), scenesName);

        }

    }

    public void showScene(ScenesName scenesName){

       Scene scene= scenesList.inverse().get(scenesName);
       print("change to scene "+scenesName.toString());
       stage.setScene(scene);
       if(scenesName==ScenesName.START)
           stage.centerOnScreen();
       stage.show();
       updateCurrentSceneModel(UPDATE.GENERAL);


    }
     public ScenesName getActualScene(){
        return  scenesList.get(stage.getScene());
     }
    public GenericSceneController getActualSceneController(){
        ScenesName scenesName =getActualScene();
        return controllerList.inverse().get(scenesName);
    }


    public void showErrorMessage(String description) {
        ScenesName scenesName =getActualScene();
        controllerList.inverse().get(scenesName).ShowErrorMessage(description);
    }

    public void updateCurrentSceneModel(UPDATE update) {
        try {
            controllerList.inverse().get(getActualScene()).updateModel(update);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public GUI getGui() {
        return Gui;
    }


    //

}
