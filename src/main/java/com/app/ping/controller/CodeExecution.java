package com.app.ping.controller;

import com.app.ping.PingApp;
import com.app.ping.weather.WeatherManager;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;
import org.fxmisc.richtext.CodeArea;
import org.projog.api.Projog;
import org.projog.core.ProjogException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.app.ping.Controller._codeTab;


public class CodeExecution
{

    private static final Pattern querry = Pattern.compile("(- initialization\\()(\\w+)(\\))\\.");
    private static Result interpret(File file) throws IOException {
        Projog prolog = new Projog();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        prolog.setUserOutput(ps);
        String fileContent = Files.readString(file.toPath());
        List<String> fileLines = Files.readAllLines(file.toPath());

        try
        {
            prolog.consultFile(file);
        }
        catch (ProjogException e)
        {
            int lineIndex = 0;
            for (int i = 0; i < fileLines.size(); i++)
            {
                if (fileLines.get(i).contains(e.getLocalizedMessage().substring(e.getLocalizedMessage().lastIndexOf(":") + 2)))
                    lineIndex = i;
            }
            return new Result(e.getLocalizedMessage(), false, lineIndex);
        }

        Matcher m = querry.matcher(fileContent);
        if (m.find())
        {
            String query = m.group(2) + ".";
            try
            {
                prolog.executeOnce(query);
            }
            catch (ProjogException e)
            {
                int lineIndex = 0;
                for (int i = 0; i < fileLines.size(); i++)
                {
                    if (fileLines.get(i).contains(e.getLocalizedMessage().substring(e.getLocalizedMessage().lastIndexOf(":") + 2)))
                        lineIndex = i;
                }
                return new Result(e.getLocalizedMessage(), false, lineIndex);
            }
            return new Result(baos.toString(), true, 0);
        }
        return new Result("No initialization close found", false, 0);
    }

    public static void execute(TextArea consoleResult, TabPane codeTab, BorderPane window, Tab tab) throws IOException, InterruptedException
    {
        if (PingApp.actualFile == null)
        {
            Dialog.error(LanguageSystem.config.getString("error"), LanguageSystem.config.getString("cannotRun"), LanguageSystem.config.getString("noFileOpened"));
            return;
        }
        TextIde.saveActualFile();


        codeTab.getSelectionModel().select(tab);
        Tab tabCode = _codeTab.getSelectionModel().getSelectedItem();
        Result result = interpret(PingApp.actualFile);

        if (result.ok())
        {
            consoleResult.setStyle(String.format("-fx-text-fill: '%s'", "black"));
            consoleResult.setText(result.out());
        }
        else
        {
            consoleResult.setStyle(String.format("-fx-text-fill: '%s'", "red"));
            showError(window, ((FileInfo) tabCode.getUserData()).textEditor(), result.lineError());
            consoleResult.setText(result.out());
        }
    }

    private static void showError(BorderPane window, CodeArea textEditor, int lineError)
    {
        RotateTransition anim = new RotateTransition(Duration.millis(800));
        anim.setNode(window);
        anim.setByAngle(360);
        anim.setCycleCount(50);
        anim.setCycleCount(1);
        anim.play();


        textEditor.setStyle(lineError, Collections.singleton("error_linter"));
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    textEditor.setStyle(lineError, Collections.singleton(""));
                });
            }
        }, 10 * 1000);
    }
}
