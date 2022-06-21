package com.app.ping;

import com.app.ping.controller.CodeExecution;
import com.app.ping.controller.Menu;
import com.app.ping.controller.TextIde;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class Controller {
    @FXML public Button menuButton;
    @FXML public ContextMenu contextMenu;
    @FXML public TextArea textEditor;
    @FXML public TextArea consoleResult;
    @FXML public BorderPane window;

    @FXML protected void updateTextIde() throws IOException { TextIde.update(textEditor); }
    @FXML protected void showMenu() { Menu.show(contextMenu, menuButton); }
    @FXML protected void openFile() throws IOException { Menu.openFile(menuButton, textEditor); }
    @FXML protected void executeCode() throws IOException, InterruptedException { CodeExecution.execute(consoleResult, textEditor, window); }

}
