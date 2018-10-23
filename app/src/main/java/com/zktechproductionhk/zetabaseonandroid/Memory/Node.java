package com.zktechproductionhk.zetabaseonandroid.Memory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;

public class Node implements Serializable {
    private static String TAG = "[Node]";
    protected String path;
    protected String data;
    protected HashMap<String, Node> nodes;


    public Node(String path) {
        this.path = path;
        this.data = "";
        this.nodes = new HashMap<>();
    }

    public String getPath() {
        return path;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public QueryPath traverse(String path, boolean createMissing) {
        Node ptr = this, parent = null;
        String[] tokens = tokenize(path);
        if (tokens.length == 1 && tokens[0].charAt(0) == '/') return new QueryPath(parent, ptr);
        for (String token : tokens) {
            if (token.equals("/")) continue;
            Node child = ptr.nodes.get(token);
            if (child == null) {
//                Log.d(TAG, token + " does not exist.");
                if (createMissing) {
//                    Log.d(TAG, "Creating " + token);
                    child = new Node(token);
                    ptr.nodes.put(token, child);
//                    Log.d(TAG, token + " is created");
                }
            }
            parent = ptr;
            ptr = child;
        }
        return new QueryPath(parent, ptr);
    }

    public static String[] tokenize(String path) {
        String[] tokens = path.split("/");
        if (tokens.length == 0) return new String[]{"/"};
        if (path.charAt(0) == '/') tokens[0] = "/";
        return tokens;
    }

    public Iterator<String> getChildrenPath() {
        return nodes.keySet().iterator();
    }

    public Node getChild(String path) {
        return nodes.get(path);
    }

    @Override
    public String toString() {
        return "Node{" +
                "path='" + path + '\'' +
                ", data='" + data + '\'' +
                ", nodes=" + nodes +
                '}';
    }
}
