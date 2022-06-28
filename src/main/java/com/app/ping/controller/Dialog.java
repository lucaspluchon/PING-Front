package com.app.ping.controller;

import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;

public class Dialog {
    public static void error(String title, String header, String content)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
