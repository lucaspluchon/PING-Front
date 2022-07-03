package com.app.ping;

import com.app.ping.controller.CodeExecution;
import com.app.ping.controller.FileInfo;
import com.app.ping.controller.Menu;
import com.app.ping.controller.TextIde;
import com.kodedu.terminalfx.TerminalBuilder;
import com.kodedu.terminalfx.TerminalTab;
import com.kodedu.terminalfx.config.TerminalConfig;
import javafx.concurrent.Task;
import com.app.ping.weather.WeatherManager;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import org.fxmisc.richtext.CodeArea;


import java.io.IOException;


public class Controller {
    @FXML public Button menuButton;
    @FXML public ContextMenu contextMenu;
    @FXML public TextArea consoleResult;
    @FXML public Tab consoleResultTab;
    @FXML public BorderPane window;
    @FXML public TreeView<NodeClass> projectTree;

    @FXML public TabPane codeTab;

    @FXML public TabPane resultTab;

    public void initialize()
    {
        //WeatherManager.setTimer(textEditor, consoleResult, projectTree, contextMenu);

        TerminalConfig darkConfig = new TerminalConfig();
        darkConfig.setBackgroundColor(Color.rgb(16, 16, 16));
        darkConfig.setForegroundColor(Color.rgb(248, 194, 145));
        darkConfig.setCursorColor(Color.rgb(248, 194, 145, 0.5));


        TerminalBuilder terminalBuilder = new TerminalBuilder(darkConfig);
        TerminalTab terminal = terminalBuilder.newTerminal();
        terminal.setText("Terminal");


        resultTab.getTabs().add(terminal);
        resultTab.getSelectionModel().select(terminal);
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
        TextIde.saveActualFile();
        FileInfo info = (FileInfo) codeTab.getSelectionModel().getSelectedItem().getUserData();
        PingApp.actualFile = info.file();
        PingApp.actualEditor = info.textEditor();
    }




}
