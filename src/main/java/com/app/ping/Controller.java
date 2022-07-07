package com.app.ping;

import com.app.ping.controller.*;
import com.app.ping.controller.Menu;
import com.kodedu.terminalfx.TerminalBuilder;
import com.kodedu.terminalfx.TerminalTab;
import com.kodedu.terminalfx.config.TerminalConfig;
import javafx.concurrent.Task;
import com.app.ping.weather.WeatherManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.fxmisc.richtext.CodeArea;


import java.io.IOException;


public class Controller {
    @FXML public Button menuButton;
    @FXML public ContextMenu contextMenu;
    @FXML public static ContextMenu _contextMenu;
    @FXML public TextArea consoleResult;
    public static TextArea _consoleResult;
    @FXML public Tab consoleResultTab;
    @FXML public BorderPane window;
    @FXML public TreeView<NodeClass> projectTree;
    public static TreeView<NodeClass> _projectTree;


    @FXML public MenuItem settingsButton;

    @FXML public TabPane codeTab;
    public static TabPane _codeTab;

    @FXML public TabPane resultTab;

    public static Stage settingStage = null;

    public void initialize() throws IOException

    {
        _contextMenu = contextMenu;
        _projectTree = projectTree;
        _consoleResult = consoleResult;
        _codeTab = codeTab;

        //WeatherManager.startTimer();

        TerminalConfig darkConfig = new TerminalConfig();
        darkConfig.setBackgroundColor(Color.rgb(16, 16, 16));
        darkConfig.setForegroundColor(Color.rgb(248, 194, 145));
        darkConfig.setCursorColor(Color.rgb(248, 194, 145, 0.5));


        TerminalBuilder terminalBuilder = new TerminalBuilder(darkConfig);
        TerminalTab terminal = terminalBuilder.newTerminal();
        terminal.setText("Terminal");

        resultTab.getTabs().add(terminal);
        resultTab.getSelectionModel().select(terminal);
        WeatherManager.startTimer();

    }

    @FXML protected void showMenu() { Menu.show(contextMenu, menuButton); }
    @FXML protected void openFile() throws IOException {
        Menu.openFile(menuButton, codeTab);
    }
    @FXML protected void openFolder() throws IOException {
        Menu.openFolder(menuButton, projectTree, codeTab);
    }
    @FXML protected void executeCode() throws IOException, InterruptedException {
        CodeExecution.execute(consoleResult, resultTab, window, consoleResultTab);
    }
    @FXML protected void createFile() throws IOException {
        Menu.createFile(codeTab);
    }
    @FXML protected void saveFile() throws IOException {
        Menu.saveFile(menuButton, codeTab);
    }

    @FXML protected void updateIde() throws IOException {
        if (codeTab.getSelectionModel().getSelectedItem() != null)
        {
            TextIde.saveActualFile();
            FileInfo info = (FileInfo) codeTab.getSelectionModel().getSelectedItem().getUserData();
            PingApp.actualFile = info.file();
            PingApp.actualEditor = info.textEditor();
        }
    }

    @FXML protected void openSettings()
    {
        FXMLLoader fxmlLoader = new FXMLLoader(PingApp.class.getResource("settings.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 510, 209);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("pute");
        Stage stage = new Stage();
        stage.setTitle(LanguageSystem.config.getString("settings"));
        stage.setScene(scene);
        stage.show();
    }

    @FXML protected void openDoc()
    {
        FXMLLoader fxmlLoader = new FXMLLoader(PingApp.class.getResource("doc.fxml"));
        Scene scene;
        try {
            scene = new Scene(fxmlLoader.load(), 640, 480);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Stage stage = new Stage();
        stage.setTitle(LanguageSystem.config.getString("documentation"));
        stage.setScene(scene);
        stage.show();
    }


    @FXML protected void treeCreateFile() throws IOException {
        Tree.createFile();
    }

    @FXML protected void treeDeleteFile() throws IOException {
        Tree.deleteFile();
    }

    @FXML protected void treeRenameFile() throws IOException {
        Tree.renameFile();
    }



}
