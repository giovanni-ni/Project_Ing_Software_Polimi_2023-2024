package it.polimi.ingsw.view.Gui;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import it.polimi.ingsw.view.Gui.SceneControllers.GenericSceneController;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.rmi.NotBoundException;

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
            controllerList.put(loader.getController(),scenesName);
            ((GenericSceneController)loader.getController()).setGuiApplication(this);
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


    }
    public void Connection(Boolean isRMI, String ip, String port) throws IOException, NotBoundException {
        Gui.connect(isRMI,ip,port);
    }
     public ScenesName getActualScene(){
        return  scenesList.get(stage.getScene());
     }


    public void showErrorMessage(String description) {
        ScenesName scenesName =getActualScene();
        controllerList.inverse().get(scenesName).ShowErrorMessage(description);

    }
}