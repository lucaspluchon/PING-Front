package com.app.ping.controller;

import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextInputDialog;

import java.util.Optional;

public class Dialog {
    public static void error(String title, String header, String content)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static String text(String title, String header, String content)
    {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(content);

        Optional<String> result = dialog.showAndWait();
        return result.orElse(null);
    }
}
