package com.app.ping.controller;

import com.app.ping.PingApp;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static com.app.ping.Controller.*;

public class Menu
{
    private static final FileChooser fileChooser = new FileChooser();

    static
    {
        fileChooser.setTitle("Open file/project");
    }

    public static void show(ContextMenu contextMenu, Button menuButton)
    {
        Bounds position = menuButton.localToScreen(menuButton.getBoundsInLocal());
        contextMenu.show(menuButton.getScene().getWindow(), position.getCenterX(), position.getCenterY());
    }

    public static void openFile(Button menuButton, TextArea textEditor) throws IOException {
        File file = fileChooser.showOpenDialog(menuButton.getScene().getWindow());
        if (file != null)
        {
            PingApp.rootPath = file.toPath();
            String content = Files.readString(file.toPath());
            textEditor.setText(content);
        }
    }
}

