package com.app.ping;

import com.app.ping.controller.CodeExecution;
import com.app.ping.controller.Menu;
import com.app.ping.controller.TextIde;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import org.fxmisc.richtext.StyledTextArea;

import java.io.IOException;

public class Controller {
    @FXML public Button menuButton;
    @FXML public ContextMenu contextMenu;
    @FXML public StyledTextArea textEditor;

    @FXML public TextArea consoleResult;
    @FXML public BorderPane window;
    @FXML public TreeView<NodeClass> projectTree;
    
    @FXML protected void updateTextIde() throws IOException { TextIde.update(textEditor); }
    @FXML protected void showMenu() { Menu.show(contextMenu, menuButton); }
    @FXML protected void openFile() throws IOException { Menu.openFile(menuButton, textEditor, projectTree); }
    @FXML protected void openFolder() throws IOException { Menu.openFolder(menuButton, projectTree, textEditor); }
    @FXML protected void executeCode() throws IOException, InterruptedException { CodeExecution.execute(consoleResult, textEditor, window); }

}
