package com.app.ping.controller;

import com.app.ping.NodeClass;
import com.app.ping.PingApp;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.fxmisc.richtext.CodeArea;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static com.app.ping.Controller.*;

public class Menu
{
    private static final FileChooser fileChooser = new FileChooser();
    private static final DirectoryChooser directoryChooser = new DirectoryChooser();

    static
    {
        fileChooser.setTitle("Open file");
    }

    public static void show(ContextMenu contextMenu, Button menuButton)
    {
        Bounds position = menuButton.localToScreen(menuButton.getBoundsInLocal());
        contextMenu.show(menuButton.getScene().getWindow(), position.getCenterX(), position.getCenterY());
    }

    public static void openFile(Button menuButton, CodeArea textEditor, TreeView<NodeClass> tree) throws IOException {
        File file = fileChooser.showOpenDialog(menuButton.getScene().getWindow());
        if (file != null)
        {
            PingApp.rootPath = file.toPath();
            TextIde.readFile(textEditor, file);
            Tree.initFile(tree, file.toPath());
        }
    }

    public static void openFolder(Button menuButton, TreeView<NodeClass> tree, CodeArea textIde)
    {
        File file = directoryChooser.showDialog(menuButton.getScene().getWindow());
        if (file != null)
        {
            PingApp.rootPath = file.toPath();
            PingApp.actualPath = null;
            Tree.initFolder(tree, file, textIde);
        }
    }
}

