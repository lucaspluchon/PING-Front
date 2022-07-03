package com.app.ping.controller;

import com.app.ping.NodeClass;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import org.fxmisc.richtext.CodeArea;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Objects;

public class Tree {

    public static ArrayList<TreeItem<NodeClass>> listItem = new ArrayList<>();
    public static void initFile(TreeView<NodeClass> tree, Path file)
    {
        TreeItem<NodeClass> elm = new TreeItem<>(new NodeClass(NodeType.FILE, file));
        tree.setRoot(elm);
        listItem.add(elm);
    }

    public static void initFolder(TabPane codeTab, TreeView<NodeClass> tree, File folder)
    {
        TreeItem<NodeClass> elm = new TreeItem<>(new NodeClass(NodeType.FOLDER, folder.toPath()));
        tree.setRoot(elm);
        listItem.add(elm);

        tree.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() != 2)
                return;
            TreeItem<NodeClass> node = tree.getSelectionModel().getSelectedItem();
            if (node != null && node.getValue().type == NodeType.FILE)
            {
                try
                {
                    Tab tab = TextIde.newFileTab(codeTab, node.getValue().path.toFile());
                    CodeArea textEditor = ((FileInfo) tab.getUserData()).textEditor();
                    TextIde.readFile(textEditor, node.getValue().path.toFile());
                    codeTab.getSelectionModel().select(tab);

                }
                catch (Exception ignored) {}
            }
        });

        tree.getRoot().setExpanded(true);
        populateProject(tree.getRoot(), folder);
    }

    private static boolean isCorrectFile(String name)
    {
        String[] authorizedExtension = {".pl", ".xml", ".md", ".gitignore"};
        for (String ext : authorizedExtension)
        {
            if (name.contains(ext))
                return true;
        }
        return false;
    }

    public static void populateProject(TreeItem<NodeClass> root, File folder)
    {
        for (File file : Objects.requireNonNull(folder.listFiles()))
        {
            if (file.isDirectory())
            {
                int index = root.getChildren().size();
                TreeItem<NodeClass> elm = new TreeItem<>(new NodeClass(NodeType.FOLDER, file.toPath()));
                root.getChildren().add(elm);
                listItem.add(elm);
                populateProject(root.getChildren().get(index), file);
            }
            else
            {
                if (isCorrectFile(file.getName()))
                {
                    TreeItem<NodeClass> elm = new TreeItem<>(new NodeClass(NodeType.FILE, file.toPath()));
                    listItem.add(elm);
                    root.getChildren().add(elm);
                }
            }
        }
    }
}
