package me.teawin.teapilot;

import me.teawin.teapilot.flags.Node;
import me.teawin.teapilot.flags.TreeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FlagsManager {
    public static final Node root = new Node();

    public void set(String name, Boolean state) {
        TreeUtils.create(root, name);
        if (state) {
            enable(name);
        } else {
            disable(name);
        }
    }

    public void toggle(String name, Boolean value) {
        Node node = TreeUtils.resolve(root, name);
        if (node == null) return;
        node.setValue(value);
    }

    public void enable(String name) {
        TreeUtils.toggle(root, name, true);
    }

    public void disable(String name) {
        TreeUtils.toggle(root, name, false);
    }

    public boolean isEnabled(String name) {
        return TreeUtils.isEnabled(root, name);
    }

    public boolean isDisabled(String name) {
        return !isEnabled(name);
    }

    public List<String> getAllNames() {
        List<String> names = new ArrayList<>();
        collectNames(root, "", names);
        return names;
    }

    private void collectNames(Node node, String prefix, List<String> names) {
        if (node.getName() != null) {
            String name = (prefix == null || prefix.isEmpty()) ? node.getName() : prefix + "." + node.getName();
            names.add(name);
        }
        for (Node child : node.getNodes().values()) {
            collectNames(child, (prefix == null || prefix.isEmpty()) ? node.getName() : prefix + "." + node.getName(), names);
        }
    }
}
