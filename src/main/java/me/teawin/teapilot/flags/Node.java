package me.teawin.teapilot.flags;

import java.util.HashMap;
import java.util.Map;

public class Node {
    private final String name;
    private boolean value;
    private final Map<String, Node> nodes;

    public Node() {
        this(null, false);
    }

    public Node(String name, boolean value) {
        this.name = name;
        this.value = value;
        this.nodes = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public Map<String, Node> getNodes() {
        return nodes;
    }
}

