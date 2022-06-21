package com.app.ping.controller;

import com.app.ping.NodeClass;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import org.fxmisc.richtext.StyledTextArea;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

public class Tree {
    public static void initFile(TreeView<NodeClass> tree, Path file)
    {
        tree.setRoot(new TreeItem<>(new NodeClass(NodeType.FOLDER, file)));
    }

    public static void initFolder(TreeView<NodeClass> tree, File folder, StyledTextArea textIde)
    {
        tree.setRoot(new TreeItem<>(new NodeClass(NodeType.FOLDER, folder.toPath())));

        tree.setOnMouseClicked(mouseEvent -> {
            TreeItem<NodeClass> node = tree.getSelectionModel().getSelectedItem();
            if (node.getValue().type == NodeType.FILE)
            {
                try
                {
                    TextIde.readFile(textIde, node.getValue().path.toFile());
                }
                catch (Exception ignored) {}
            }
        });

        tree.getRoot().setExpanded(true);
        populateProject(tree.getRoot(), folder);
    }

    public static void populateProject(TreeItem<NodeClass> root, File folder)
    {
        for (File file : Objects.requireNonNull(folder.listFiles()))
        {
            if (file.isDirectory())
            {
                int index = root.getChildren().size();
                root.getChildren().add(new TreeItem<>(new NodeClass(NodeType.FOLDER, file.toPath())));
                populateProject(root.getChildren().get(index), file);
            }
            else
            {
                TreeItem<NodeClass> node = new TreeItem<>(new NodeClass(NodeType.FILE, file.toPath()));
                root.getChildren().add(node);
            }
        }
    }



}
