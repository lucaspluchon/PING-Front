package com.app.ping;

import com.app.ping.controller.NodeType;

import java.nio.file.Path;

public class NodeClass {


    public NodeType type;
    public Path path;

    public NodeClass(NodeType type, Path path)
    {
        this.type = type;
        this.path = path;
    }

    @Override
    public String toString()
    {
        return this.path.toFile().getName();
    }

}
