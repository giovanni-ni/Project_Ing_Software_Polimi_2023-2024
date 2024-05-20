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

    exports it.polimi.ingsw.controller;
    exports it.polimi.ingsw;
    opens it.polimi.ingsw to javafx.fxml;
    exports it.polimi.ingsw.view.Gui;
    opens it.polimi.ingsw.view.Gui to javafx.fxml;

}