package com.zktechproductionhk.zetabaseonandroid;

import android.content.Context;
import android.support.annotation.Nullable;

import com.cedarsoftware.util.io.JsonReader;
import com.cedarsoftware.util.io.JsonWriter;
import com.zktechproductionhk.zetabaseonandroid.InternalStorage.IO;
import com.zktechproductionhk.zetabaseonandroid.Memory.DataNode;
import com.zktechproductionhk.zetabaseonandroid.Memory.Storage;

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

    public void containsKey(String path) {
        path = preparePath(path);
        ram.read(path);
    }

    public void write(String path, Object data, Storage.OnChangeListner listner) {
        path = preparePath(path);
        String json = JsonWriter.objectToJson(data);
        this.ram.write(path, json, listner);
    }

    public void write(String path, Object data) {
        path = preparePath(path);
        String json;
        if (!data.getClass().getSimpleName().equals("String"))
            json = JsonWriter.objectToJson(data);
        else json = data.toString();
        this.ram.write(path, json);
    }

    public void append(String path, Object data, Storage.OnChangeListner listner) {
        path = preparePath(path);
        String json = JsonWriter.objectToJson(data);
        this.ram.append(path, json, listner);
    }

    public void append(String path, Object data) {
        path = preparePath(path);
        String json = JsonWriter.objectToJson(data);
        this.ram.append(path, json);
    }

    public Object read(String path) {
        path = preparePath(path);
        String json = this.ram.read(path).getData();
        return JsonReader.jsonToJava(json);
    }

    public void wipe(String path, Storage.OnChangeListner listner) {
        path = preparePath(path);
        if (path.equals("/")) this.ram.purge(listner);
        this.ram.wipe(path, listner);
    }

    public void wipe(String path) {
        path = preparePath(path);
        if (path.equals("/")) this.ram.purge();
        else this.ram.wipe(path);
    }

    public void monitor(String path, Storage.Monitor monitor) {
        path = preparePath(path);
        this.ram.monitor(path, monitor);
    }

    public DataNode getDataNode(String path) {
        path = preparePath(path);
        return this.ram.read(path);
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
}
