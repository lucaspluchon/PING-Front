package com.app.ping;

import com.app.ping.controller.LanguageSystem;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class Documentation {
    @FXML
    public TextArea text;

    @FXML public void initialize()
    {
        text.setText(LanguageSystem.config.getString("docContent"));
    }
}
