package me.teawin.teapilot.flags;

import java.util.*;

public class NodeProcessor {
    public static Map<String, Boolean> getKeyValuePairs(Node root) {
        Map<String, Boolean> result = new HashMap<>();
        Stack<NodePath> stack = new Stack<>();

        for (Node node : root.getNodes()
                .values()) {
            stack.push(new NodePath(node, node.getName()));
        }

        while (!stack.isEmpty()) {
            NodePath element = stack.pop();

            Node node = element.node();
            String path = element.path();

            if (node.getNodes()
                    .isEmpty()) {
                result.put(path, node.getValue());
            }
            if (!node.getNodes()
                    .isEmpty() && node.getValue()) {
                result.put(path + ".*", true);
            }

            for (Map.Entry<String, Node> entry : node.getNodes()
                    .entrySet()) {
                stack.push(new NodePath(entry.getValue(), path + "." + entry.getKey()));
            }
        }

        return result;
    }

    private record NodePath(Node node, String path) {
    }
}