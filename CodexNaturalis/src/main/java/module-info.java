module it.polimi.ingsw {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires java.rmi;
    requires java.desktop;
    requires com.google.common;

    exports it.polimi.ingsw.model;
    exports it.polimi.ingsw.view;
    opens it.polimi.ingsw.model to com.fasterxml.jackson.databind;
    exports it.polimi.ingsw.controller;
    exports it.polimi.ingsw;
    opens it.polimi.ingsw to javafx.fxml;
    exports it.polimi.ingsw.view.Gui;
    opens it.polimi.ingsw.view.Gui to javafx.fxml;
    exports it.polimi.ingsw.view.Gui.SceneControllers;
    opens it.polimi.ingsw.view.Gui.SceneControllers to javafx.fxml;

    exports it.polimi.ingsw.Networking.remoteInterface;
    opens it.polimi.ingsw.Networking.remoteInterface to java.rmi;

}