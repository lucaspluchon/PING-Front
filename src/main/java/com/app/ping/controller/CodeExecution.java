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

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.app.ping.Controller._codeTab;


public class CodeExecution
{

    private static boolean hasError(String stderr)
    {
        return stderr.contains("ERROR:");
    }

    private static int countError(String stderr, Path path)
    {
        return (stderr.split(path.toAbsolutePath().toString(), -1).length) - 1;
    }

    public static void execute(TextArea consoleResult, TabPane codeTab, BorderPane window, Tab tab) throws IOException, InterruptedException
    {
        if (PingApp.actualFile == null)
        {
            Dialog.error(LanguageSystem.config.getString("error"), LanguageSystem.config.getString("cannotRun"), LanguageSystem.config.getString("noFileOpened"));
            return;
        }
        TextIde.saveActualFile();
        String[] cmd = {"swipl", "-s", PingApp.actualFile.getAbsolutePath(), "-t" ,"halt.", "-q"};
        ProcessBuilder pb = new ProcessBuilder(cmd);
        pb.directory(PingApp.actualFile.getParentFile());
        Process process = pb.start();
        process.waitFor();
        String stdout = new String(process.getInputStream().readAllBytes());
        String stderr = new String(process.getErrorStream().readAllBytes());
        codeTab.getSelectionModel().select(tab);
        Tab tabCode = _codeTab.getSelectionModel().getSelectedItem();

        if (!hasError(stderr))
        {
            consoleResult.setStyle(String.format("-fx-control-inner-background: '%s'; -fx-background-color: '%s'; -fx-text-fill: '%s'", WeatherManager.backColor, WeatherManager.backColor, WeatherManager.textColor));
            consoleResult.setText(stdout);
        }
        else
        {
            consoleResult.setStyle(String.format("-fx-control-inner-background: '%s'; -fx-background-color: '%s'; -fx-text-fill: '%s'", WeatherManager.backColor, WeatherManager.backColor, "red"));
            showError(window, countError(stderr, PingApp.actualFile.toPath()), ((FileInfo) tabCode.getUserData()).textEditor(), stderr.split("\n")[0]);
            consoleResult.setText(stderr);
        }
    }

    private static final Pattern p = Pattern.compile("^(ERROR: [\\w/.-]+:)(\\d+)(.*)");
    private static void showError(BorderPane window, int count, CodeArea textEditor, String line)
    {
        RotateTransition anim = new RotateTransition(Duration.millis(800));
        anim.setNode(window);
        anim.setByAngle(360);
        anim.setCycleCount(50);
        anim.setCycleCount(count);
        anim.play();


        Matcher m = p.matcher(line);
        if (m.find())
        {
            int paragraph = Integer.parseInt(m.group(2)) - 1;
            textEditor.setStyle(paragraph, Collections.singleton("error_linter"));
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() -> {
                        textEditor.setStyle(paragraph, Collections.singleton(""));
                    });

                }
            }, 10 * 1000);
        }
    }
}
