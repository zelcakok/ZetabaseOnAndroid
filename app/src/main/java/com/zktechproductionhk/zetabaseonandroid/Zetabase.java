package com.zktechproductionhk.zetabaseonandroid;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.cedarsoftware.util.io.JsonReader;
import com.cedarsoftware.util.io.JsonWriter;
import com.zktechproductionhk.zetabaseonandroid.InternalStorage.IO;
import com.zktechproductionhk.zetabaseonandroid.Memory.DataNode;
import com.zktechproductionhk.zetabaseonandroid.Memory.Storage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Zetabase {

    public static Zetabase instnace = null;

    private static String TAG = "[Zetabase]";
    private IO database;
    private Storage ram;

    private Zetabase(Context context) {
        this.database = new IO(context, "db.zetabase");
        init();
    }

    public static Zetabase getInstnace(Context context) {
        if (instnace == null) instnace = new Zetabase(context);
        return instnace;
    }

    public String toJSON() {
        Map args = new HashMap<String, Boolean>() {{
            put(JsonWriter.TYPE, false);
        }};
        DataNode node = this.ram.read("/");
        return JsonWriter.objectToJson(node, args);
    }

    public boolean containsKey(String path) {
        try {
            path = preparePath(path);
            return ram.read(path) != null;
        } catch (Exception e) {
            return false;
        }
    }

    public void write(String path, Object data, Storage.OnChangeListner listner) {
        try {
            path = preparePath(path);
            String json = JsonWriter.objectToJson(data);
            this.ram.write(path, json, listner);
        } catch (Exception e) {
            Log.e(TAG, "Error: " + e.getMessage());
        }
    }

    public void write(String path, Object data) {
        try {
            path = preparePath(path);
            String json;
            if (!data.getClass().getSimpleName().equals("String"))
                json = JsonWriter.objectToJson(data);
            else json = data.toString();
            this.ram.write(path, json);
        } catch (Exception e) {
            Log.e(TAG, "Error: " + e.getMessage());
        }
    }

    public void append(String path, Object data, Storage.OnChangeListner listner) {
        try {
            path = preparePath(path);
            String json = JsonWriter.objectToJson(data);
            this.ram.append(path, json, listner);
        } catch (Exception e) {
            Log.e(TAG, "Error: " + e.getMessage());
        }
    }

    public void append(String path, Object data) {
        try {
            path = preparePath(path);
            String json = JsonWriter.objectToJson(data);
            this.ram.append(path, json);
        } catch (Exception e) {
            Log.e(TAG, "Error: " + e.getMessage());
        }
    }

    public Object read(String path) {
        try {
            path = preparePath(path);
            String json = this.ram.read(path).getData();
            return JsonReader.jsonToJava(json);
        } catch (Exception e) {
            Log.e(TAG, "Error: " + e.getMessage());
            return null;
        }
    }

    public void wipe(String path, Storage.OnChangeListner listner) {
        try {
            path = preparePath(path);
            if (path.equals("/")) this.ram.purge(listner);
            this.ram.wipe(path, listner);
        } catch (Exception e) {
            Log.e(TAG, "Error: " + e.getMessage());
        }
    }

    public void wipe(String path) {
        try {
            path = preparePath(path);
            if (path.equals("/")) this.ram.purge();
            else this.ram.wipe(path);
        } catch (Exception e) {
            Log.e(TAG, "Error: " + e.getMessage());
        }
    }

    public void monitor(String path, Storage.Monitor monitor) {
        try {
            path = preparePath(path);
            this.ram.monitor(path, monitor);
        } catch (Exception e) {
            Log.e(TAG, "Error: " + e.getMessage());
        }
    }

    public DataNode getDataNode(String path) {
        try {
            path = preparePath(path);
            return this.ram.read(path);
        } catch (Exception e) {
            Log.e(TAG, "Error: " + e.getMessage());
            return null;
        }
    }

    private String preparePath(String path) {
        if (path.charAt(0) != '/') return "/" + path;
        return path;
    }

    private void init() {
        String rootJson = this.database.read();
        if (rootJson.length() == 0) ram = new Storage();
        else {
            DataNode root = (DataNode) JsonReader.jsonToJava(rootJson);
            ram = new Storage(root);
        }
    }

    public void saveState(@Nullable IO.OnFileUpdateListener listener) {
        DataNode root = getDataNode("/");
        String rootJson = JsonWriter.objectToJson(root);
        this.database.write(rootJson.getBytes(), listener);
    }

    public String debug() {
        return ram.toString();
    }
}
