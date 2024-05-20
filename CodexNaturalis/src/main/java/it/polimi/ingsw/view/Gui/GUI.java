package it.polimi.ingsw.view.Gui;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.IOException;


public class GUI extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {

        Image icon = new Image("icon.png");

        // Set the title of the stage
        Parent root = FXMLLoader.load((getClass().getResource("AskNickname.fxml")));
        primaryStage.setTitle("Codex Naturalis");
        root.setStyle("-fx-background-color: black;");
        primaryStage.getIcons().add(icon);
        // Set the scene with the loaded FXML
        primaryStage.setScene(new Scene(root,1280,720));

        primaryStage.setResizable(false);
        // Show the stage
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
