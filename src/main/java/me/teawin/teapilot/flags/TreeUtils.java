package me.teawin.teapilot.flags;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.TreeMap;

public class TreeUtils {
    public static Node resolve(Node root, String path, boolean create) {
        String[] parts = path.split("\\.");

        Node current = root;
        for (String part : parts) {
            if (part.equals("*")) {
                continue;
            }

            Node next = current.getNodes()
                    .get(part);

            if (next == null) {
                if (create) {
                    next = new Node(part, false);
                    current.getNodes()
                            .put(part, next);
                } else {
                    return null;
                }
            }

            current = next;
        }

        return current;
    }

    public static Node resolve(Node root, String path) {
        return resolve(root, path, false);
    }

    public static void create(Node root, String path) {
        resolve(root, path, true);
    }

    public static Iterable<Node> walk(Node root, String path) {
        return new WalkIterator(root, path);
    }

    public static void toggle(Node root, String path, boolean value) {
        Node leaf = resolve(root, path, true);
        if (leaf != null) {
            leaf.setValue(value);
        }
    }

    public static boolean isEnabled(Node root, String path) {
        if (root.getValue()) return true;
        for (Node node : walk(root, path)) {
            if (node == null) return false;
            if (node.getValue()) {
                return true;
            }
        }
        return false;
    }

    private static class WalkIterator implements Iterable<Node>, Iterator<Node> {
        private final String[] parts;
        private Node current;
        private int index;

        public WalkIterator(Node root, String path) {
            this.parts = path.split("\\.");
            this.current = root;
            this.index = 0;
        }

        @Override
        public @NotNull Iterator<Node> iterator() {
            return this;
        }

        @Override
        public boolean hasNext() {
            return index < parts.length;
        }

        @Override
        public Node next() {
            String part = parts[index++];
            if (part.equals("*")) {
                return current;
            }

            Node next = current.getNodes()
                    .get(part);
            if (next == null) {
                return null;
            }
            current = next;
            return next;
        }
    }

}
