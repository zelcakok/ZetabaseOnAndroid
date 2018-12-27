package com.zktechproductionhk.zetabaseonandroid.Memory;

import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;

public class DataNode implements Serializable {
    private static String TAG = "[DataNode]";
    protected String path;
    protected String data;
    protected HashMap<String, DataNode> nodes;


    public DataNode(String path) {
        this.path = path;
        this.data = "";
        this.nodes = new HashMap<>();
    }

    public JSONObject toJSON(@Nullable String parent, @Nullable JSONObject jsonObject) {
        try {
            String parentPath = (parent != null ? parent : "");
            jsonObject = jsonObject == null ? new JSONObject() : jsonObject;
            Log.d(TAG, "CUR: " + parentPath + this.path + " => " + this.data);
            Log.d(TAG, "[PARENT]: " + parent);
            Log.d(TAG, "[WRITE]: " + this.path + " => " + this.data);
            jsonObject.put(parent == null ? this.path : parent, this.data);
            for (String child : nodes.keySet()) {
                JSONObject subJson = nodes.get(child).toJSON(parentPath + this.path + (parent != null ? "/" : ""), null);
                Log.d(TAG, "SUB: " + subJson);
                jsonObject.put(child, subJson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            Log.d(TAG, "[RETURN]: " + jsonObject);
            return jsonObject;
        }
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

    public void replaceDataNode(String key, DataNode dataNode) {
        this.nodes.put(key, dataNode);
    }

    public QueryPath traverse(String path, boolean createMissing) {
        DataNode ptr = this, parent = null;
        String[] tokens = tokenize(path);
        if (tokens.length == 1 && tokens[0].charAt(0) == '/') return new QueryPath(parent, ptr);
        for (String token : tokens) {
            if (token.equals("/")) continue;
            DataNode child = ptr.nodes.get(token);
            if (child == null) {
//                Log.d(TAG, token + " does not exist.");
                if (createMissing) {
//                    Log.d(TAG, "Creating " + token);
                    child = new DataNode(token);
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

    public DataNode getChild(String path) {
        return nodes.get(path);
    }

    @Override
    public String toString() {
        return "DataNode{" +
                "path='" + path + '\'' +
                ", data='" + data + '\'' +
                ", nodes=" + nodes +
                '}';
    }
}
