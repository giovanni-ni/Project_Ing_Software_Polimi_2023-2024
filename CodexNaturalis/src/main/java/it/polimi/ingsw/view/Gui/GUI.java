package it.polimi.ingsw.view.Gui;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

public class GUI extends Application {
    @Override
    public void start(Stage primaryStage){

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
        loader.setRoot(new AnchorPane());
        Parent root = loader.getRoot();
        // Set the title of the stage
        primaryStage.setTitle("FXML Example");

        // Set the scene with the loaded FXML
        primaryStage.setScene(new Scene(root));

        // Show the stage
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
