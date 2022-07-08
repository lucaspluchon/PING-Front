package com.app.ping.controller;

import com.app.ping.NodeClass;
import com.app.ping.PingApp;
import javafx.geometry.Bounds;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.fxmisc.richtext.CodeArea;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import static com.app.ping.controller.TextIde.newFileTab;

public class Menu
{
    private static final FileChooser fileChooser = new FileChooser();

    private static final DirectoryChooser directoryChooser = new DirectoryChooser();

    static
    {
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Prolog files", "*.pl"),
                new FileChooser.ExtensionFilter("Dev files", "*.md", "*.xml", "*.gitignore"));
        fileChooser.setTitle("Open file");
    }



    public static void show(ContextMenu contextMenu, Button menuButton)
    {
        Bounds position = menuButton.localToScreen(menuButton.getBoundsInLocal());
        contextMenu.show(menuButton.getScene().getWindow(), position.getCenterX(), position.getCenterY());
    }

    public static void openFile(Button menuButton, TabPane codeTab) throws IOException, URISyntaxException {
        File file = fileChooser.showOpenDialog(menuButton.getScene().getWindow());
        if (file != null)
        {
            Tab tab = newFileTab(codeTab, file);
            CodeArea textEditor = ((FileInfo) tab.getUserData()).textEditor();
            PingApp.actualFile = file;
            PingApp.actualEditor = textEditor;
            codeTab.getSelectionModel().select(tab);
            TextIde.readFile(textEditor, file);
        }
    }

    public static void openFolder(Button menuButton, TreeView<NodeClass> tree, TabPane codeTab)
    {
        File file = directoryChooser.showDialog(menuButton.getScene().getWindow());
        if (file != null)
        {
            PingApp.projectFolder = file;
            PingApp.actualFile = null;
            codeTab.getTabs().removeAll();
            Tree.initFolder(codeTab, tree, file);
        }
    }

    public static void createFile(TabPane codeTab) throws URISyntaxException, IOException {
       Tab tab = newFileTab(codeTab, null);
       PingApp.actualEditor = ((FileInfo) tab.getUserData()).textEditor();
       PingApp.actualFile = null;
       codeTab.getSelectionModel().select(tab);
    }

    public static void saveFile(Button menuButton, TabPane codeTab) throws IOException
    {
        if (PingApp.actualFile != null)
        {
            TextIde.saveActualFile();
            return;
        }

        File file = directoryChooser.showDialog(menuButton.getScene().getWindow());
        if (file == null)
            return;

        String name = Dialog.text(LanguageSystem.config.getString("fileName"), LanguageSystem.config.getString("enterName"), LanguageSystem.config.getString("fileName"));
        if (name == null)
            return;

        File newFile = new File(file.getAbsolutePath() + "/" + name);
        newFile.createNewFile();
        PingApp.actualFile = newFile;
        PingApp.actualEditor.setUserData(new FileInfo(newFile, PingApp.actualEditor));
        TextIde.saveActualFile();
        codeTab.getSelectionModel().getSelectedItem().setText(name);
        codeTab.getSelectionModel().getSelectedItem().setUserData(new FileInfo(newFile, PingApp.actualEditor));
    }
}

