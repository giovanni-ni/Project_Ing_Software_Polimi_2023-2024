package it.polimi.ingsw.view.Gui;

import javafx.fxml.FXML;

import java.awt.event.ActionEvent;

public class GuiController {
    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("Button clicked!");
    }
}
